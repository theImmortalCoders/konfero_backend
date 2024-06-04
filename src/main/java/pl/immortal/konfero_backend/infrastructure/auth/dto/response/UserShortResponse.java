package pl.immortal.konfero_backend.infrastructure.auth.dto.response;

import lombok.Data;
import pl.immortal.konfero_backend.model.Role;

@Data
public class UserShortResponse {
	private Long id;
	private String username;
	private String email;
	private String photo;
	private boolean verified;
	private Role role;
}
