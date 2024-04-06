package pl.immortal.konfero_backend.infrastructure.lecture.dto.response;

import lombok.Data;
import pl.immortal.konfero_backend.infrastructure.image.dto.ImageSingleResponse;

import java.time.LocalDateTime;

@Data
public class LectureShortResponse {
    private Long id;
    private String name;
    private LocalDateTime startDateTime;
    private int durationMinutes;
    private ImageSingleResponse image;
    private String place;
}
