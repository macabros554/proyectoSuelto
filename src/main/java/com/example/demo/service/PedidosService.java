package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Pedidos;
import com.example.demo.model.Productos;
import com.example.demo.model.Usuario;

@Service
public class PedidosService {
	
	@Autowired
	private ProductoService sevicioProductos;
	@Autowired
	private UsuarioService sevicioUsuario;

	private Pedidos ultimoPedido;
	//La variable para guardar lospedidos y sus cantidades
	private Map<Productos, Integer> productosLista = new HashMap<Productos, Integer>();
	
	//variable para guardar el precio total del pedido
	private double precioTotal;
	//variable para guardar el precio total del pedido mas el IVA
	private double precioTotalIVA;
	//variable para el IVA estandar
	private double iva=1.21;
	
	//le añade el pedido al usuario
	public boolean addPedido(Pedidos e,Usuario a) {
		//creamos un nuevo pedido para que funcione
		ultimoPedido = new Pedidos(e.getDireccion(),e.getTelefono(),e.getCorreoElectronico());
		//metemos los productos en el mapa de pedidos
		ultimoPedido.rellenarLista(productosLista);
		//añadimos el pedido al usuario
		return a.getListaPedidos().add(ultimoPedido);
	}
	/**metodo para editar pedido
	 * Del usuario a saca la lista para recorrerla
	 * Luego al usuario le quitamos el pedido con la misma ID y le añadimos el pedido modificado
	 * Tambien hay que recordar añadir la fecha y ID
	 * 
	 */
	public void editarPedido(Pedidos e,Usuario a) {
		boolean encontrado = false;
		int i = 0;
		Usuario user = sevicioUsuario.sacarUsuario(a);
		List<Pedidos> listaPedidos=user.getListaPedidos();
		while (!encontrado && i < listaPedidos.size()) {
			if (listaPedidos.get(i).getIdPedido() == sevicioUsuario.getId()) {
				encontrado = true;
				e.setFechaPack(listaPedidos.get(i).getFechaPack());
				e.setIdPedido(listaPedidos.get(i).getIdPedido());
				e.setProductosLista(productosLista);
				a.getListaPedidos().remove(i);
				a.getListaPedidos().add(e);
			} else {
				i++;
			}
		}
	}
	
	//este metodo es solo para mostrar el pedido cuando hagas el envio
	public Pedidos mostrarUltimoPedido() {
		return ultimoPedido;
	}
	
	public void setUltimoPedido(Pedidos ultimoPedido) {
		this.ultimoPedido = ultimoPedido;
	}
	//Este metodo es para mostrar la lista de pedidos que tiene guardada el usuario
	public List<Pedidos> getListaProductos(Usuario a) {
		return a.getListaPedidos();
	}
	//Aqui metemos los pedidos en la lista de productos y ya que estamos hacemos el precio total y el precio con IVA
	public void meterPedidos(Integer[] cantidades) {
		precioTotal=0;
		Map<Productos, Integer> caja = new HashMap<Productos, Integer>();
		List<Productos> listaProductos = this.sevicioProductos.getListaProductos();
		
		for (int i = 0; i < cantidades.length; i++) {
			precioTotal+=cantidades[i]*listaProductos.get(i).getPrecio();
			caja.put(listaProductos.get(i), cantidades[i]);
		}
		precioTotalIVA=precioTotal*iva;
		this.productosLista=caja;
	}

	//Este metodo es para sacar los pedidos de un cierto usuario
	public List<Pedidos> sacarPedidos(Usuario e) {
		
		for (Usuario usuario : sevicioUsuario.getListaUsuarios()) {
			if (usuario.getNickName().equalsIgnoreCase(e.getNickName())) {
				if (usuario.getContrasenia().equalsIgnoreCase(e.getContrasenia())) {
					return usuario.getListaPedidos();
				}
			}
		}
		return null;
	}
	
	public double getPrecioTotal() {
		return precioTotal;
	}

	public double getPrecioTotaliva() {
		return precioTotalIVA;
	}
	
	

	
	
}
