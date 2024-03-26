package pl.immortal.konfero_backend.infrastructure.auth.dto;

import lombok.Data;

@Data
public class OrganizerSingleBecomeRequest {
    private String companyName;
    private String address;
    private String city;
    private String phone;
}
