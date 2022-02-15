package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Productos;

/*
 * Le decimos de quien es el repositorio y de que timo es la PK en este caso Long
 */

@Repository
public interface ProductosRepository extends JpaRepository<Productos, Long>{

}