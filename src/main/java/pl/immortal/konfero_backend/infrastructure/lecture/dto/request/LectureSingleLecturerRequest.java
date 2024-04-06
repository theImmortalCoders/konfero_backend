package pl.immortal.konfero_backend.infrastructure.lecture.dto.request;

import lombok.Data;

@Data
public class LectureSingleLecturerRequest {
    private String description;
    private Long imageId;
}
