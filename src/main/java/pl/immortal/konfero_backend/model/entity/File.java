package pl.immortal.konfero_backend.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class File {
    @Id
    @GeneratedValue
    private Long id;
    private String path;
    private boolean hasThumbnail;
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "author_id")
    private User author;
    @Enumerated(EnumType.STRING)
    private FileType fileType;

    public enum FileType{
        IMAGE, DOCUMENT, VIDEO, LINK
    }
}
