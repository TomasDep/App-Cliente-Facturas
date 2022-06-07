package com.dev.springboot.app.controllers;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dev.springboot.app.models.entity.Cliente;
import com.dev.springboot.app.models.entity.Factura;
import com.dev.springboot.app.models.entity.ItemFactura;
import com.dev.springboot.app.models.entity.Producto;
import com.dev.springboot.app.models.service.IClienteService;

@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {
	@Autowired
	private IClienteService clienteService;
	
	@Autowired
	private MessageSource messageSource;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@GetMapping("/form/{cliente_id}")
	public String crear(
			@PathVariable(value = "cliente_id") Long clienteId, 
			Map<String, Object> model,
			RedirectAttributes flash,
			Locale locale
	) {
		Cliente cliente = this.clienteService.findOne(clienteId);
		
		if (cliente == null) {
			flash.addFlashAttribute("error", this.messageSource.getMessage("text.error.cliente.notExist", null, locale));
			return "redirect:/listar";
		}
		
		Factura factura = new Factura();
		factura.setCliente(cliente);
		
		model.put("factura", factura);
		model.put("titulo", this.messageSource.getMessage("text.form.factura.titulo", null, locale));
		
		return "factura/form";
	}
	
	@GetMapping(value = "/cargar-productos/{term}", produces= {"application/json"})
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term) {
		return this.clienteService.findByNombre(term);
	}
	
	@GetMapping("/ver/{id}")
	public String ver(
			@PathVariable(value = "id") Long id, 
			Model model, 
			RedirectAttributes flash,
			Locale locale
	) {
		Factura factura = this.clienteService.fetchByIdWithClienteWithItemFacturaWithProducto(id);
		
		if (factura == null) {
			flash.addFlashAttribute("error", "La factura no existe en la base de datos");
			return "redirect:/listar";
		}
		
		model.addAttribute("factura", factura);
		model.addAttribute("titulo", this.messageSource.getMessage("text.ver.factura.titulo", null, locale));
		
		return "factura/ver";
	}
	
	@PostMapping("/form")
	public String guardar(
			@Valid Factura factura,
			BindingResult result,
			Model model,
			@RequestParam(name = "item_id[]", required = false) Long[] itemId,
			@RequestParam(name = "cantidad[]", required = false) Integer[] cantidad,
			RedirectAttributes flash,
			SessionStatus status,
			Locale locale
	) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", this.messageSource.getMessage("text.form.factura.titulo", null, locale));
			return "factura/form";
		}
		
		if (itemId == null || itemId.length == 0) {
			model.addAttribute("titulo", this.messageSource.getMessage("text.form.factura.titulo", null, locale));
			model.addAttribute("error", this.messageSource.getMessage("text.error.factura.linesZero", null, locale));
			return "factura/form";
		}
		
		for (int i = 0; i < itemId.length; i++) {
			Producto producto = this.clienteService.findProductoById(itemId[i]);
			ItemFactura linea = new ItemFactura();
			linea.setCantidad(cantidad[i]);
			linea.setProducto(producto);
			factura.addItemFactura(linea);
			
			log.info("ID: " + itemId[i].toString() + ", cantidad: " + cantidad[i]);
		}
		
		this.clienteService.saveFactura(factura);
		status.setComplete();
		flash.addFlashAttribute("success", this.messageSource.getMessage("text.success.factura.crear", null, locale));
		
		return "redirect:/ver/" + factura.getCliente().getId();
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Locale locale) {
		Factura factura = this.clienteService.findFacturaById(id);
		
		if (factura != null) {
			this.clienteService.deleteFactura(id);
			flash.addFlashAttribute("success", this.messageSource.getMessage("text.success.factura.eliminar", null, locale));
			return "redirect:/ver/" + factura.getCliente().getId();
		}
		
		flash.addFlashAttribute("error", this.messageSource.getMessage("text.error.factura.NotExistDelete", null, locale));
		
		return "redirect:/listar";
	}
}