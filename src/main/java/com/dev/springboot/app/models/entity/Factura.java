package com.dev.springboot.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "facturas")
public class Factura implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String descripcion;
	
	private String observacion;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Cliente cliente;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "factura_id")
	private List<ItemFactura> items;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "create_at")
	private Date createAt;
	
	private static final long serialVersionUID = 1L;

	public Factura() {
		this.items = new ArrayList<ItemFactura>();
	}
	
	@PrePersist
	public void prePersist() {
		createAt = new Date();
	}
	
	public Long getId() {
		return id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getObservacion() {
		return observacion;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public Cliente getCliente() {
		return cliente;
	}
	
	public List<ItemFactura> getItems() {
		return items;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Double getTotal() {
		Double total = 0.0;
		int size = items.size();
		
		for (int i = 0; i < size; i++) {
			total += items.get(i).calcularImporte();
		}
		
		return total;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setItems(List<ItemFactura> items) {
		this.items = items;
	}
	
	public void addItemFactura(ItemFactura item) {
		this.items.add(item);
	}
}
