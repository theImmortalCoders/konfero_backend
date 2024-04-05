package pl.immortal.konfero_backend.infrastructure.conference.dto.response;

import lombok.Data;
import pl.immortal.konfero_backend.infrastructure.auth.dto.response.UserShortResponse;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.response.LectureShortResponse;
import pl.immortal.konfero_backend.model.Location;
import pl.immortal.konfero_backend.model.entity.Conference;
import pl.immortal.konfero_backend.model.entity.Image;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ConferenceSingleResponse {
    private Long id;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private UserShortResponse organizer;
    private String name;
    private String description;
    private Image image;
    private List<Conference.Tag> tags;
    private Location location;
    private List<UserShortResponse> participants;
    private List<LectureShortResponse> lectures;
    private boolean finished;
}
