package com.contacloud.pdinventario.repository;

import com.contacloud.pdinventario.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Métodos personalizados si los necesitas más adelante
}
