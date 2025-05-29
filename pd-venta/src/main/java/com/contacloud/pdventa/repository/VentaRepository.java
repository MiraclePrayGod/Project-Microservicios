package com.contacloud.pdventa.repository;


import com.contacloud.pdventa.entity.Venta;
import com.contacloud.pdventa.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByClienteId(Long clienteId);
}
