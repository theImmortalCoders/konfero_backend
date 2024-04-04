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
    private String description;
    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image image;
    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private List<Tag> tags = new ArrayList<>();
    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Location location;
    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<User> participants = new ArrayList<>();
    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Lecture> lectures = new ArrayList<>();

    public enum Tag {
        IT, AI, LIFESTYLE, HEALTH, FASHION, NUTRITION
    }
}
