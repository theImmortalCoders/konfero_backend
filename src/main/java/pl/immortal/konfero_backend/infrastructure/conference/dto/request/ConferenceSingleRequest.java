package pl.immortal.konfero_backend.infrastructure.conference.dto.request;

import lombok.Data;
import pl.immortal.konfero_backend.model.Location;
import pl.immortal.konfero_backend.model.entity.Conference;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ConferenceSingleRequest {
    private LocalDateTime startDateTime;
    private String name;
    private String description;
    private Long logoId;
    private List<Conference.Tag> tags;
    private Location location;
    private int participantsLimit;
    private Conference.Format format;
    private List<Long> photosIds;
}
