package pl.immortal.konfero_backend.infrastructure.lecture.dto.response;

import lombok.Data;
import pl.immortal.konfero_backend.infrastructure.auth.dto.response.UserShortResponse;
import pl.immortal.konfero_backend.model.Material;
import pl.immortal.konfero_backend.model.entity.Image;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LectureSingleResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDateTime;
    private int durationMinutes;
    private Image image;
    private Long conferenceId;
    private List<UserShortResponse> lecturers;
    private List<Material> materials;
}
