package com.contacloud.dpfacturation.service.impl;

import lombok.RequiredArgsConstructor;
import com.contacloud.dpfacturation.dato.*;
import com.contacloud.dpfacturation.entity.Factura;
import com.contacloud.dpfacturation.entity.FacturaVenta;
import com.contacloud.dpfacturation.feign.VentaFeign;
import com.contacloud.dpfacturation.repository.FacturaRepository;
import com.contacloud.dpfacturation.repository.FacturaVentaRepository;
import com.contacloud.dpfacturation.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private final FacturaRepository facturaRepo;
    @Autowired
    private final FacturaVentaRepository fvRepo;
    @Autowired
    private final VentaFeign ventaFeign;

    private final AtomicLong secuencial = new AtomicLong(1);

    /* ---------------- LISTAR ---------------- */
    @Override
    @Transactional(readOnly = true)
    public List<Factura> listar() {
        return facturaRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Factura> listarPorCliente(Long clienteId) {
        return facturaRepo.findByClienteId(clienteId);
    }

    /* ---------- CONVERSIÓN A DTO COMPLETO ---------- */
    public FacturaDTO convertirAFacturaDTO(Factura factura) {
        FacturaDTO dto = new FacturaDTO();
        dto.setId(factura.getId());
        dto.setClienteId(factura.getClienteId());
        dto.setFechaEmision(factura.getFechaEmision());
        dto.setNumero(factura.getNumero());
        dto.setEstado(factura.getEstado());
        dto.setTotal(factura.getTotal());

        List<FacturaVentaDetalleDTO> ventas = factura.getVentas().stream().map(fv -> {
            FacturaVentaDetalleDTO vdto = new FacturaVentaDetalleDTO();
            vdto.setId(fv.getId());
            vdto.setVentaId(fv.getVentaId());
            vdto.setFechaVenta(fv.getFechaVenta());
            vdto.setTotalVenta(fv.getTotalVenta());

            VentaDTO ventaCompleta = ventaFeign.obtenerVenta(fv.getVentaId());
            vdto.setDetalles(ventaCompleta.getDetalles());
            return vdto;
        }).collect(Collectors.toList());

        dto.setVentas(ventas);
        return dto;
    }

    /* ---------- VENTAS PAGADAS SIN FACTURA ---------- */
    @Override
    public List<VentaDTO> ventasPorFacturar() {
        List<VentaDTO> pagadas = ventaFeign.listarPagadas();
        return pagadas.stream()
                .filter(v -> !fvRepo.existsByVentaId(v.getId()))
                .collect(Collectors.toList());
    }

    /* ---------- GENERAR FACTURA ---------- */
    @Override
    @Transactional
    public Factura generarFactura(List<Long> ventaIds) {
        if (ventaIds == null || ventaIds.isEmpty()) {
            throw new IllegalArgumentException("Se requiere al menos una venta");
        }

        // Validar que no estén facturadas
        List<FacturaVenta> previas = fvRepo.findByVentaIdIn(ventaIds);
        if (!previas.isEmpty()) {
            throw new RuntimeException("Alguna venta ya fue facturada: " + previas.stream()
                    .map(FacturaVenta::getVentaId)
                    .collect(Collectors.toList()));
        }

        // Obtener ventas
        List<VentaDTO> ventas = ventaIds.stream()
                .map(ventaFeign::obtenerVenta)
                .collect(Collectors.toList());

        // Deben pertenecer al mismo cliente
        Long clienteId = ventas.get(0).getClienteId();
        if (ventas.stream().anyMatch(v -> !clienteId.equals(v.getClienteId()))) {
            throw new RuntimeException("Todas las ventas deben ser del mismo cliente");
        }

        // Construir factura
        Factura factura = new Factura();
        factura.setClienteId(clienteId);
        factura.setFechaEmision(LocalDateTime.now());
        factura.setNumero(String.format("F001-%08d", secuencial.getAndIncrement()));
        factura.setEstado("GENERADA");

        BigDecimal total = BigDecimal.ZERO;
        List<FacturaVenta> listaFV = new ArrayList<>();
        for (VentaDTO v : ventas) {
            FacturaVenta fv = new FacturaVenta();
            fv.setVentaId(v.getId());
            fv.setFechaVenta(v.getFechaEmision());
            fv.setTotalVenta(v.getTotal());
            fv.setFactura(factura);
            listaFV.add(fv);
            total = total.add(v.getTotal());
        }

        factura.setTotal(total);
        factura.setVentas(listaFV);

        return facturaRepo.save(factura);
    }
}