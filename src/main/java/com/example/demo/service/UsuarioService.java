package com.example.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.example.demo.model.Pedidos;
import com.example.demo.model.Productos;
import com.example.demo.model.Usuario;

@Service
public class UsuarioService {

	//La variable lista de usuarios
	private List<Usuario> listaUsuarios = new ArrayList<>();
	//La variable 
	private Pedidos pedidoEditar;
	//La variable para guardar lospedidos y sus cantidades
	private Map<Productos, Integer> mapaDelPedido = new HashMap<Productos, Integer>();
	//La variable para guardar la id del pedido
	private Integer id;
	
	//metodo para 
	//Si lo encuentra dice true y continuamos si no tiene que dar error(se implementara en otro lado)
	public Usuario sacarUsuario(Usuario e) {
		boolean laVerdad=false;
		Usuario usu=null;
		for (Usuario usuario : listaUsuarios) {
			if (usuario.getNickName().equalsIgnoreCase(e.getNickName())) {
				if (usuario.getContrasenia().equalsIgnoreCase(e.getContrasenia())) {
					usu=usuario;
					laVerdad=true;
					
				}
			}
		}
		if (laVerdad==true) {
			return usu;
		}else {
			return null;
		}
		
	}
	
	public Pedidos sacarPedido(Usuario a, Integer id) {
		Pedidos nuevoPedido = null;
		for (Pedidos pedidos : a.getListaPedidos()) {
			if (pedidos.getIdPedido()==id) {
				nuevoPedido= pedidos;
			}
		}
		mapaDelPedido = nuevoPedido.getProductosLista();
		pedidoEditar=nuevoPedido;
		return nuevoPedido;
	}
	
	@PostConstruct
	public void init() {
		listaUsuarios.addAll(
			Arrays.asList(new Usuario("admin","admin","admin","admin","000000001","adim@gmail.com"),
				new Usuario("usuario","usuario","usuario","la lenteja Nº3 1A","123456789","usuario@gmail.com")
				)
			);
	}

	public Pedidos getPedidoEditar() {
		return pedidoEditar;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Map<Productos, Integer> getMapaDelPedido() {
		return mapaDelPedido;
	}

	public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}
	
	

	
	
	
}
