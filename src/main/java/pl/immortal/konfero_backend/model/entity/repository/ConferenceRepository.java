package pl.immortal.konfero_backend.model.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.immortal.konfero_backend.model.entity.Conference;
import pl.immortal.konfero_backend.model.entity.Tag;
import pl.immortal.konfero_backend.model.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {
	@Query(value = "SELECT c FROM Conference c " +
			"WHERE (?1 IS NULL OR lower(c.name) LIKE concat('%', lower(cast(?1 as string)), '%')) " +
			"AND (?2 IS NULL OR c.canceled = ?2) " +
			"AND (?3 IS NULL OR c.participantsLimit = ?3) " +
			"AND (?4 IS NULL OR c.verified = ?4) " +
			"AND (?5 IS NULL OR c.participantsFull = ?5) " +
			"AND (cast (?6 as localdatetime ) IS NULL OR c.startDateTime >= ?6) " +
			"AND (cast (?7 as localdatetime) IS NULL OR c.endDateTime <= ?7) " +
			"AND (?8 IS NULL OR EXISTS (" +
			"    SELECT 1" +
			"    FROM c.tags t" +
			"    WHERE t.id IN ?8" +
			"    GROUP BY c.id" +
			"    HAVING COUNT(DISTINCT t.id) = ?9))" +
			"AND (?10 IS NULL OR c.organizer.id = ?10)" +
			"AND (?11 IS NULL OR lower(cast(c.location as string)) LIKE CONCAT('%', lower(cast(?11 as string)), '%'))" +
			"AND (?12 IS NULL OR cast(c.endDateTime as localdatetime) < CURRENT_TIMESTAMP )")
	Page<Conference> findAllWithFilters(
			@Param("name") String name,
			@Param("canceled") Boolean canceled,
			@Param("participantsLimit") Integer participantsLimit,
			@Param("verified") Boolean verified,
			@Param("participantsFull") Boolean participantsFull,
			@Param("startDateTimeFrom") LocalDateTime startDateTimeFrom,
			@Param("startDateTimeTo") LocalDateTime startDateTimeTo,
			@Param("tags") List<Long> tagsIds,
			@Param("tagsAmount") int tagsAmount,
			@Param("organizerId") Long organizerId,
			@Param("locationName") String locationName,
			@Param("locationName") Boolean showFinished,
			Pageable pageable
	);

	List<Conference> findAllByTagsContaining(Tag tag);

	List<Conference> findAllByParticipantsContaining(User user);

	List<Conference> findAllByParticipantsContainingAndEndDateTimeBefore(User user, LocalDateTime time);

	List<Conference> findAllByParticipantsContainingAndStartDateTimeBeforeAndEndDateTimeAfter(User user, LocalDateTime startDateTime, LocalDateTime endDateTime);

	List<Conference> findAllByParticipantsContainingAndStartDateTimeAfter(User user, LocalDateTime time);

}
