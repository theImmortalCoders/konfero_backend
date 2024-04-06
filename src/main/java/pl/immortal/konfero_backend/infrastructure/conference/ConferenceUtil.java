package pl.immortal.konfero_backend.infrastructure.conference;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import pl.immortal.konfero_backend.model.Role;
import pl.immortal.konfero_backend.model.entity.Conference;
import pl.immortal.konfero_backend.model.entity.Lecture;
import pl.immortal.konfero_backend.model.entity.User;
import pl.immortal.konfero_backend.model.entity.repository.ConferenceRepository;

import java.util.ArrayList;
import java.util.Comparator;

@Component
@AllArgsConstructor
public class ConferenceUtil {
    private final ConferenceRepository conferenceRepository;

    public Conference getById(Long conferenceId) {
        return Option.ofOptional(conferenceRepository.findById(conferenceId))
                .getOrElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conference not found"));
    }

    public Conference getByIdWithAuthorCheck(User user, Long conferenceId) {
        Conference conference = getById(conferenceId);

        if (notOwnerOrAdmin(user, conference)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own a conference");
        }

        return conference;
    }

    public Conference getByIdWithAuthorLecturerOrParticipantCheck(User user, Long conferenceId) {
        Conference conference = getById(conferenceId);

        if (notParticipantOrLecturer(conference, user) && notOwnerOrAdmin(user, conference)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot view this conference");
        }

        return conference;
    }

    public void updateConferenceEndTimeByLectures(Conference conference) {
        var lastLecture = conference.getLectures()
                .stream()
                .max(Comparator.comparing(l -> l.getStartDateTime().plusMinutes(l.getDurationMinutes())))
                .orElse(null);

        if (lastLecture == null) {
            conference.setEndDateTime(conference.getStartDateTime());
            return;
        }

        conference.setEndDateTime(lastLecture.getStartDateTime().plusMinutes(lastLecture.getDurationMinutes()));
    }

    public void sortLectures(Conference conference) {
        conference.setLectures(
                new ArrayList<>(
                        conference.getLectures()
                                .stream()
                                .sorted(Comparator.comparing(Lecture::getStartDateTime)).toList())
        );
    }

    public void save(Conference request) {
        Option.of(conferenceRepository.save(request))
                .getOrElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, request.toString())
                );
    }

    //

    private static boolean notOwnerOrAdmin(User user, Conference conference) {
        return !user.getId().equals(conference.getOrganizer().getId()) && !user.getRole().equals(Role.ADMIN);
    }

    private static boolean notParticipantOrLecturer(Conference conference, User user) {
        return !conference.getParticipants().contains(user) && conference.getLectures().stream().filter(l -> l.getLecturers().contains(user)).toList().isEmpty();
    }
}
