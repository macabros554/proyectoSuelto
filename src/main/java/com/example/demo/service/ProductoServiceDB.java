package com.example.demo.service;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.LineaPedido;
import com.example.demo.model.Pedidos;
import com.example.demo.model.Productos;
import com.example.demo.repository.LineaPedidoRepository;
import com.example.demo.repository.PedidosRepository;
import com.example.demo.repository.ProductosRepository;

@Service("productoServiceDB")
public class ProductoServiceDB implements InterfaceProductos{
	
	@Autowired
	private ProductosRepository repoProductos;
	
	@Autowired
	private PedidosRepository repoPedido;
	
	@Autowired
	private LineaPedidoRepository repoLineaDePedido;
	
	@Autowired
	private HttpSession sesion;
	
	/*
	 * Creamos las variables que vamos a usar que son, un comprobador pasa saber si hay pedidos, 2 listas una para los productos,la otra para las lineas de pedido y
	 * un pedido nuevo para añadirle las litas(guardamos el pedido)
	 * luego despues de añadir la ingormacion a cada lineas de pedido lo guardamos en base de datos y le añadimos la linea al pedido
	 * para  finalizar guardamos el pedido con sus lineas en la base de datos
	 */
	
	@Override
	public Pedidos addProducto(Integer[] cantidades) {
		int comprobar=0;
		
		for (int i = 0; i < cantidades.length; i++) {
			comprobar+=cantidades[i];
		}
		if (comprobar>=1) {
			List<Productos> listaProductos=repoProductos.findAll();
			LineaPedido lineaPedido;
	
			Pedidos pedido = new Pedidos();
			repoPedido.save(pedido);
			List<LineaPedido> listaLineaPedidos;
			
			for (int i = 0; i < cantidades.length; i++) {
				
				lineaPedido=new LineaPedido(cantidades[i], listaProductos.get(i), pedido);
				repoLineaDePedido.save(lineaPedido);
				listaLineaPedidos=pedido.getListaLineaPedidos();
				listaLineaPedidos.add(lineaPedido);
				pedido.setListaLineaPedidos(listaLineaPedidos);
				
			}
		
			repoPedido.save(pedido);
			sesion.setAttribute("IdUltimoPedido", pedido.getId());
			return pedido;
		}else {
			return null;
		}
	}
	
	/*
	 * Saca la lista de productos de la base de datos
	 */

	@Override
	public List<Productos> listaProductos() {
		return repoProductos.findAll();
	}
	
	/*
	 * debuelbe el calculo de sumar y multiplicar el precio y cantidades de cada linea de preoducto de un pedido (el pedido lo recupera usando la id que esta en sesion)
	 */

	@Override
	public Double suma() {
		Double resultado=0.0;
		for (LineaPedido a : repoPedido.getById((Long) sesion.getAttribute("IdUltimoPedido")).getListaLineaPedidos()) {
			resultado+= a.getCantidad()*a.getProducto().getPrecio();
		}
		return resultado;
	}
	
	/*
	 * debuelbe el calculo de sumar y multiplicar el precio y cantidades de cada linea de preoducto de un pedido 
	 * El pedido lo recupera usando la id pasada por parametro
	 */
	
	public Double sumaConcreta(Long id) {
		Double resultado=0.0;
		for (LineaPedido a : repoPedido.getById(id).getListaLineaPedidos()) {
			resultado+= a.getCantidad()*a.getProducto().getPrecio();
		}
		return resultado;
	}	
	
}
