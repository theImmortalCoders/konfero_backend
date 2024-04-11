package pl.immortal.konfero_backend.model.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Type;

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
    private String description;
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
    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private List<File> materials = new ArrayList<>();
}
