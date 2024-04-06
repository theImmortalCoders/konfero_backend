package pl.immortal.konfero_backend.model.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.immortal.konfero_backend.model.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
