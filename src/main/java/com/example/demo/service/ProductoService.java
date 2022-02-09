package com.example.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.example.demo.model.Productos;

@Service
public class ProductoService {

	private ArrayList<Productos> listaProductos = new ArrayList<Productos>();
	//La variable para guardar lospedidos y sus cantidades
	private Map<Productos, Integer> listaCantidades = new HashMap<Productos, Integer>();
	
	@PostConstruct
	public void init() {
		listaProductos.addAll(Arrays.asList(new Productos("Leche",1.20),new Productos("Cafe",1.80),
				new Productos("Manzana",0.32),new Productos("Lechuga",0.80),new Productos("Avena",2.10)));
	}
	
	public ProductoService(ArrayList<Productos> listaProductos) {
		super();
		this.listaProductos = listaProductos;
	}
	
	public ArrayList<Productos> getListaProductos() {
		return listaProductos;
	}

	public void setListaProductos(ArrayList<Productos> listaProductos) {
		this.listaProductos = listaProductos;
	}
	
	public Map<Productos, Integer> getListaCantidades() {
		return listaCantidades;
	}

	public void setListaCantidades(Map<Productos, Integer> listaCantidades) {
		this.listaCantidades = listaCantidades;
	}

	/**Este metodo sirve para rellenar la lista de productos y meter sus cantidades
	 * hace un for que solo añade pedidos si la cantidad es mayor a 0
	 */
	public void meterProducto(Integer[] cantidades) {
		Map<Productos, Integer> productos = new HashMap<Productos, Integer>();

		for (int i = 0; i < cantidades.length; i++) {
			if(cantidades[i]>=1) {
				productos.put(listaProductos.get(i), cantidades[i]);
			}
		}
		//la segunda lista es porque sobre escribe el primer pedido
		this.listaCantidades=productos;
	}
	


	
}
