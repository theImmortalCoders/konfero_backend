package pl.immortal.konfero_backend.infrastructure.conference.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import pl.immortal.konfero_backend.model.Location;
import pl.immortal.konfero_backend.model.entity.Conference;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ConferenceSingleRequest {
	private LocalDateTime startDateTime;
	@Size(min = 3, max = 40)
	private String name;
	@Size(min = 10, max = 300)
	private String description;
	private Long logoId;
	private List<Long> tagsIds;
	private Location location;
	private Integer participantsLimit;
	@NotNull
	private Conference.Format format;
	private List<Long> photosIds;
}
