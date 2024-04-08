package pl.immortal.konfero_backend.infrastructure.lecture.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LectureSingleLecturerRequest {
    @Size(min = 10, max = 200)
    private String description;
    private Long imageId;
}
