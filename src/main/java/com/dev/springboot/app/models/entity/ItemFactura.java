package com.dev.springboot.app.models.entity;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "facturas_items")
public class ItemFactura implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Integer cantidad;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "producto_id")
	private Producto producto;
	
	private static final long serialVersionUID = 1L;

	public Long getId() {
		return id;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	
	public Double calcularImporte() {
		return cantidad.doubleValue() * producto.getPrecio();
	}
}
