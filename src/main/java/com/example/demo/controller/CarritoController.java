package com.example.demo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Pedidos;
import com.example.demo.model.Usuario;
import com.example.demo.service.PedidoServiceDB;
import com.example.demo.service.ProductoServiceDB;
import com.example.demo.service.UsuarioServiceDB;

@Controller
public class CarritoController {
	
	@Autowired
	private HttpSession sesion;
	
	@Autowired
	private UsuarioServiceDB servicioUsuario;
	
	@Autowired
	private PedidoServiceDB servicioPedido;
	
	@Autowired
	private ProductoServiceDB servicioProductos;
	

	
	@GetMapping({"/","login"})
	public String logearUsuario(Model model) {
		/*
		 * creamos un usuario para pasárselo al html
		 */
		model.addAttribute("usuario", new Usuario());
		return "login";
	}
	
	/*
	 * recogemos el usuario
	 * verificamos si existe
	 * en el caso de que exista guardamos la key en sesion y pasamos a la siguiente pagina
	 */
	@PostMapping("/login/submit")
	@RequestMapping (value="/login/submit", method=RequestMethod.POST)
	public String logearUsuarioSubmit(@Validated @ModelAttribute("usuario") Usuario intentoDeLogin ) {
		
		if (servicioUsuario.sacarUsuario(intentoDeLogin)==null) {
			return "login";
		}else {
			return "redirect:/login/select";
		}
	}
	
	/*
	 * Esta pagina es solo para elegir si crear un pedido o ver los existentes 
	 * tambien le digo que busque el usuario en la base de datos para sacar el nombre del usuario y que aparezca en la pagina
	 */
	
	@GetMapping("/login/select")
	public String seleccionarAccion1(Model model) {
		model.addAttribute("usuario", servicioUsuario.datosUsuario((String)sesion.getAttribute("usuario1")));
		return "select";
	}
	
	/*
	 * Aqui le paso la lista de pedidos del usuario para ver los pedidos que tiene
	 * En el HTML tienes la opcion de ver los datos del pedido modificarlo o borrarlo
	 */
	
	@GetMapping("/login/select/listaP")
	public String listaDeProductos(Model model) {
		model.addAttribute("usuario", servicioUsuario.datosUsuario((String)sesion.getAttribute("usuario1")));		
		model.addAttribute("pedidos",servicioUsuario.listaPedidos(servicioUsuario.datosUsuario((String)sesion.getAttribute("usuario1"))));
		return "listaDeProductos";
	}
	
	/*
	 * Pera hacer un nuevo pedido primero pasamos los productos para que elija 
	 */
	
	@GetMapping("/login/select/NuevoP")
	public String nuevoPedido(Model model) {
		model.addAttribute("usuario", servicioUsuario.datosUsuario((String)sesion.getAttribute("usuario1")));
		model.addAttribute("productos",servicioProductos.listaProductos());
		return "pedidoNuevo";
	}
	
	/*
	 * Recuperamos las cantidades de cada productos
	 * En el metodo se crea un producto, las lineas del pedido y las añade al pedido
	 */
	
	@PostMapping("/login/select/NuevoP/submit")
	public String nuevoPedidoSubmit(Model model, @RequestParam(name="cantidades") Integer[] nuevoProducto) {
		if(servicioProductos.addProducto(nuevoProducto)==null) {
			return "redirect:/login/select/NuevoP";
		}else {
			return "redirect:/login/select/NuevoP/envio";
		}
	}
	
	/*
	 * sacamos la lista de pedidos del pedido que acabamos de hacer
	 */
	
	@GetMapping("/login/select/NuevoP/envio")
	public String envio(Model model) {
		model.addAttribute("usuario", servicioUsuario.datosUsuario((String)sesion.getAttribute("usuario1")));
		model.addAttribute("listaPedidos",servicioPedido.ultimaListaPedido());
		return "envio";
	}
	
	/*
	 * recuperamos datos necesarios para rellenar ahora el pedido
	 * sacamos el anterior pedido en otro pedio para rellenarlo
	 * rellenamos los datos y para acabar actualizamos el pedido
	 */
	
	@PostMapping("/login/select/NuevoP/envio/submit")
	public String nuevoEnvioSubmit(Model model,
			@RequestParam(name="direccion") String direccion,
			@RequestParam(name="email") String email,
			@RequestParam(name="telefono") String telefono
			) {
		if(direccion=="" || telefono=="" || email=="") {
			return "redirect:/login/select/NuevoP/envio";
		}else {
			Pedidos pedidoGuardado = servicioPedido.ultimoPedido();
			pedidoGuardado.setCorreoElectronico(email);
			pedidoGuardado.setDireccion(direccion);
			pedidoGuardado.setTelefono(telefono);
			servicioPedido.guardarPedido(pedidoGuardado);
			return "redirect:/login/select/NuevoP/envio/resumen";
		}
	}
	
	/*
	 * mostramos todos lo datos del pedido: las lineas de pedido, el pedido y los calculos
	 * para finalizar le guardamos el pedido al usuario
	 */
	
	@GetMapping("/login/select/NuevoP/envio/resumen")
	public String resumen(Model model) {
		model.addAttribute("usuario", servicioUsuario.datosUsuario((String)sesion.getAttribute("usuario1")));
		model.addAttribute("listaPedidos",servicioPedido.ultimaListaPedido());
		model.addAttribute("direccion", servicioPedido.ultimoPedido());
		model.addAttribute("total", servicioProductos.suma());
		servicioUsuario.guardarPedidoEnUsuario();
		return "resumenEnvio";
	}
	
	/*
	 * Para editar el pedi do necesitamos la id del pedido que la recuperamos por Path
	 * Le pasamos las lineas del pedido para que aparezcan las cantidades que tenia
	 * Hacemos una comprobacion para ver si existe el pedido
	 */
	
	@GetMapping("/login/select/EditarProducto/{id}")
	public String editarPedido(@PathVariable Long id, Model model) {
		model.addAttribute("usuario", servicioUsuario.datosUsuario((String)sesion.getAttribute("usuario1")));
		model.addAttribute("lista",servicioPedido.ultimaListaPedido());
		if (servicioPedido.sacarPedido(id)==null) {
			return "redirect:/login/select/listaP";
		}else {
			sesion.setAttribute("IdUltimoPedido", id);
			return "pedidoNuevoEdit";
		}
	}
	
	/*
	 * Recuperamos las cantidades de cada producto y a la vez que se sobre escribe comprobamos si todas las cantidades son mayores a 0
	 * En caso de que todo sea 0 lo devolvemos a la pagina
	 */
	
	@PostMapping("/login/select/EditarProducto/submit")
	public String editarPedidoSubmit(@RequestParam(name="cantidades") Integer[] nuevoProducto) {
		if(servicioPedido.editPedido(nuevoProducto)==null) {
			return "redirect:/login/select/EditarProducto/"+sesion.getAttribute("IdUltimoPedido");
		}else {
			return "redirect:/login/select/EditarProducto/EditarEnvio";
		}
	}
	
	/*
	 * Le pasamos la lista de lineas de pedido para mostrar los pedidos y las cantidades
	 */
	
	@GetMapping("/login/select/EditarProducto/EditarEnvio")
	public String editarEnvio(Model model) {
		model.addAttribute("usuario", servicioUsuario.datosUsuario((String)sesion.getAttribute("usuario1")));
		model.addAttribute("listaPedidos", servicioPedido.ultimaListaPedido());
		return "envioEdit";
	}
	
	/*
	 * recuperamos datos necesarios para rellenar ahora el pedido
	 * sacamos el anterior pedido en otro pedio para rellenarlo
	 * rellenamos los datos y para acabar actualizamos el pedido
	 */
	
	@PostMapping("/login/select/EditarProducto/EditarEnvio/submit")
	public String editarEnvioSubmit(Model model, @ModelAttribute("nuevaDireccion")
			@RequestParam(name="direccion") String direccion,
			@RequestParam(name="email") String email,
			@RequestParam(name="telefono") String telefono
			) {
		if(direccion=="" || telefono=="" || email=="") {
			return "redirect:/login/select/EditarProducto/EditarEnvio";
		}else {
			Pedidos pedidoGuardado = servicioPedido.ultimoPedido();
			pedidoGuardado.setCorreoElectronico(email);
			pedidoGuardado.setDireccion(direccion);
			pedidoGuardado.setTelefono(telefono);
			servicioPedido.guardarPedido(pedidoGuardado);
			return "redirect:/login/select/NuevoP/envio/resumenEditado";
		}
	}
	
	/*
	 * mostramos todos lo datos del pedido: las lineas de pedido, el pedido y los calculos
	 * para finalizar le guardamos el pedido al usuario
	 */
	
	@GetMapping("/login/select/NuevoP/envio/resumenEditado")
	public String resumenEdit(Model model) {
		model.addAttribute("usuario", servicioUsuario.datosUsuario((String)sesion.getAttribute("usuario1")));
		model.addAttribute("listaPedidos",servicioPedido.ultimaListaPedido());
		model.addAttribute("direccion", servicioPedido.ultimoPedido());
		model.addAttribute("total", servicioProductos.suma());
		return "resumenEnvio";
	}
	
	/*
	 * Para borrar e pedido recojemos la id y va al metodo de borrar que borra el pedido del usuario, las lineas del pedido y finalmente el pedido
	 */
	
	@PostMapping("/login/select/listaP/borrar/{id}")
	public String borrarProducto(@PathVariable Long id, Model model) {
		servicioPedido.borrarPedido(id, (String) sesion.getAttribute("usuario1"));
		return "redirect:/login/select/listaP";
	}
	
	/*
	 * Como vamos a mostrar los datos del pedido lo que hacemos es sacar los datos del pedido que nos pasen por la id
	 */
	
	@GetMapping("/login/select/listaP/datos/{id}")
	public String datosPedido(Model model,@PathVariable Long id) {
		model.addAttribute("usuario", servicioUsuario.datosUsuario((String)sesion.getAttribute("usuario1")));
		model.addAttribute("listaPedidos",servicioPedido.sacarListaPedido(id));
		model.addAttribute("direccion", servicioPedido.sacarPedido(id));
		model.addAttribute("total", servicioProductos.sumaConcreta(id));
		return "datosPedido";
	}
	
	/*
	 * para cerrar la sesion la invalidamos y lo devolvemos al login
	 */
	
	@GetMapping("/cerrarSesion")
	public String cerrarSesion() {
		sesion.invalidate();
		return "redirect:/login";
	}
	

	
	
	
	
	
	
	
}