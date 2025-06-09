package com.contacloud.pdventa.repository;


import com.contacloud.pdventa.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    Optional<Pago> findById(Long id);
    List<Pago> findByVentaId(Long ventaId);

}
