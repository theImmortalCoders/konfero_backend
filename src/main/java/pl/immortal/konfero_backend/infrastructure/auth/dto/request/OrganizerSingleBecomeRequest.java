package pl.immortal.konfero_backend.infrastructure.auth.dto.request;

import lombok.Data;

@Data
public class OrganizerSingleBecomeRequest {
	private String companyName;
	private String address;
	private String city;
	private String phone;
}
