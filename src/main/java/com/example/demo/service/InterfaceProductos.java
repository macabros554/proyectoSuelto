package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Pedidos;
import com.example.demo.model.Productos;

public interface InterfaceProductos {

	public Pedidos addProducto(Integer[] cantidades);
	public Double suma();
	public List<Productos> listaProductos();

}
