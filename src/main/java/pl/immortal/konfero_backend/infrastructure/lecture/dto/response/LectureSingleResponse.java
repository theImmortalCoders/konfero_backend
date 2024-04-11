package pl.immortal.konfero_backend.infrastructure.lecture.dto.response;

import lombok.Data;
import pl.immortal.konfero_backend.infrastructure.auth.dto.response.UserShortResponse;
import pl.immortal.konfero_backend.infrastructure.file.dto.FileSingleResponse;
import pl.immortal.konfero_backend.model.entity.File;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LectureSingleResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDateTime;
    private int durationMinutes;
    private FileSingleResponse image;
    private Long conferenceId;
    private List<UserShortResponse> lecturers;
    private List<File> materials;
    private List<UserShortResponse> interested;
    private String place;
}
