package pl.immortal.konfero_backend.model.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.immortal.konfero_backend.model.entity.OrganizerRequest;
import pl.immortal.konfero_backend.model.entity.User;

import java.util.List;

public interface OrganizerRequestRepository extends JpaRepository<OrganizerRequest, Long> {
	List<OrganizerRequest> findAllByStatus(OrganizerRequest.OrganizerRequestStatus status);
	Boolean existsByUserAndStatus(User user, OrganizerRequest.OrganizerRequestStatus status);
}
