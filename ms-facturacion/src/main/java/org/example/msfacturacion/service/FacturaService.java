package org.example.msfacturacion.service;


import org.example.msfacturacion.dato.FacturaDTO;
import org.example.msfacturacion.entity.Factura;
import org.example.msfacturacion.dato.VentaDTO;

import java.util.List;

public interface FacturaService {
    List<Factura> listar();
    List<Factura> listarPorCliente(Long clienteId);
    List<VentaDTO> ventasPorFacturar();
    Factura generarFactura(List<Long> ventaIds);
    FacturaDTO convertirAFacturaDTO(Factura factura);

}