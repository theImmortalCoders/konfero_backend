package pl.immortal.konfero_backend.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrganizerRequest {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "author_id")
    private User user;
    private OrganizerRequestStatus status = OrganizerRequestStatus.PENDING;

    public enum OrganizerRequestStatus {
        PENDING, APPROVED, DECLINED
    }
}
