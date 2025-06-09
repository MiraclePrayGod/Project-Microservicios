package org.example.msfacturacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.example.msfacturacion.feign")
public class MsFacturacionApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsFacturacionApplication.class, args);
    }
}