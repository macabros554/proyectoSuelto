package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.LineaPedido;
import com.example.demo.model.Pedidos;
import com.example.demo.model.Productos;
import com.example.demo.model.Usuario;
import com.example.demo.repository.LineaPedidoRepository;
import com.example.demo.repository.PedidosRepository;
import com.example.demo.repository.ProductosRepository;
import com.example.demo.repository.UsuarioRepository;

@Service("pedidoServiceDB")
public class PedidoServiceDB {
	
	@Autowired
	private HttpSession sesion;
	
	@Autowired
	private PedidosRepository repoPedido;
	
	@Autowired
	private ProductosRepository repoProductos;
	
	@Autowired
	private LineaPedidoRepository repoLineaDePedido;
	
	@Autowired
	private UsuarioRepository repoUsuario;
	
	/*
	 * sacamos la lista de lineas de pedido apartir de la id del pedido que esta guardada en la sesion
	 */
	
	public List<LineaPedido> ultimaListaPedido() {
		return repoPedido.getById((Long) sesion.getAttribute("IdUltimoPedido")).getListaLineaPedidos();
	}
	
	/*
	 * sacamos la lista de lineas de pedido apartir de la id del pedido que pide el metodo
	 */
	
	public List<LineaPedido> sacarListaPedido(Long id) {
		return repoPedido.getById(id).getListaLineaPedidos();
	}
	
	/*
	 * 
	 */
	
	public Pedidos sacarPedido(Long id) {
		return repoPedido.getById(id);
	}

	/*
	 * sacamos el pedido apartir de la id del pedido que esta guardada en la sesion
	 */
	
	public Pedidos ultimoPedido() {
		return repoPedido.getById((Long) sesion.getAttribute("IdUltimoPedido"));
	}

	/*
	 * guardamos el pedido en la base de datos, el pedido lo pasamos por parametro
	 */
	
	public void guardarPedido(Pedidos pedidoEntrante) {
		repoPedido.save(pedidoEntrante);
	}
	
	/*
	 * Editamos el pedido aunque aqui se editan las lineas es igual que al crearlo pero en vez de crear un pedido nuevo 
	 * recuperamos el pedido que queremos modificar y sobre escribimos las lineas de pedido existentes.
	 */
	
	public Pedidos editPedido(Integer[] cantidades) {
		int comprobar=0;
		LineaPedido lineaPedido;
		Pedidos pedido = ultimoPedido();
		List<LineaPedido> listaLineaPedidos = new ArrayList<>();
		listaLineaPedidos=pedido.getListaLineaPedidos();
		
		for (int i = 0; i < cantidades.length; i++) {
			lineaPedido=listaLineaPedidos.get(i);
			lineaPedido.setCantidad(cantidades[i]);
			listaLineaPedidos.get(i).setCantidad(cantidades[i]);
			repoLineaDePedido.save(lineaPedido);
			comprobar+=cantidades[i];
		}
		if (comprobar>=1) {
			pedido.setListaLineaPedidos(listaLineaPedidos);
			repoPedido.save(pedido);
			sesion.setAttribute("IdUltimoPedido", pedido.getId());
			return pedido;
		}else {
			return null;
		}
	}
	
	/*
	 * Para borrar el pedido le pasamos la id del pedido y la PK del usuario
	 * Sacamos el pedido y el usuario de la base de datos
	 * del usuario borramos el pedido de su lista de pedidos y guardamos los cambios
	 * luego borramos las lines de pedido usando la lista de linesa de pedido del pedido y para finalizar borramos el pedido
	 */
	
	public void borrarPedido(Long id, String usuarioName) {
		Pedidos pedido = repoPedido.findById(id).orElse(null);
		Usuario usuario = repoUsuario.findById(usuarioName).orElse(null);
		usuario.getListaPedidos().remove(pedido);
		repoUsuario.save(usuario);
		
		for (LineaPedido linea: pedido.getListaLineaPedidos()) {
			repoLineaDePedido.delete(linea);
		}
		
		repoPedido.delete(pedido);
		
	}
	
	
	
}
