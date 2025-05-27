package com.contacloud.pdinventario.service.impl;

import com.contacloud.pdinventario.model.MovimientoStock;
import com.contacloud.pdinventario.model.Producto;
import com.contacloud.pdinventario.repository.MovimientoStockRepository;
import com.contacloud.pdinventario.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovimientoStockService implements ProductoService {

    @Autowired
    private MovimientoStockRepository movimientoRepo;
    private ProductoService productoService;
    public MovimientoStock registrarMovimiento(MovimientoStock movimiento) {
        return movimientoRepo.save(movimiento);
    }

    public List<MovimientoStock> listarPorProducto(Long productoId) {
        return movimientoRepo.findByProductoId(productoId);
    }

    @Override
    public List<Producto> listar() {
        return productoRepository.listar();
    }

    @Override
    public Optional<Producto> obtenerPorId(Long id) {
        return productoService.obtenerPorId(id);
    }

    @Override
    public Producto guardar(Producto producto) {
        return productoService.guardar(producto);
    }

    @Override
    public Producto actualizarStock(Long id, int cantidad) {
        Producto producto = prod.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setStock(producto.getStock() + cantidad);
        return productoRepository.save(producto);
        return null;
    }

    @Override
    public void eliminar(Long id) {

    }
}
