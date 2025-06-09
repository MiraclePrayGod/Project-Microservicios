package org.example.dmbauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class 	DmbAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(DmbAuthApplication.class, args);
	}

}
