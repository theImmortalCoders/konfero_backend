package pl.immortal.konfero_backend.infrastructure.auth.dto.response;

import lombok.Data;
import pl.immortal.konfero_backend.model.entity.OrganizerRequest;

@Data
public class OrganizerRequestSingleResponse {
    private Long id;
    private UserSingleResponse user;
    private OrganizerRequest.OrganizerRequestStatus status;
}
