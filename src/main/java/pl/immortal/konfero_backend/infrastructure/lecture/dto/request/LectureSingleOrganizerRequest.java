package pl.immortal.konfero_backend.infrastructure.lecture.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LectureSingleOrganizerRequest {
	@Size(min = 3, max = 40)
	private String name;
	@Size(min = 10, max = 200)
	private String description;
	private LocalDateTime startDateTime;
	private int durationMinutes;
	private Long imageId;
	private List<Long> lecturersIds;
	private String place;
}
