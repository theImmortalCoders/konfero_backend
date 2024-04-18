package pl.immortal.konfero_backend.infrastructure.file.dto;

import lombok.Data;
import pl.immortal.konfero_backend.infrastructure.auth.dto.response.UserShortResponse;
import pl.immortal.konfero_backend.model.entity.File;

import java.time.LocalDateTime;

@Data
public class FileSingleResponse {
	private Long id;
	private String path;
	private String description;
	private UserShortResponse author;
	private File.FileType fileType;
	private LocalDateTime createdDate;
}
