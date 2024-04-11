package pl.immortal.konfero_backend.infrastructure.file.dto;

import lombok.Data;

@Data
public class FileSingleResponse {
    private Long id;
    private String path;
    private boolean hasThumbnail;
    private Long authorId;
}
