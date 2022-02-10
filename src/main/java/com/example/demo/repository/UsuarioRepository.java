package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Usuario;

/*
 * Le decimos de quien es el repositorio y de que timo es la PK en este caso String
 */

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String>{
	
}