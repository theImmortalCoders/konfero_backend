package pl.immortal.konfero_backend.infrastructure.file.dto;

import lombok.Data;
import pl.immortal.konfero_backend.model.entity.File;

@Data
public class FileSingleResponse {
    private Long id;
    private String path;
    private String description;
    private Long authorId;
    private File.FileType fileType;
}
