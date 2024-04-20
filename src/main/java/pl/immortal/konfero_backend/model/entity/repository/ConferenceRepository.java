package pl.immortal.konfero_backend.model.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.immortal.konfero_backend.model.entity.Conference;

import java.time.LocalDateTime;
import java.util.List;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {
	@Query(value = "SELECT c FROM Conference c " +
			"WHERE (?1 IS NULL OR c.name = ?1) " +
			"AND (?2 IS NULL OR c.canceled = ?2) " +
			"AND (?3 IS NULL OR c.participantsLimit = ?3) " +
			"AND (?4 IS NULL OR c.verified = ?4) " +
			"AND (?5 IS NULL OR c.participantsFull = ?5) " +
			"AND (cast (?6 as localdatetime ) IS NULL OR c.startDateTime >= ?6) " +
			"AND (cast (?7 as localdatetime) IS NULL OR c.endDateTime <= ?7) " +
			"AND (?8 IS NULL OR EXISTS (" +
			"   SELECT 1 FROM c.tags t WHERE t.id IN ?8))" +
			"AND (?9 IS NULL OR c.organizer.id = ?9)")
	Page<Conference> findAllWithFilters(
			@Param("name") String name,
			@Param("canceled") Boolean canceled,
			@Param("participantsLimit") Integer participantsLimit,
			@Param("verified") Boolean verified,
			@Param("participantsFull") Boolean participantsFull,
			@Param("startDateTimeFrom") LocalDateTime startDateTimeFrom,
			@Param("startDateTimeTo") LocalDateTime startDateTimeTo,
			@Param("tags") List<Long> tagsIds,
			@Param("organizerId") Long organizerId,
			Pageable pageable
	);

}
