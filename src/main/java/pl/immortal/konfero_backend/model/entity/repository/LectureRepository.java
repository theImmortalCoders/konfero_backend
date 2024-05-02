package pl.immortal.konfero_backend.model.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.immortal.konfero_backend.model.entity.Lecture;
import pl.immortal.konfero_backend.model.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
	List<Lecture> findAllByInterestedContaining(User user);

	List<Lecture> findAllByInterestedContainingAndStartDateTimeBefore(User user, LocalDateTime time);


	List<Lecture> findAllByInterestedContainingAndStartDateTimeAfter(User user, LocalDateTime time);
}
