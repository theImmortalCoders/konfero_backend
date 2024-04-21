package pl.immortal.konfero_backend.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Tag {
	@Id
	@GeneratedValue
	private Long id;
	@Column(unique = true)
	private String tagName;
}
