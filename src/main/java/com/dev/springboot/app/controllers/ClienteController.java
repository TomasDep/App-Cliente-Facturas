package com.dev.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

	@Autowired
	private MessageSource messageSource;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private static final String IMG_EXTENSION_VALIDATION = "image\\/(jpg|jpeg|png)$";
	
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
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
	
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	@GetMapping("/ver/{id}")
	public String ver(
			@PathVariable(value = "id") Long id, 
			Map<String, Object> model, 
			RedirectAttributes flash,
			Locale locale
	) {
		Cliente cliente = this.clienteService.fetchByWithFacturas(id);
		
		if (cliente == null) {
			flash.addFlashAttribute("error", this.messageSource.getMessage("text.error.cliente.notExist", null, locale));
			return "redirect:/listar";
		}
		
		String titulo = String.format(this.messageSource.getMessage("text.ver.titulo", null, locale), cliente.getNombre().concat(" ").concat(cliente.getApellido()));
		
		model.put("cliente", cliente);
		model.put("titulo", titulo);
		
		return "ver";
	}
	
	@GetMapping({"/listar", "/"})
	public String listar(
			@RequestParam(name = "page", defaultValue = "0") int page, 
			Model model,
			Authentication authentication,
			HttpServletRequest request,
			Locale locale
	) {
		if (authentication != null) {
			logger.info("Usuario autenticado, USERNAME: ".concat(authentication.getName()));
		}
		
		if (request.isUserInRole("ADMIN")) {
			logger.info("Administrador ".concat(authentication.getName().concat(" tienes acceso a los recursos")));
		} else {
			logger.info("Usuario ".concat(authentication.getName().concat(" NO tienes acceso a los recursos")));
		}
		
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Cliente> clientes = this.clienteService.findAll(pageRequest);
		
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
		
		model.addAttribute("titulo", this.messageSource.getMessage("text.listar.cliente.titulo", null, locale));
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		
		return "listar";
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/form")
	public String crear(Map<String, Object> model, Locale locale) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", this.messageSource.getMessage("text.form.cliente.crear.titulo", null, locale));
		return "form";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/form/{id}")
	public String editar(
			@PathVariable(value = "id") Long id, Map<String, Object> model, 
			RedirectAttributes flash,
			Locale locale
	) {
		Cliente cliente = null;
		
		if (id > 0) {
			cliente = this.clienteService.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", this.messageSource.getMessage("text.error.cliente.idError", null, locale));
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", this.messageSource.getMessage("text.error.cliente.idZero", null, locale));
			return "redirect:/listar";
		}
		
		model.put("cliente", cliente);
		model.put("titulo", this.messageSource.getMessage("text.form.cliente.editar.titulo", null, locale));
		return "form";
	}
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/form")
	public String guardar(
			@Valid Cliente cliente, 
			BindingResult result, 
			Model model,
			@RequestParam("file") MultipartFile foto,
			RedirectAttributes flash,
			SessionStatus status,
			Locale locale
	) {
		if (result.hasErrors()) {
			if (cliente.getId() != null) {
				model.addAttribute("titulo", this.messageSource.getMessage("text.form.cliente.editar.titulo", null, locale));
			} else {
				model.addAttribute("titulo", this.messageSource.getMessage("text.form.cliente.crear.titulo", null, locale));
			}
			
			return "form";
		}
		
		if (!foto.getContentType().matches(IMG_EXTENSION_VALIDATION)) {
			flash.addFlashAttribute("error", this.messageSource.getMessage("text.error.cliente.foto.extension", null, locale));
			if (cliente.getId() != null) {
				model.addAttribute("titulo", this.messageSource.getMessage("text.form.cliente.editar.titulo", null, locale));
				return "redirect:/form/" + cliente.getId();
			} else {
				model.addAttribute("titulo", this.messageSource.getMessage("text.form.cliente.crear.titulo", null, locale));
				return "redirect:/form";
			}
		}
		
		String uniqueFilename = "";
		
		if (!foto.isEmpty()) {
			if (
				cliente.getId() != null && 
				cliente.getId() > 0 && 
				cliente.getFoto() != null && 
				cliente.getFoto().length() > 0
			) {
				this.uploadFileService.delete(cliente.getFoto());
			}
			
			try {
				uniqueFilename = this.uploadFileService.copy(foto);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			flash.addFlashAttribute("info", this.messageSource.getMessage("text.info.cliente.foto", null, locale));
			cliente.setFoto(uniqueFilename);
		} else {
			cliente.setFoto(uniqueFilename);
		}
		
		String mensajeFlash = null;
		
		if (cliente.getId() != null) {
			mensajeFlash = this.messageSource.getMessage("text.success.cliente.editar", null, locale);
		} else {
			mensajeFlash = this.messageSource.getMessage("text.success.cliente.crear", null, locale);
		}
		
		this.clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:/listar";
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Locale locale) {
		if (id > 0) {
			Cliente cliente = this.clienteService.findOne(id);
			this.clienteService.delete(id);
			
			flash.addFlashAttribute("success", this.messageSource.getMessage("text.success.cliente.eliminar", null, locale));
					
			if (this.uploadFileService.delete(cliente.getFoto())) {
				flash.addFlashAttribute("info", this.messageSource.getMessage("text.info.cliente.deleteFoto", null, locale));
			}
		}
		
		return "redirect:/listar";
	}
}
