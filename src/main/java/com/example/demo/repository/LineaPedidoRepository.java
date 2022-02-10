package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.LineaPedido;

/*
 * Le decimos de quien es el repositorio y de que timo es la PK en este caso Long
 */

@Repository
public interface LineaPedidoRepository extends JpaRepository<LineaPedido, Long>{
	
}