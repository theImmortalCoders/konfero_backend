package pl.immortal.konfero_backend.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Tag {
	@Id
	@GeneratedValue
	private Long id;
	@Enumerated(EnumType.STRING)
	private Conference.TagName tagName;
}
