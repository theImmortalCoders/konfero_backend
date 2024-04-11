package pl.immortal.konfero_backend.infrastructure.conference.dto.response;

import lombok.Data;
import pl.immortal.konfero_backend.infrastructure.auth.dto.response.UserShortResponse;
import pl.immortal.konfero_backend.infrastructure.comment.dto.CommentSingleResponse;
import pl.immortal.konfero_backend.infrastructure.file.dto.FileSingleResponse;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.response.LectureShortResponse;
import pl.immortal.konfero_backend.model.Location;
import pl.immortal.konfero_backend.model.entity.Conference;
import pl.immortal.konfero_backend.model.entity.Tag;

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
    private FileSingleResponse logo;
    private List<Tag> tags;
    private Location location;
    private List<UserShortResponse> participants;
    private List<LectureShortResponse> lectures;
    private boolean finished;
    private boolean canceled;
    private int participantsLimit;
    private Conference.Format format;
    private List<FileSingleResponse> photos;
    private boolean verified;
    private List<CommentSingleResponse> comments;
    private boolean participantsFull;
}
