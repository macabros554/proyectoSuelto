package com.example.demo.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="lineaPedido")
public class LineaPedido {

	/*
	 * aqui estan las cantidades de cada producto que ha comprado
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private int cantidad;
    @ManyToOne
    private Productos producto;
    @ManyToOne
    private Pedidos pedido;
  
    public LineaPedido(){}
    
	public LineaPedido(int cantidad, Productos producto, Pedidos pedido) {
		super();
		this.cantidad = cantidad;
		this.producto = producto;
		this.pedido = pedido;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public Productos getProducto() {
		return producto;
	}
	public void setProducto(Productos producto) {
		this.producto = producto;
	}
	public Pedidos getPedido() {
		return pedido;
	}
	public void setPedido(Pedidos pedido) {
		this.pedido = pedido;
	}
	
	/*
	 * generamos hashCode, equals y toString
	 */

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LineaPedido other = (LineaPedido) obj;
		return id == other.id;
	}
	
	@Override
	public String toString() {
		return "LineaPedido [id=" + id + ", cantidad=" + cantidad + "]";
	}
    
    
    
	
}
