package com.contacloud.pdfacturacion.repository;


import com.contacloud.pdfacturacion.entity.DetalleFactura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleFacturaRepository extends JpaRepository<DetalleFactura, Long> {
    List<DetalleFactura> findByFacturaId(Long facturaId);
}
