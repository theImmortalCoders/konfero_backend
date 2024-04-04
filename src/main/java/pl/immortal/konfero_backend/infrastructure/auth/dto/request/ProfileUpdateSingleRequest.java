package pl.immortal.konfero_backend.infrastructure.auth.dto.request;

import lombok.Data;

@Data
public class ProfileUpdateSingleRequest {
    private String phone;
    private String city;
}
