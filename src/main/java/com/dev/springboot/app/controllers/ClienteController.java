package com.dev.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dev.springboot.app.models.entity.Cliente;
import com.dev.springboot.app.models.service.IClienteService;
import com.dev.springboot.app.models.service.IUploadFileService;
import com.dev.springboot.app.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente")
public class ClienteController {
	@Autowired
	private IClienteService clienteService;
	
	@Autowired
	private IUploadFileService uploadFileService;
	
	@GetMapping("/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
		
		Resource recurso = null;
		
		try {
			recurso = this.uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}
	
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		// Cliente cliente = this.clienteService.findOne(id);
		Cliente cliente = this.clienteService.fetchByWithFacturas(id);
		
		if (cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		}
		
		model.put("cliente", cliente);
		model.put("titulo", "Detalle cliente: " + cliente.getNombre());
		
		return "ver";
	}
	
	@GetMapping({"/listar", "/"})
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Cliente> clientes = this.clienteService.findAll(pageRequest);
		
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
		
		model.addAttribute("titulo", "Listado de Clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}
	
	@GetMapping("/form")
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de Cliente");
		return "form";
	}
	
	@GetMapping("/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = null;
		
		if (id > 0) {
			cliente = this.clienteService.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", "El ID de cliente no existe en la base de datos...");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del cliente no puede ser cero...");
			return "redirect:/listar";
		}
		
		model.put("cliente", cliente);
		model.put("titulo", "Editar Cliente");
		return "form";
	}
	
	@PostMapping("/form")
	public String guardar(
			@Valid Cliente cliente, 
			BindingResult result, 
			Model model,
			@RequestParam("file") MultipartFile foto,
			RedirectAttributes flash,
			SessionStatus status
	) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Cliente");
			return "form";
		}
		
		if (!foto.isEmpty()) {
			if (
				cliente.getId() != null && 
				cliente.getId() > 0 && 
				cliente.getFoto() != null && 
				cliente.getFoto().length() > 0
			) {
			
				this.uploadFileService.delete(cliente.getFoto());
				
			}
			
			String uniqueFilename = null;
			
			try {
				uniqueFilename = this.uploadFileService.copy(foto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");
			cliente.setFoto(uniqueFilename);
		}
		
		String mensajeFlash = (cliente.getId() != null)? "Cliente editado con éxito" : "Cliente creado con éxito";
		
		this.clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:/listar";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {
			Cliente cliente = this.clienteService.findOne(id);
			this.clienteService.delete(id);
			
			flash.addFlashAttribute("success", "Cliente eliminado con éxito");
					
			if (this.uploadFileService.delete(cliente.getFoto())) {
				flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con éxito");
			}
		}
		
		return "redirect:/listar";
	}
}
