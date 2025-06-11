package com.contacloud.dpfacturation.repository;


import com.contacloud.dpfacturation.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
    List<Factura> findByClienteId(Long clienteId);
}