package com.contacloud.pdventa.feignclient;


import com.contacloud.pdventa.dto.ClienteDTO;
import com.contacloud.pdventa.feignclient.fallback.ClienteClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pd-cliente", path = "/clientes", fallbackFactory = ClienteClientFallbackFactory.class)
public interface ClienteFeing {

    @GetMapping("/{id}")
    ResponseEntity<ClienteDTO>  obtenerClientePorId(@PathVariable Integer id);

}