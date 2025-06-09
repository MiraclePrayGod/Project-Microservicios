package org.example.msfacturacion.repository;


import org.example.msfacturacion.entity.FacturaVenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacturaVentaRepository extends JpaRepository<FacturaVenta, Long> {
    /**
     * Verifica si una venta ya está asociada a alguna factura.
     */
    boolean existsByVentaId(Long ventaId);

    /**
     * Devuelve los registros FacturaVenta que pertenezcan a la lista de IDs de venta indicada.
     */
    List<FacturaVenta> findByVentaIdIn(List<Long> ventaIds);
}