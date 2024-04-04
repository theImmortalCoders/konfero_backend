package pl.immortal.konfero_backend.infrastructure.auth.dto.response;

import lombok.Data;

@Data
public class UserShortResponse {
    private Long id;
    private String username;
    private String email;
    private String photo;
}
