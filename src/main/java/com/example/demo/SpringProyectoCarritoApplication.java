package com.example.demo;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.example.demo.model.Productos;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ProductosRepository;
import com.example.demo.repository.UsuarioRepository;

@SpringBootApplication
public class SpringProyectoCarritoApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(SpringProyectoCarritoApplication.class, args);
	}
	
	@Bean
	CommandLineRunner iniUsuarios(UsuarioRepository repoUsuario) {
		return (arg)-> {
			repoUsuario.saveAll(Arrays.asList(new Usuario("javi", "javiLira", "1234", "javilira@gmail.com", "C/ Guadalquivir", "123456789"),
					new Usuario("adela", "patata", "2021", "adelalira@gmail.com", "C/ Guadalquivir", "333333333")));
		};
	}
	@Bean
	CommandLineRunner iniProductos (ProductosRepository repoProductos) {
		return (arg)-> {
			repoProductos.saveAll(Arrays.asList(new Productos("Leche",1.20),new Productos("Cafe",1.80),
					new Productos("Manzana",0.32),new Productos("Lechuga",0.80),new Productos("Avena",2.10)));
		};
	}


}
