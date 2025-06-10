package com.contacloud.pdventa.repository;


import com.contacloud.pdventa.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByClienteId(Integer clienteId);
    Optional<Venta> findTopByClienteIdAndEstadoOrderByFechaEmision(Integer clienteId, String estado);
    List<Venta> findByEstado(String estado);

}
