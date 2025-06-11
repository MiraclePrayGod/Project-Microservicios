package com.contacloud.dpfacturation.feign;

import com.contacloud.dpfacturation.dato.ProductoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "dp-inventario-service", path = "/productos")
public interface InventarioFeign {
    @GetMapping("/{id}")
    ProductoDTO obtenerProductoPorId(@PathVariable("id") Long id);
}
