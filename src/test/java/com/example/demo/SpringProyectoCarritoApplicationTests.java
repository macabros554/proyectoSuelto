package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.model.Usuario;

@SpringBootTest
class SpringProyectoCarritoApplicationTests {

	@Test
	void contextLoads() {
	}
	
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
