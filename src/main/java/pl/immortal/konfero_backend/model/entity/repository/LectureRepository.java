package pl.immortal.konfero_backend.model.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.immortal.konfero_backend.model.entity.Lecture;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
}
