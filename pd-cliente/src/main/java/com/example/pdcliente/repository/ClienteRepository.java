package com.example.pdcliente.repository;

import com.example.pdcliente.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    boolean existsByRucDni(String RucDni);

}
