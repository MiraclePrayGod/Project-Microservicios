package com.contacloud.pdfacturacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PdFacturacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdFacturacionApplication.class, args);
	}

}
