package com.example.demo.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Pedidos {
	
	private Date fechaPack;
	private static Integer cont=0;
	private String direccion;
	private String telefono;
	private String correoElectronico;
	private Integer idPedido;
	private Map<Productos, Integer> productosLista = new HashMap<Productos, Integer>();
	
	public Pedidos() {}

	public Pedidos(String direccion, String telefono, String correoElectronico) {
		super();
		this.direccion = direccion;
		this.telefono = telefono;
		this.correoElectronico=correoElectronico;
		this.fechaPack =new Date();
		this.idPedido=cont;
		aumentarCont();
	}

	public void rellenarLista(Map<Productos, Integer> nuevoProductosLista) {
		productosLista=nuevoProductosLista;
	}


	public Date getFechaPack() {
		return fechaPack;
	}
	
	private static void aumentarCont() {
		cont++;
	}

	public void setFechaPack(Date fechaPack) {
		this.fechaPack = fechaPack;
	}
	
	public Integer getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(Integer idPedido) {
		this.idPedido = idPedido;
	}

	public static Integer getReferencia() {
		return cont;
	}

	public static void setReferencia(Integer referencia) {
		Pedidos.cont = referencia;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public Map<Productos, Integer> getProductosLista() {
		return productosLista;
	}

	public void setProductosLista(Map<Productos, Integer> productosLista) {
		this.productosLista = productosLista;
	}
	
	
	
	
}
