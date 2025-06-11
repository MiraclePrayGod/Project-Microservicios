package com.contacloud.dpfacturation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class DpFacturationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DpFacturationApplication.class, args);
    }

}
