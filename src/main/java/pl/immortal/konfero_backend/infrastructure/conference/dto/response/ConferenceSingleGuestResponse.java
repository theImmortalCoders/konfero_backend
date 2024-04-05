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
public class ConferenceSingleGuestResponse {
    private Long id;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private UserShortResponse organizer;
    private String name;
    private String description;
    private Image logo;
    private List<Conference.Tag> tags;
    private Location location;
    private List<LectureShortResponse> lectures;
    private boolean finished;
    private boolean canceled;
    private int participantsLimit;
    private Conference.Format format;
    private List<Image> photos;
}
