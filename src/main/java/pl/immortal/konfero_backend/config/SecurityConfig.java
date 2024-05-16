package pl.immortal.konfero_backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.immortal.konfero_backend.infrastructure.auth.OidcAuthService;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@OpenAPIDefinition(info = @Info(title = "Konfero backend API documentation"))
@AllArgsConstructor
public class SecurityConfig {

	private final OidcAuthService oidcAuthService;
	private final ApplicationProps applicationProps;

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(@NonNull CorsRegistry registry) {
				registry
						.addMapping("/**")
						.allowedOrigins(
								applicationProps.getAllowedOrigins().toArray(new String[0]))
						.allowedHeaders("*")
						.allowCredentials(true)
						.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
			}
		};
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.cors(Customizer.withDefaults())
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
				.exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
				.authorizeHttpRequests(request -> request
						.anyRequest()
						.permitAll())
				.oauth2Login(login -> login
						.userInfoEndpoint(uie -> uie.oidcUserService(oidcAuthService))
						.authorizationEndpoint(endpoint -> endpoint.baseUri(applicationProps.getLoginUri()))
						.successHandler((request, response, authentication) -> {
							RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
							String redirectUrl = Arrays.stream(request.getCookies())
									.filter(cookie -> cookie.getName().equals("redirectUrl"))
									.findFirst()
									.map(Cookie::getValue)
									.orElse(applicationProps.getRedirectUri());
							redirectStrategy.sendRedirect(request, response, redirectUrl);
							System.out.println(redirectUrl);
						})
				)
				.logout(logout -> logout
						.logoutUrl(applicationProps.getLogoutUri())
						.logoutSuccessUrl(applicationProps.getLogoutRedirectUri())
						.invalidateHttpSession(false)
						.deleteCookies("JSESSIONID")
				);
		return http.build();
	}
}