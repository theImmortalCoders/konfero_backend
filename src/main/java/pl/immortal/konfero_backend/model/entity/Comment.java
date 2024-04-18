package pl.immortal.konfero_backend.model.entity;

import jakarta.persistence.*;
import lombok.Data;

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
	private User author;
	@ManyToMany
	private List<Comment> responses = new ArrayList<>();
}
