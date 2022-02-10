package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="usuario")
public class Usuario {
	
	/*
	 * Le decioms que la id es el nickname 
	 */
	
	@Id
	private String nickName;
	private String nombre;
	private String contrasenia;
	private String correoElectronico;
	private String direccion;
	private String telefono;
	/*
	 * en la relacion OneToMany usamos el fetch de tipipo eager porque cargará todos los datos de las entidades que sean necesarias 
	 * esto incluye a los hijos
	 */
	@OneToMany(fetch = FetchType.EAGER)
	private List<Pedidos> listaPedidos = new ArrayList<>();
	
	public Usuario() {}

	public Usuario(String nickName, String nombre, String contrasenia, String correoElectronico, String direccion,
			String telefono) {
		super();

		this.nickName = nickName;
		this.nombre = nombre;
		this.contrasenia = contrasenia;
		this.correoElectronico = correoElectronico;
		this.direccion = direccion;
		this.telefono = telefono;
	}


	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public List<Pedidos> getListaPedidos() {
		return listaPedidos;
	}

	public void setListaPedidos(List<Pedidos> listaPedidos) {
		this.listaPedidos = listaPedidos;
	}
	
	/*
	 * generamos hashCode, equals y toString
	 */

	@Override
	public int hashCode() {
		return Objects.hash(nickName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(nickName, other.nickName);
	}

	@Override
	public String toString() {
		return "Usuario [nickName=" + nickName + ", nombre=" + nombre + ", contrasenia=" + contrasenia
				+ ", correoElectronico=" + correoElectronico + ", direccion=" + direccion + ", telefono=" + telefono + "]";
	}

	
	
	
	
}
