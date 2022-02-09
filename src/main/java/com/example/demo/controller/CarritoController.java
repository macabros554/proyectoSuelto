package com.example.demo.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
import com.example.demo.model.Productos;
import com.example.demo.model.Usuario;
import com.example.demo.service.PedidosService;
import com.example.demo.service.ProductoService;
import com.example.demo.service.UsuarioService;

@Controller
public class CarritoController {

	//creamos los autowired de los servicios porque los usaremos todos
	
	@Autowired
	private UsuarioService servicioUsuario;
	
	@Autowired
	private PedidosService servicioPedido;
	
	@Autowired
	private HttpSession sesion;
	
	@Autowired
	private ProductoService servicioProductos;
	
	
	@GetMapping({"/","login"})
	public String logearUsuario(Model model) {
		//añadimos el model usuario para poder usarlo y recogerlo en el post
		model.addAttribute("usuario", new Usuario());
		return "/login";
	}
	
	@PostMapping("login/submit")
	@RequestMapping (value="/login/submit", method=RequestMethod.POST)
	public String logearUsuarioSubmit(@Validated @ModelAttribute("usuario") Usuario intentoDeLogin,BindingResult bindingResult ) {
		//Con el ModelAttribute recogemos el usuario y lo comparamos con los existentes
		//si existe te lleva al selector si no te devuelve al login
		if (servicioUsuario.sacarUsuario(intentoDeLogin)==null) {
			return "login";
		} else {
			//guardamos el usuario en la sesion
			sesion.setAttribute("usuario1", servicioUsuario.sacarUsuario(intentoDeLogin));
			return "redirect:/login/select";
		}
	}
	
	@GetMapping("login/select")
	public String seleccionarAccion1(Model model) {
		//Desde de aqui creo siempre el model ususario para mostrar siempre el usuario en todas las paginas
		model.addAttribute("usuario", sesion.getAttribute("usuario1"));
		return "/select";
	}

	
	@GetMapping("login/select/listaP")
	public String listaDeProductos(Model model) {
		model.addAttribute("usuario", sesion.getAttribute("usuario1"));
		//Saco de la sesion del usuario sus pedidos para mostrarlos
		model.addAttribute("pedidos",servicioPedido.sacarPedidos((Usuario) sesion.getAttribute("usuario1")));
		return "/listaDeProductos";
	}
	
	@GetMapping("login/select/NuevoP")
	public String nuevoPedido(Model model) {
		model.addAttribute("usuario", sesion.getAttribute("usuario1"));
		//Saco todos los productos creados para posterior mente reutilizarlos para crear un mapa con la cantidad
		model.addAttribute("Productos",servicioProductos.getListaProductos());
		return "/pedidoNuevo";
	}
	
	@PostMapping("login/select/NuevoP/submit")
	public String nuevoPedidoSubmit(Model model, @RequestParam(name="cantidades") Integer[] nuevoProducto) {
		//metemos las cantidades y productos en un mapa
		this.servicioProductos.meterProducto(nuevoProducto);
		this.servicioPedido.meterPedidos(nuevoProducto);
		//confirmamos que no este vacio con una variable que tengo en servicio pedido
		if(servicioPedido.getPrecioTotal()==0) {
			return "redirect:/login/select/NuevoP";
		}else {
			return "redirect:/login/select/NuevoP/envio";
		}
		
	}
	
	@GetMapping("login/select/NuevoP/envio")
	public String envio(Model model) {
		//creamos un pedido para usarlo en envio
		model.addAttribute("direccion", new Pedidos());
		model.addAttribute("usuario", sesion.getAttribute("usuario1"));
		//sacamos las el mapa con productos y cantidades para verlo en el envio
		model.addAttribute("datosPedido", this.servicioProductos.getListaCantidades());
		return "envio";
	}
	
	@PostMapping("login/select/NuevoP/envio/submit")
	public String nuevoEnvioSubmit(Model model, @ModelAttribute("direccion") Pedidos pedidoNuevo,
			@RequestParam(name="direccion") String direccion,
			@RequestParam(name="email") String email,
			@RequestParam(name="telefono") String telefono
			) {
		//confirmamos que el usuario no a dejado nada en blanco
		if(direccion=="" || telefono=="" || email=="") {
			return "redirect:/login/select/NuevoP/envio";
		}else {
			//rellenamos pedidoNuevo con los datos que acabamos de recojer
			pedidoNuevo.setCorreoElectronico(email);
			pedidoNuevo.setDireccion(direccion);
			pedidoNuevo.setTelefono(telefono);
			//guardamos ep pedido dentro del usuario
			servicioPedido.addPedido(pedidoNuevo,(Usuario) sesion.getAttribute("usuario1"));
			return "redirect:/login/select/NuevoP/envio/resumen";
		}
	}
	
	@GetMapping("login/select/NuevoP/envio/resumen")
	public String resumen(Model model) {
		//mostramos todos los datos que ha rellenado y cuanto cuesta en total lo que a comprado
		model.addAttribute("usuario", sesion.getAttribute("usuario1"));
		model.addAttribute("datosPedido", this.servicioProductos.getListaCantidades());
		model.addAttribute("direccionPedido", servicioPedido.mostrarUltimoPedido());	
		model.addAttribute("precioFinal", servicioPedido.getPrecioTotal());
		model.addAttribute("precioFinalConIVA", servicioPedido.getPrecioTotaliva());
		return "/resumenEnvio";
	}
	
	
	@GetMapping("login/select/EditarProducto/{id}")
	public String editarPedido(@PathVariable Integer id, Model model) {
		model.addAttribute("usuario", sesion.getAttribute("usuario1"));
		//creamos un pedido para guardar el pedido a modificar del usuario
		Pedidos pedidoResguardo= servicioUsuario.sacarPedido((Usuario) sesion.getAttribute("usuario1"), id);
		//confirmamos que no a pasado una id nula
		if(pedidoResguardo!=null) {
			//guardamos la id del pedido en el usuario
			servicioUsuario.setId(id);
			model.addAttribute("lista",pedidoResguardo.getProductosLista());
			return "/pedidoNuevoEdit";
		}else {
			return "redirect:/login/select/NuevoP";
		}
	}
	
	@PostMapping("login/select/EditarProducto/submit")
	public String editarPedidoSubmit(@RequestParam(name="cantidades") Integer[] nuevoProducto) {
		//creamos la lista de productos en los servicios pedidos y productos
		this.servicioProductos.meterProducto(nuevoProducto);
		this.servicioPedido.meterPedidos(nuevoProducto);
		if(servicioPedido.getPrecioTotal()==0) {
			return "redirect:/login/select/EditarProducto/"+servicioUsuario.getId();
		}else {
			return "redirect:/login/select/EditarProducto/EditarEnvio";
		}
	}
	
	@GetMapping("login/select/EditarProducto/EditarEnvio")
	public String editarEnvio(Model model) {
		model.addAttribute("usuario", sesion.getAttribute("usuario1"));
		model.addAttribute("datosPedido", servicioProductos.getListaCantidades());
		return "/envioEdit";
	}
	
	@PostMapping("login/select/EditarProducto/EditarEnvio/submit")
	public String editarEnvioSubmit(Model model, @ModelAttribute("nuevaDireccion") Pedidos pedidoNuevo,
			@RequestParam(name="direccion") String direccion,
			@RequestParam(name="email") String email,
			@RequestParam(name="telefono") String telefono
			) {
		if(direccion=="" || telefono=="" || email=="") {
			return "redirect:login/select/NuevoP/envio";
		}else {
			//actualizamos el pedido que tenemos guardado en el servicio
			servicioPedido.setUltimoPedido(pedidoNuevo);
			pedidoNuevo.setCorreoElectronico(email);
			pedidoNuevo.setDireccion(direccion);
			pedidoNuevo.setTelefono(telefono);
			//guardamos ep pedido dentro del usuario
			servicioPedido.editarPedido(pedidoNuevo,(Usuario) sesion.getAttribute("usuario1"));
			return "redirect:/login/select/NuevoP/envio/resumenEditado";
		}
	}
	
	@GetMapping("login/select/NuevoP/envio/resumenEditado")
	public String resumenEdit(Model model) {
		model.addAttribute("usuario", sesion.getAttribute("usuario1"));
		model.addAttribute("datosPedido", this.servicioProductos.getListaCantidades());
		model.addAttribute("direccionPedido", servicioPedido.mostrarUltimoPedido());	
		model.addAttribute("precioFinal", servicioPedido.getPrecioTotal());
		model.addAttribute("precioFinalConIVA", servicioPedido.getPrecioTotaliva());
		return "/resumenEnvio";
	}
	
	@GetMapping("cerrarSesion")
	public String cerrarSesion() {
		sesion.invalidate();
		return "redirect:/login";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
