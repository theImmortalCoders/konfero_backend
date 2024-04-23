package pl.immortal.konfero_backend.model.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Type;
import pl.immortal.konfero_backend.model.Location;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Conference {
	@Id
	@GeneratedValue
	private Long id;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "organizer_id")
	private User organizer;
	private String name;
	@Column(length = 200, columnDefinition = "text")
	private String description;
	@ManyToOne
	@JoinColumn(name = "image_id")
	private File logo;
	@ManyToMany
	private List<Tag> tags = new ArrayList<>();
	@Type(JsonBinaryType.class)
	@Column(columnDefinition = "jsonb")
	private Location location;
	@ManyToMany(fetch = FetchType.LAZY)
	@ToString.Exclude
	private List<User> participants = new ArrayList<>();
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@ToString.Exclude
	private List<Lecture> lectures = new ArrayList<>();
	private boolean canceled;
	private Integer participantsLimit;
	@Enumerated(EnumType.STRING)
	private Format format = Format.STATIONARY;
	@ManyToMany
	private List<File> photos;
	private boolean verified;
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "conference")
	private List<Comment> comments;
	private boolean participantsFull;

	public enum Format {
		STATIONARY, ONLINE
	}
}
