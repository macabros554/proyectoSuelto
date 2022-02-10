package com.example.demo.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.demo.model.Pedidos;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;

@Primary
@Service("usuarioServiceDB")
public class UsuarioServiceDB implements InterfaceUsuario{

	@Autowired
	private UsuarioRepository repoUsuario;
	
	@Autowired
	private HttpSession sesion;
	
	@Autowired
	private PedidoServiceDB servicioPedido;
	
	/*
	 * Comprueba su el usuario esta en la base de datos y si la contraseña pertenece a ese usuario
	 */
	
	@Override
	public Usuario sacarUsuario(Usuario e) {
		for (Usuario usuario : repoUsuario.findAll()) {
			if (usuario.getNickName().equalsIgnoreCase(e.getNickName())) {
				if (usuario.getContrasenia().equalsIgnoreCase(e.getContrasenia())) {
					sesion.setAttribute("usuario1", usuario.getNickName());
					return usuario;
				}
			}
		}
		return null;
	}

	/*
	 * devuelve la lista de pedidos de la lista de pedidos del usuario que se para por parametro
	 */
	
	@Override
	public List<Pedidos> listaPedidos(Usuario a) {
		return a.getListaPedidos();
	}
	
	/*
	 * le añade al usuario el nuevo pedido y lo guarda en la base de datos
	 */

	@Override
	public void guardarPedidoEnUsuario() {
		Usuario usu=datosUsuario((String)sesion.getAttribute("usuario1"));
		List<Pedidos> listaPedidos;
		listaPedidos=usu.getListaPedidos();
		listaPedidos.add(servicioPedido.ultimoPedido());
		usu.setListaPedidos(listaPedidos);
		repoUsuario.save(usu);
	}
	
	/*
	 * Pasa el PK de usuario en mi caso el nickname y te devuelve los datos del usuario que esta guardado en la base de datos
	 */

	@Override
	public Usuario datosUsuario(String nickname) {
		return repoUsuario.getById(nickname);
	}

	/*
	 * recordatorio 
	 * En caso de que la erramienta de eclipse que se llama variables no funcione 
	 * usar syso para ver lo que contiene la variable
	 */
}
