package pl.immortal.konfero_backend.infrastructure.conference;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import pl.immortal.konfero_backend.model.entity.Conference;
import pl.immortal.konfero_backend.model.entity.User;
import pl.immortal.konfero_backend.model.entity.repository.ConferenceRepository;

import java.util.Comparator;

@Component
@AllArgsConstructor
public class ConferenceUtil {
    private final ConferenceRepository conferenceRepository;

    public Conference getByIdWithAuthorCheck(User user, Long conferenceId) {
        Conference conference = Option.ofOptional(conferenceRepository.findById(conferenceId))
                .getOrElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conference not found"));

        if (!user.getId().equals(conference.getOrganizer().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own a conference");
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

    public void save(Conference request) {
        Option.of(conferenceRepository.save(request))
                .getOrElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, request.toString())
                );
    }
}
