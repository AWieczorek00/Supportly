package supportly.supportlybackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class SupportlyBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupportlyBackendApplication.class, args);
	}

}
