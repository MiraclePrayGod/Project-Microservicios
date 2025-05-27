package contacloud.dpregistryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DpRegistryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DpRegistryServiceApplication.class, args);
    }

}
