package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Pedidos;
import com.example.demo.model.Usuario;

public interface InterfaceUsuario {
	
	public Usuario sacarUsuario(Usuario u);
	public Usuario datosUsuario(String nickname);
	public List<Pedidos> listaPedidos(Usuario a);
	public void guardarPedidoEnUsuario();

}
