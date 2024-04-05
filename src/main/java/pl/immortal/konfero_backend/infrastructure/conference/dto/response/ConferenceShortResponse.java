package pl.immortal.konfero_backend.infrastructure.conference.dto.response;

import lombok.Data;
import pl.immortal.konfero_backend.infrastructure.auth.dto.response.UserShortResponse;
import pl.immortal.konfero_backend.model.Location;
import pl.immortal.konfero_backend.model.entity.Image;

import java.time.LocalDateTime;

@Data
public class ConferenceShortResponse {
    private Long id;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private UserShortResponse organizer;
    private String name;
    private Image image;
    private Location location;
    private boolean finished;
}
