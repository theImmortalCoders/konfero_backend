package pl.immortal.konfero_backend.infrastructure.lecture.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LectureSingleOrganizerRequest {
    private String name;
    private String description;
    private LocalDateTime startDateTime;
    private int durationMinutes;
    private Long imageId;
    private List<Long> lecturersIds;
    private String place;
}
