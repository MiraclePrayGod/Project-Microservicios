package com.contacloud.pdventa.feignclient.fallback;


import com.contacloud.pdventa.feignclient.ClienteFeing;
import com.contacloud.pdventa.dto.ClienteDTO;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ClienteClientFallbackFactory implements FallbackFactory<ClienteFeing> {
    @Override
    public ClienteFeing create(Throwable cause) {
        return new ClienteFeing() {
            @Override
            public ClienteDTO obtenerClientePorId(Long id) {
                throw new RuntimeException("No se pudo contactar con el servicio de clientes. Detalle: " + cause.getMessage());
            }
        };
    }
}
