package com.contacloud.pdventa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PdVentaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdVentaApplication.class, args);
	}

}
