package com.contacloud.pdventa.service.impl;

import com.contacloud.pdventa.dto.ClienteDTO;
import com.contacloud.pdventa.dto.DetalleVentaDTO;
import com.contacloud.pdventa.dto.VentaDTO;
import com.contacloud.pdventa.dto.PagoDTO;
import com.contacloud.pdventa.dto.ProductoDTO;
import com.contacloud.pdventa.entity.DetalleVenta;
import com.contacloud.pdventa.entity.Venta;
import com.contacloud.pdventa.entity.Pago;
import com.contacloud.pdventa.feignclient.ClienteFeing;
import com.contacloud.pdventa.feignclient.ProductoFeing;
import com.contacloud.pdventa.repository.DetalleVentaRepository;
import com.contacloud.pdventa.repository.VentaRepository;
import com.contacloud.pdventa.repository.PagoRepository;
import com.contacloud.pdventa.service.VentaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final PagoRepository pagoRepository;

    private final ClienteFeing clienteClient;
    private final ProductoFeing productoClient;

    @Override
    @Transactional
    public VentaDTO crearVenta(VentaDTO dto) {
        ClienteDTO cliente;
        try {
            cliente = clienteClient.obtenerClientePorId(dto.getClienteId());
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener el cliente (ID: " + dto.getClienteId() + "): " + e.getMessage());
        }

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente con ID " + dto.getClienteId() + " no existe.");
        }

        Venta venta = new Venta();
        venta.setClienteId(dto.getClienteId());
        venta.setFechaEmision(LocalDateTime.now());
        venta.setEstado("PENDIENTE");
        venta = ventaRepository.save(venta);

        BigDecimal totalFactura = BigDecimal.ZERO;

        for (DetalleVentaDTO detalleDTO : dto.getDetalles()) {
            try {
                ProductoDTO producto = productoClient.obtenerProductoPorId(detalleDTO.getProductoId());

                if (producto == null) {
                    throw new IllegalArgumentException("Producto con ID " + detalleDTO.getProductoId() + " no existe.");
                }

                if (producto.getStock() < detalleDTO.getCantidad()) {
                    throw new IllegalArgumentException("Stock insuficiente para el producto ID " + detalleDTO.getProductoId());
                }

                BigDecimal precioUnitario = producto.getPrecioUnitario(); // ← tomamos el precio desde el microservicio de productos
                BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(detalleDTO.getCantidad()));
                totalFactura = totalFactura.add(subtotal);

                DetalleVenta detalle = new DetalleVenta();
                detalle.setVenta(venta);
                detalle.setProductoId(detalleDTO.getProductoId());
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setPrecioUnitario(precioUnitario); // ← usamos el precio correcto
                detalle.setSubtotal(subtotal);
                detalleVentaRepository.save(detalle);

            } catch (Exception e) {
                throw new RuntimeException("Error procesando el producto ID " + detalleDTO.getProductoId() + ": " + e.getMessage());
            }
        }

        venta.setTotal(totalFactura);
        ventaRepository.save(venta);

        dto.setId(venta.getId());
        dto.setFechaEmision(venta.getFechaEmision());
        dto.setTotal(totalFactura);
        dto.setEstado(venta.getEstado());


        return dto;

    }


    @Override
    public VentaDTO obtenerVenta(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venta con ID " + id + " no existe."));

        List<DetalleVenta> detalles = detalleVentaRepository.findByVentaId(id);

        VentaDTO dto = new VentaDTO();
        dto.setId(venta.getId());
        dto.setClienteId(venta.getClienteId());
        dto.setFechaEmision(venta.getFechaEmision());
        dto.setTotal(venta.getTotal());
        dto.setEstado(venta.getEstado());

        List<DetalleVentaDTO> detallesDTO = detalles.stream().map(d -> {
            DetalleVentaDTO detalle = new DetalleVentaDTO();
            detalle.setProductoId(d.getProductoId());
            detalle.setCantidad(d.getCantidad());
            detalle.setPrecioUnitario(d.getPrecioUnitario());

            try {
                ProductoDTO producto = productoClient.obtenerProductoPorId(d.getProductoId());
                detalle.setNombreProducto(producto.getNombre());
                detalle.setCategoria(producto.getCategoria());
                detalle.setUnidadMedida(producto.getUnidadMedida());
            } catch (Exception e) {
                detalle.setNombreProducto("Desconocido");
                detalle.setCategoria("Desconocida");
                detalle.setUnidadMedida("N/D");
            }

            return detalle;
        }).collect(Collectors.toList());

        dto.setDetalles(detallesDTO);
        return dto;
    }


    @Override
    public List<VentaDTO> listarVentas() {
        return ventaRepository.findAll().stream()
                .map(this::convertirVentaADTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VentaDTO> listarVentasPorCliente(Integer clienteId) {
        return ventaRepository.findByClienteId(clienteId).stream()
                .map(this::convertirVentaADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PagoDTO registrarPago(PagoDTO dto) {
        // 🟠 Buscar última venta pendiente del cliente
        Venta venta = ventaRepository
                .findTopByClienteIdAndEstadoOrderByFechaEmision(dto.getClienteId(), "PENDIENTE")
                .orElseThrow(() -> new IllegalArgumentException("No hay ventas pendientes para el cliente."));

        // 🟡 Verificar si ya existen pagos previos
        List<Pago> pagosAnteriores = pagoRepository.findByVentaId(venta.getId());
        boolean esPrimerPago = pagosAnteriores.isEmpty();

        // 🛑 Si ya está pagada, no permitir más pagos
        if ("PAGADO".equalsIgnoreCase(venta.getEstado())) {
            throw new IllegalArgumentException("La venta ya fue pagada.");
        }

        // ✅ Verificación de stock solo en el primer pago
        if (esPrimerPago) {
            List<DetalleVenta> detalles = detalleVentaRepository.findByVentaId(venta.getId());

            for (DetalleVenta detalle : detalles) {
                ProductoDTO producto = productoClient.obtenerProductoPorId(detalle.getProductoId());

                if (producto == null) {
                    throw new IllegalArgumentException("Producto ID " + detalle.getProductoId() + " no encontrado.");
                }

                if (producto.getStock() < detalle.getCantidad()) {
                    throw new IllegalArgumentException("Stock insuficiente para producto ID " + detalle.getProductoId());
                }
            }

            // 🔻 Reducir stock solo si es el primer pago
            for (DetalleVenta detalle : detalles) {
                productoClient.reducirStock(detalle.getProductoId(), detalle.getCantidad());
            }
        }

        // 💰 Registrar el pago
        Pago pago = new Pago();
        pago.setVenta(venta);
        pago.setFechaPago(LocalDateTime.now());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setMonto(dto.getMonto());
        pago.setReferencia(dto.getReferencia());
        pagoRepository.save(pago);

        // ➕ Calcular total pagado hasta ahora
        BigDecimal totalPagado = pagosAnteriores.stream()
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(dto.getMonto());

        // 🔁 Si el total pagado cubre o supera el total de la venta, actualizar estado
        if (totalPagado.compareTo(venta.getTotal()) >= 0) {
            venta.setEstado("PAGADO");
            ventaRepository.save(venta);
        }

        // 🔁 Retornar DTO actualizado
        dto.setId(pago.getId());
        dto.setFechaPago(pago.getFechaPago());

        return dto;
    }





    @Override
    public List<PagoDTO> obtenerTodosLosPagosDTO() {
        List<Pago> pagos = pagoRepository.findAll();
        return pagos.stream()
                .map(this::convertirAPagoDTO)
                .collect(Collectors.toList());
    }

    private PagoDTO convertirAPagoDTO(Pago pago) {
        PagoDTO dto = new PagoDTO();
        dto.setId(pago.getId());
        dto.setFacturaId(pago.getVenta().getId());
        dto.setFechaPago(pago.getFechaPago());
        dto.setMonto(pago.getMonto());
        dto.setMetodoPago(pago.getMetodoPago());
        dto.setReferencia(pago.getReferencia());
        dto.setClienteId(pago.getVenta().getClienteId());
        return dto;
    }




    private VentaDTO convertirVentaADTO(Venta venta) {
        List<DetalleVenta> detalles = detalleVentaRepository.findByVentaId(venta.getId());

        List<DetalleVentaDTO> detallesDTO = detalles.stream().map(d -> {
            DetalleVentaDTO detalle = new DetalleVentaDTO();
            detalle.setProductoId(d.getProductoId());
            detalle.setCantidad(d.getCantidad());
            detalle.setPrecioUnitario(d.getPrecioUnitario());

            try {
                ProductoDTO producto = productoClient.obtenerProductoPorId(d.getProductoId());
                detalle.setNombreProducto(producto.getNombre());
                detalle.setCategoria(producto.getCategoria());
                detalle.setUnidadMedida(producto.getUnidadMedida());
            } catch (Exception e) {
                detalle.setNombreProducto("Desconocido");
                detalle.setCategoria("Desconocida");
                detalle.setUnidadMedida("N/D");
            }

            return detalle;
        }).collect(Collectors.toList());

        return new VentaDTO(
                venta.getId(),
                venta.getClienteId(),
                venta.getFechaEmision(),
                venta.getTotal(),
                venta.getEstado(),
                detallesDTO
        );
    }

    @Override
    public List<VentaDTO> listarVentasPorEstado(String estado) {
        return ventaRepository.findByEstado(estado).stream()
                .map(this::convertirVentaADTO)
                .collect(Collectors.toList());
    }
}
