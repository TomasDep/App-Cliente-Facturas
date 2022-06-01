package com.dev.springboot.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.springboot.app.dao.IClienteDao;
import com.dev.springboot.app.dao.IFacturaDao;
import com.dev.springboot.app.dao.IProductoDao;
import com.dev.springboot.app.models.entity.Cliente;
import com.dev.springboot.app.models.entity.Factura;
import com.dev.springboot.app.models.entity.Producto;

@Service
public class ClienteServiceImp implements IClienteService {
	@Autowired
	private IClienteDao clienteDao;
	
	@Autowired
	private IProductoDao productoDao;
	
	@Autowired
	private IFacturaDao facturaDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) this.clienteDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findOne(Long id) {
		return this.clienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Cliente fetchByWithFacturas(Long id) {
		return this.clienteDao.fetchByIdWithFacturas(id);
	}
	
	@Override
	public Page<Cliente> findAll(Pageable pageable) {
		return this.clienteDao.findAll(pageable);
	}
	
	@Override
	@Transactional
	public void save(Cliente cliente) {
		this.clienteDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.clienteDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findByNombre(String term) {
		return this.productoDao.findByNombreLikeIgnoreCase("%" + term + "%");
	}

	@Override
	@Transactional
	public void saveFactura(Factura factura) {
		this.facturaDao.save(factura);
	}

	@Override
	@Transactional(readOnly = true)
	public Producto findProductoById(Long id) {
		return this.productoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Factura findFacturaById(Long id) {
		return this.facturaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteFactura(Long id) {
		this.facturaDao.deleteById(id);
	}

	@Override
	@Transactional
	public Factura fetchByIdWithClienteWithItemFacturaWithProducto(Long id) {
		return this.facturaDao.fetchByIdWithClienteWithItemFacturaWithProducto(id);
	}
}
