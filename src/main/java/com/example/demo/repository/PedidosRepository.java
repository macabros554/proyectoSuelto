package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Pedidos;

/*
 * Le decimos de quien es el repositorio y de que timo es la PK en este caso Long
 */

@Repository
public interface PedidosRepository extends JpaRepository<Pedidos, Long>{

}