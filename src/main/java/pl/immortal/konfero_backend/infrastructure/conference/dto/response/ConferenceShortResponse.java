package pl.immortal.konfero_backend.infrastructure.conference.dto.response;

import lombok.Data;
import pl.immortal.konfero_backend.infrastructure.auth.dto.response.UserShortResponse;
import pl.immortal.konfero_backend.infrastructure.file.dto.FileSingleResponse;
import pl.immortal.konfero_backend.model.Location;
import pl.immortal.konfero_backend.model.entity.Conference;

import java.time.LocalDateTime;

@Data
public class ConferenceShortResponse {
    private Long id;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private UserShortResponse organizer;
    private String name;
    private FileSingleResponse logo;
    private Location location;
    private boolean finished;
    private boolean canceled;
    private int participantsLimit;
    private Conference.Format format;
    private boolean verified;
    private boolean participantsFull;
}
