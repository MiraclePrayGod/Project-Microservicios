package com.contacloud.dpfacturation.service;


import com.contacloud.dpfacturation.dato.FacturaDTO;
import com.contacloud.dpfacturation.dato.VentaDTO;
import com.contacloud.dpfacturation.entity.Factura;

import java.util.List;

public interface FacturaService {
    List<Factura> listar();
    List<Factura> listarPorCliente(Long clienteId);
    List<VentaDTO> ventasPorFacturar();
    Factura generarFactura(List<Long> ventaIds);
    FacturaDTO convertirAFacturaDTO(Factura factura);

}