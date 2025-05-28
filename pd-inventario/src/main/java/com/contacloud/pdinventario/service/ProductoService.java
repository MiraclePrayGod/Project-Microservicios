package com.contacloud.pdinventario.service;

import com.contacloud.pdinventario.model.Producto;
import com.contacloud.pdinventario.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto reducirStock(Long id, int cantidad) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setStock(producto.getStock() - cantidad);
        return productoRepository.save(producto);
    }

    public void actualizarStock(Long productoId, Integer cantidad) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + productoId));

        // Suma o resta stock, según lógica que uses (aquí sumamos cantidad)
        producto.setStock(producto.getStock() + cantidad);
        productoRepository.save(producto);
    }



    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }
}
