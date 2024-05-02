package pl.immortal.konfero_backend.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Lecture {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	@Column(length = 200, columnDefinition = "text")
	private String description;
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime startDateTime;
	private int durationMinutes;
	private String place;
	@ManyToOne
	@JoinColumn(name = "image_id")
	private File image;
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "conference_id")
	private Conference conference;
	@ManyToMany
	@ToString.Exclude
	private List<User> lecturers = new ArrayList<>();
	@ManyToMany
	@ToString.Exclude
	private List<User> interested = new ArrayList<>();
	@ManyToMany
	private List<File> materials = new ArrayList<>();
}
