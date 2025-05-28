package com.contacloud.pdfacturacion.feignclient.fallback;


import com.contacloud.pdfacturacion.feignclient.ProductoFeing;
import com.contacloud.pdfacturacion.dto.ProductoDTO;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ProductoClientFallbackFactory implements FallbackFactory<ProductoFeing> {
    @Override
    public ProductoFeing create(Throwable cause) {
        return new ProductoFeing() {
            @Override
            public ProductoDTO obtenerProductoPorId(Long id) {
                throw new RuntimeException("No se pudo contactar con el servicio de inventario. Detalle: " + cause.getMessage());
            }

            @Override
            public void reducirStock(Long productoId, Integer cantidad) {
                throw new RuntimeException("No se pudo reducir el stock del producto. Detalle: " + cause.getMessage());
            }
        };
    }
}
