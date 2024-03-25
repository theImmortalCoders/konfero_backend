package pl.immortal.konfero_backend.infrastructure.auth.dto;

import lombok.Data;

@Data
public class ProfileUpdateSingleRequest {
    private String phone;
    private String city;
}
