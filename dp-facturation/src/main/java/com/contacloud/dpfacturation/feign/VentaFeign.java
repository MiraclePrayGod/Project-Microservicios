package com.contacloud.dpfacturation.feign;

import com.contacloud.dpfacturation.dato.VentaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "pd-venta", path = "/ventas")
public interface VentaFeign {

    @GetMapping("/{id}")
    VentaDTO obtenerVenta(@PathVariable("id") Long id);

    // Ventas pagadas (debe existir en ms‑venta)
    @GetMapping("/pagadas")
    List<VentaDTO> listarPagadas();
}