package grupo2.docubot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DocubotApplication {
	public static void main(String[] args) {
		SpringApplication.run(DocubotApplication.class, args);
	}
}
