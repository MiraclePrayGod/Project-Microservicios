package com.contacloud.pdfacturacion.service;


import com.contacloud.pdfacturacion.dto.FacturaDTO;
import com.contacloud.pdfacturacion.dto.PagoDTO;

import java.util.List;

public interface FacturaService {
    FacturaDTO crearFactura(FacturaDTO facturaDTO);
    FacturaDTO obtenerFactura(Long id);
    List<FacturaDTO> listarFacturas();
    List<FacturaDTO> listarFacturasPorCliente(Long clienteId);

    PagoDTO registrarPago(PagoDTO dto);

}
