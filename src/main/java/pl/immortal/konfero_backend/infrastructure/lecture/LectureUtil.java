package pl.immortal.konfero_backend.infrastructure.lecture;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import pl.immortal.konfero_backend.model.Role;
import pl.immortal.konfero_backend.model.entity.Lecture;
import pl.immortal.konfero_backend.model.entity.User;
import pl.immortal.konfero_backend.model.entity.repository.LectureRepository;

@Component
@AllArgsConstructor
public class LectureUtil {
    private final LectureRepository lectureRepository;

    public Lecture getById(Long lectureId) {
        return Option.ofOptional(lectureRepository.findById(lectureId))
                .getOrElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecture not found"));
    }

    public Lecture getByIdWithAuthorityCheck(Long lectureId, User user) {
        Lecture lecture = getById(lectureId);

        if (!userIsOwner(user, lecture) && !userIsLecturer(user, lecture) && !user.getRole().equals(Role.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own a conference");
        }

        return lecture;
    }

    public void save(Lecture lecture) {
        Option.of(lectureRepository.save(lecture))
                .getOrElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, lecture.toString()));
    }

    private static boolean userIsOwner(User user, Lecture lecture) {
        return user.getId().equals(lecture.getConference().getOrganizer().getId());
    }

    private static boolean userIsLecturer(User user, Lecture lecture) {
        return lecture.getLecturers().contains(user);
    }
}
