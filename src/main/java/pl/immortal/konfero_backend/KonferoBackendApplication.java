package pl.immortal.konfero_backend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.TimeZone;

@EnableAsync
@SpringBootApplication(
		exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class}
)
@OpenAPIDefinition(info = @Info(title = "Konfero backend app documentation", description = "Authors: Marcin Bator"))
public class KonferoBackendApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Warsaw"));
		SpringApplication.run(KonferoBackendApplication.class, args);
	}

}
