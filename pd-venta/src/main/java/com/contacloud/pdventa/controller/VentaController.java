package com.contacloud.pdventa.controller;

import com.contacloud.pdventa.dto.PagoDTO;
import com.contacloud.pdventa.dto.VentaDTO;
import com.contacloud.pdventa.service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @PostMapping
    public ResponseEntity<VentaDTO> crearVenta(@RequestBody VentaDTO ventaDTO) {
        VentaDTO creada = ventaService.crearVenta(ventaDTO);
        return ResponseEntity.ok(creada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaDTO> obtenerVenta(@PathVariable Long id) {
        VentaDTO factura = ventaService.obtenerVenta(id);
        return ResponseEntity.ok(factura);
    }

    @GetMapping
    public ResponseEntity<List<VentaDTO>> listarVentas() {
        List<VentaDTO> facturas = ventaService.listarVentas();
        return ResponseEntity.ok(facturas);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VentaDTO>> listarVentasPorCliente(@PathVariable Long clienteId) {
        List<VentaDTO> facturas = ventaService.listarVentasPorCliente(clienteId);
        return ResponseEntity.ok(facturas);
    }

    @PostMapping("/pagos")
    public ResponseEntity<PagoDTO> registrarPago(@RequestBody PagoDTO pagoDTO) {
        PagoDTO pagoRegistrado = ventaService.registrarPago(pagoDTO);
        return ResponseEntity.ok(pagoRegistrado);
    }
    @GetMapping("/pagos")
    public ResponseEntity<List<PagoDTO>> listarPagos() {
        List<PagoDTO> pagos = ventaService.obtenerTodosLosPagosDTO();
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/pagadas")
    public ResponseEntity<List<VentaDTO>> listarVentasPagadas() {
        List<VentaDTO> ventasPagadas = ventaService.listarVentasPorEstado("PAGADO");
        return ResponseEntity.ok(ventasPagadas);
    }

}
