package pl.immortal.konfero_backend.model.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.immortal.konfero_backend.model.entity.Conference;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {
}
