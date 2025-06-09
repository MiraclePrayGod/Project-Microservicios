package com.contacloud.pdventa.service;


import com.contacloud.pdventa.dto.PagoDTO;
import com.contacloud.pdventa.dto.VentaDTO;

import java.util.List;

public interface VentaService {
    VentaDTO crearVenta(VentaDTO ventaDTO);
    VentaDTO obtenerVenta(Long id);
    List<VentaDTO> listarVentas();
    List<VentaDTO> listarVentasPorCliente(Long clienteId);
    PagoDTO registrarPago(PagoDTO dto);
    List<PagoDTO> obtenerTodosLosPagosDTO();
    List<VentaDTO> listarVentasPorEstado(String estado);

}
