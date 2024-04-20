package pl.immortal.konfero_backend.infrastructure.auth.dto.response;

import lombok.Data;
import pl.immortal.konfero_backend.infrastructure.conference.dto.response.ConferenceShortResponse;
import pl.immortal.konfero_backend.model.Role;

import java.util.List;

@Data
public class UserSingleResponse {
	private Long id;
	private String googleId;
	private String username;
	private Role role;
	private String email;
	private String photo;
	private boolean active;
	private String companyName;
	private String address;
	private String city;
	private String phone;
	private List<ConferenceShortResponse> conferencesOrganized;
	private List<ConferenceShortResponse> conferencesParticipated;
	private boolean verified;
}
