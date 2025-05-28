package com.contacloud.pdfacturacion.feignclient;


import com.contacloud.pdfacturacion.dto.ProductoDTO;
import com.contacloud.pdfacturacion.feignclient.fallback.ProductoClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "pd-inventario", path = "/productos", fallbackFactory = ProductoClientFallbackFactory.class)
public interface ProductoFeing {

    @GetMapping("/{id}")
    ProductoDTO obtenerProductoPorId(@PathVariable("id") Long id);

    @PutMapping("/productos/{id}/reducir-stock")
    void reducirStock(@PathVariable("id") Long id, @RequestParam("cantidad") Integer cantidad);
}
