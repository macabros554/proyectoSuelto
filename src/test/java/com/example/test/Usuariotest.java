package com.example.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.demo.model.Usuario;

class Usuariotest {

	Usuario usuario;
	
	
	@BeforeEach
	void init() {
		usuario= new Usuario("ew", "ew", "ew", "ew", "ew", "ew");
	}
	
	@Test
	void test() {
		assertEquals("ew", usuario.getDireccion());
	}
	


}
