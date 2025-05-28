package com.contacloud.pdfacturacion.service.impl;

import com.contacloud.pdfacturacion.dto.ClienteDTO;
import com.contacloud.pdfacturacion.dto.DetalleFacturaDTO;
import com.contacloud.pdfacturacion.dto.FacturaDTO;
import com.contacloud.pdfacturacion.dto.PagoDTO;
import com.contacloud.pdfacturacion.dto.ProductoDTO;
import com.contacloud.pdfacturacion.entity.DetalleFactura;
import com.contacloud.pdfacturacion.entity.Factura;
import com.contacloud.pdfacturacion.entity.Pago;
import com.contacloud.pdfacturacion.feignclient.ClienteFeing;
import com.contacloud.pdfacturacion.feignclient.ProductoFeing;
import com.contacloud.pdfacturacion.repository.DetalleFacturaRepository;
import com.contacloud.pdfacturacion.repository.FacturaRepository;
import com.contacloud.pdfacturacion.repository.PagoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacturaServiceImpl implements com.contacloud.pdfacturacion.service.FacturaService {

    private final FacturaRepository facturaRepository;
    private final DetalleFacturaRepository detalleFacturaRepository;
    private final PagoRepository pagoRepository;

    private final ClienteFeing clienteClient;
    private final ProductoFeing productoClient;

    @Override
    @Transactional
    public FacturaDTO crearFactura(FacturaDTO dto) {
        ClienteDTO cliente;
        try {
            cliente = clienteClient.obtenerClientePorId(dto.getClienteId());
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener el cliente (ID: " + dto.getClienteId() + "): " + e.getMessage());
        }

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente con ID " + dto.getClienteId() + " no existe.");
        }

        Factura factura = new Factura();
        factura.setClienteId(dto.getClienteId());
        factura.setFechaEmision(LocalDateTime.now());
        factura.setEstado("PENDIENTE");
        factura = facturaRepository.save(factura);

        BigDecimal totalFactura = BigDecimal.ZERO;

        // Validar stock pero NO reducirlo aquí
        for (DetalleFacturaDTO detalleDTO : dto.getDetalles()) {
            try {
                ProductoDTO producto = productoClient.obtenerProductoPorId(detalleDTO.getProductoId());

                if (producto == null) {
                    throw new IllegalArgumentException("Producto con ID " + detalleDTO.getProductoId() + " no existe.");
                }

                if (producto.getStock() < detalleDTO.getCantidad()) {
                    throw new IllegalArgumentException("Stock insuficiente para el producto ID " + detalleDTO.getProductoId());
                }

                BigDecimal subtotal = detalleDTO.getPrecioUnitario().multiply(BigDecimal.valueOf(detalleDTO.getCantidad()));
                totalFactura = totalFactura.add(subtotal);

                DetalleFactura detalle = new DetalleFactura();
                detalle.setFactura(factura);
                detalle.setProductoId(detalleDTO.getProductoId());
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
                detalle.setSubtotal(subtotal);
                detalleFacturaRepository.save(detalle);

            } catch (Exception e) {
                throw new RuntimeException("Error procesando el producto ID " + detalleDTO.getProductoId() + ": " + e.getMessage());
            }
        }

        factura.setTotal(totalFactura);
        facturaRepository.save(factura);

        dto.setId(factura.getId());
        dto.setFechaEmision(factura.getFechaEmision());
        dto.setTotal(totalFactura);
        dto.setEstado(factura.getEstado());

        return dto;
    }

    @Override
    public FacturaDTO obtenerFactura(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Factura con ID " + id + " no existe."));

        List<DetalleFactura> detalles = detalleFacturaRepository.findByFacturaId(id);

        FacturaDTO dto = new FacturaDTO();
        dto.setId(factura.getId());
        dto.setClienteId(factura.getClienteId());
        dto.setFechaEmision(factura.getFechaEmision());
        dto.setTotal(factura.getTotal());
        dto.setEstado(factura.getEstado());

        List<DetalleFacturaDTO> detallesDTO = detalles.stream().map(d -> {
            DetalleFacturaDTO detalle = new DetalleFacturaDTO();
            detalle.setProductoId(d.getProductoId());
            detalle.setCantidad(d.getCantidad());
            detalle.setPrecioUnitario(d.getPrecioUnitario());
            return detalle;
        }).collect(Collectors.toList());

        dto.setDetalles(detallesDTO);
        return dto;
    }

    @Override
    public List<FacturaDTO> listarFacturas() {
        return facturaRepository.findAll().stream()
                .map(this::convertirFacturaADTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FacturaDTO> listarFacturasPorCliente(Long clienteId) {
        return facturaRepository.findByClienteId(clienteId).stream()
                .map(this::convertirFacturaADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PagoDTO registrarPago(PagoDTO dto) {
        Factura factura = facturaRepository.findById(dto.getFacturaId())
                .orElseThrow(() -> new IllegalArgumentException("Factura con ID " + dto.getFacturaId() + " no existe."));

        if ("PAGADO".equalsIgnoreCase(factura.getEstado())) {
            throw new IllegalArgumentException("La factura ya fue pagada.");
        }

        // Obtener detalles para validar y descontar stock
        List<DetalleFactura> detalles = detalleFacturaRepository.findByFacturaId(factura.getId());

        // Validar stock para cada producto
        for (DetalleFactura detalle : detalles) {
            ProductoDTO producto = productoClient.obtenerProductoPorId(detalle.getProductoId());

            if (producto == null) {
                throw new IllegalArgumentException("Producto con ID " + detalle.getProductoId() + " no encontrado.");
            }

            if (producto.getStock() < detalle.getCantidad()) {
                throw new IllegalArgumentException("Stock insuficiente para producto ID " + detalle.getProductoId());
            }
        }

        // Reducir stock de cada producto
        for (DetalleFactura detalle : detalles) {
            productoClient.reducirStock(detalle.getProductoId(), detalle.getCantidad());
        }

        // Registrar pago
        Pago pago = new Pago();
        pago.setFactura(factura);
        pago.setFechaPago(LocalDateTime.now());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setMonto(dto.getMonto());
        pagoRepository.save(pago);

        factura.setEstado("PAGADO");
        facturaRepository.save(factura);

        dto.setId(pago.getId());
        dto.setFechaPago(pago.getFechaPago());

        return dto;
    }

    private FacturaDTO convertirFacturaADTO(Factura factura) {
        List<DetalleFactura> detalles = detalleFacturaRepository.findByFacturaId(factura.getId());

        List<DetalleFacturaDTO> detallesDTO = detalles.stream().map(d -> {
            DetalleFacturaDTO detalle = new DetalleFacturaDTO();
            detalle.setProductoId(d.getProductoId());
            detalle.setCantidad(d.getCantidad());
            detalle.setPrecioUnitario(d.getPrecioUnitario());
            return detalle;
        }).collect(Collectors.toList());

        return new FacturaDTO(
                factura.getId(),
                factura.getClienteId(),
                factura.getFechaEmision(),
                factura.getTotal(),
                factura.getEstado(),
                detallesDTO
        );
    }
}
