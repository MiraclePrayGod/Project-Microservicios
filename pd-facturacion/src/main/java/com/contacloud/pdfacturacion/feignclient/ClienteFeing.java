package com.contacloud.pdfacturacion.feignclient;


import com.contacloud.pdfacturacion.dto.ClienteDTO;
import com.contacloud.pdfacturacion.feignclient.fallback.ClienteClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pd-clientes", path = "/clientes", fallbackFactory = ClienteClientFallbackFactory.class)
public interface ClienteFeing {

    @GetMapping("/{id}")
    ClienteDTO obtenerClientePorId(@PathVariable("id") Long id);
}