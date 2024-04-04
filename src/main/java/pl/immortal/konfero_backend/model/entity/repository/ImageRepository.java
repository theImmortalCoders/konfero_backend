package pl.immortal.konfero_backend.model.entity.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.immortal.konfero_backend.model.entity.Image;
import pl.immortal.konfero_backend.model.entity.User;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByAuthor(User author);
}
