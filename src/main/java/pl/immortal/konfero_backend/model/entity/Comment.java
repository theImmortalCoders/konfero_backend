package pl.immortal.konfero_backend.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Comment {
	@Id
	@GeneratedValue
	private Long id;
	private String content;
	private LocalDateTime createdAt;
	@ManyToOne
	@JoinColumn(name = "conference_id")
	private Conference conference;
	@ManyToOne
	private User author;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@ToString.Exclude
	private List<Comment> responses = new ArrayList<>();
	@ManyToOne
	@ToString.Exclude
	private Comment respondTo;
}
