package com.contacloud.pdfacturacion.controller;

import com.contacloud.pdfacturacion.dto.FacturaDTO;
import com.contacloud.pdfacturacion.dto.PagoDTO;
import com.contacloud.pdfacturacion.service.FacturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facturas")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;

    @PostMapping
    public ResponseEntity<FacturaDTO> crearFactura(@RequestBody FacturaDTO facturaDTO) {
        FacturaDTO creada = facturaService.crearFactura(facturaDTO);
        return ResponseEntity.ok(creada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaDTO> obtenerFactura(@PathVariable Long id) {
        FacturaDTO factura = facturaService.obtenerFactura(id);
        return ResponseEntity.ok(factura);
    }

    @GetMapping
    public ResponseEntity<List<FacturaDTO>> listarFacturas() {
        List<FacturaDTO> facturas = facturaService.listarFacturas();
        return ResponseEntity.ok(facturas);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<FacturaDTO>> listarFacturasPorCliente(@PathVariable Long clienteId) {
        List<FacturaDTO> facturas = facturaService.listarFacturasPorCliente(clienteId);
        return ResponseEntity.ok(facturas);
    }

    @PostMapping("/pagos")
    public ResponseEntity<PagoDTO> registrarPago(@RequestBody PagoDTO pagoDTO) {
        PagoDTO pagoRegistrado = facturaService.registrarPago(pagoDTO);
        return ResponseEntity.ok(pagoRegistrado);
    }
}
