package com.contacloud.dpinventario.controller;

import com.contacloud.pdinventario.model.Producto;
import com.contacloud.pdinventario.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> listarProductos() {
        return productoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.guardar(producto));
    }

    @PutMapping("/productos/{id}/reducir-stock")
    public ResponseEntity<Void> reducirStock(@PathVariable Long id,@RequestParam Integer cantidad) {
        productoService.reducirStock(id, cantidad);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<String> actualizarStock(@PathVariable Long id, @RequestBody Map<String, Integer> request) {
        Integer cantidad = request.get("cantidad");
        if (cantidad == null) {
            return ResponseEntity.badRequest().body("Falta el campo 'cantidad' en el cuerpo.");
        }
        productoService.actualizarStock(id, cantidad);
        return ResponseEntity.ok("Stock actualizado correctamente.");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
