package pl.immortal.konfero_backend.infrastructure.lecture.dto.response;

import lombok.Data;
import pl.immortal.konfero_backend.infrastructure.file.dto.FileSingleResponse;

import java.time.LocalDateTime;

@Data
public class LectureShortResponse {
    private Long id;
    private String name;
    private LocalDateTime startDateTime;
    private int durationMinutes;
    private FileSingleResponse image;
    private String place;
}
