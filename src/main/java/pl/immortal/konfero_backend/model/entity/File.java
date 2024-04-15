package pl.immortal.konfero_backend.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class File {
    @Id
    @GeneratedValue
    private Long id;
    private String path;
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "author_id")
    private User author;
    @Enumerated(EnumType.STRING)
    private FileType fileType = FileType.UNDEFINED;
    private String description;
    private LocalDateTime createdDate = LocalDateTime.now();

    public enum FileType {
        IMAGE, DOCUMENT, VIDEO, SOUND, UNDEFINED
    }
}
