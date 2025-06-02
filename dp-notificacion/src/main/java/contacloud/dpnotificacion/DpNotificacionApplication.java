package contacloud.dpnotificacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class DpNotificacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(DpNotificacionApplication.class, args);
	}

}
