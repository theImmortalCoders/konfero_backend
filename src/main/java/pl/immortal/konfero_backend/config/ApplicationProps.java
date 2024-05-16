package pl.immortal.konfero_backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ConfigurationProperties(prefix = "konfero.security")
public class ApplicationProps {
	private List<String> allowedOrigins;
	private String loginUri;
	private String redirectUri;
	private String logoutRedirectUri;
	private String logoutUri;
}