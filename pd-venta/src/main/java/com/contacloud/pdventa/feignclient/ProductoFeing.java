package com.contacloud.pdventa.feignclient;


import com.contacloud.pdventa.dto.ProductoDTO;
import com.contacloud.pdventa.feignclient.fallback.ProductoClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "pd-inventario", path = "/productos", fallbackFactory = ProductoClientFallbackFactory.class)
public interface ProductoFeing {

    @GetMapping("/{id}")
    ProductoDTO obtenerProductoPorId(@PathVariable("id") Long id);

    @PutMapping("/productos/{id}/reducir-stock")
    void reducirStock(@PathVariable("id") Long id, @RequestParam("cantidad") Integer cantidad);
}
