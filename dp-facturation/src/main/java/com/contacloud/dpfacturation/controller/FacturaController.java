package com.contacloud.dpfacturation.controller;

import lombok.RequiredArgsConstructor;
import com.contacloud.dpfacturation.dato.FacturaDTO;
import com.contacloud.dpfacturation.dato.VentaDTO;
import com.contacloud.dpfacturation.entity.Factura;
import com.contacloud.dpfacturation.service.FacturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facturas")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService service;

    /** Lista facturas con filtro opcional por cliente */
    @GetMapping
    public ResponseEntity<List<FacturaDTO>> listar(@RequestParam(required = false) Long clienteId) {
        List<Factura> facturas = clienteId == null ? service.listar() : service.listarPorCliente(clienteId);
        List<FacturaDTO> response = facturas.stream()
                .map(service::convertirAFacturaDTO)
                .toList();
        return ResponseEntity.ok(response);
    }

    /** Ventas pagadas que aún no tienen factura */
    @GetMapping("/ventas-por-facturar")
    public ResponseEntity<List<VentaDTO>> ventasPorFacturar() {
        return ResponseEntity.ok(service.ventasPorFacturar());
    }

    /** Recibe lista de IDs de ventas y genera factura */
    @PostMapping
    public ResponseEntity<FacturaDTO> generar(@RequestBody List<Long> ventaIds) {
        Factura factura = service.generarFactura(ventaIds);
        FacturaDTO dto = service.convertirAFacturaDTO(factura);
        return ResponseEntity.ok(dto);
    }
}
