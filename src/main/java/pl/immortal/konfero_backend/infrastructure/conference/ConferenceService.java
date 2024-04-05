package pl.immortal.konfero_backend.infrastructure.conference;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.immortal.konfero_backend.infrastructure.auth.UserUtil;
import pl.immortal.konfero_backend.infrastructure.conference.dto.ConferenceMapper;
import pl.immortal.konfero_backend.infrastructure.conference.dto.request.ConferenceSingleRequest;
import pl.immortal.konfero_backend.infrastructure.image.ImageUtil;
import pl.immortal.konfero_backend.infrastructure.mail.MailTemplateService;
import pl.immortal.konfero_backend.model.entity.Conference;
import pl.immortal.konfero_backend.model.entity.User;
import pl.immortal.konfero_backend.model.entity.repository.ConferenceRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ConferenceService {
    private final ConferenceRepository conferenceRepository;
    private final ConferenceMapper conferenceMapper;
    private final ConferenceUtil conferenceUtil;
    private final MailTemplateService mailTemplateService;
    private final ImageUtil imageUtil;
    private final UserUtil userUtil;

    void add(ConferenceSingleRequest request) {
        if (wrongDateTime(request)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad date or time");
        }

        Option.of(request)
                .map(conferenceMapper::map)
                .peek(c -> {
                    c.setOrganizer(userUtil.getCurrentUser());
                    c.setImage(imageUtil.getImageById(request.getImageId()));
                })
                .peek(conferenceUtil::save);
    }

    public void updateInfo(Long conferenceId, ConferenceSingleRequest request) {
        Conference conference = getConferenceWithUserCheck(conferenceId);

        if (wrongDateTime(request)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad date or time");
        }

        Option.of(conference)
                .peek(c -> {
                    conferenceMapper.update(c, request);
                    c.setOrganizer(userUtil.getCurrentUser());
                    c.setImage(imageUtil.getImageById(request.getImageId()));
                })
                .peek(conferenceUtil::save);
    }

    public void cancel(Long conferenceId) {
        Conference conference = getConferenceWithUserCheck(conferenceId);

        if (conference.isCanceled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conference already canceled");
        }

        conference.setCanceled(true);
        conferenceUtil.save(conference);

        for (var participant : conference.getParticipants()) {
            mailTemplateService.sendConferenceInfoEmail(
                    participant.getEmail(),
                    conference.getName(),
                    conference.getStartDateTime(),
                    "Twoja konferencja została anulowana przez organizatora. Przepraszamy."
            );
        }
    }

    public void delete(Long conferenceId) {
        Conference conference = getConferenceWithUserCheck(conferenceId);

        if (!conference.getParticipants().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conference has participants");
        }

        conferenceRepository.delete(conference);
    }

    //

    private Conference getConferenceWithUserCheck(Long conferenceId) {
        User user = userUtil.getCurrentUser();
        Conference conference = conferenceUtil.getById(conferenceId);

        if (userDoNotOwnConference(user, conference)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own a conference");
        }

        return conference;
    }

    private static boolean userDoNotOwnConference(User user, Conference conference) {
        return !user.getId().equals(conference.getOrganizer().getId());
    }

    private static boolean wrongDateTime(ConferenceSingleRequest request) {
        return request.getStartDateTime().isBefore(LocalDateTime.now())
                || request.getStartDateTime().isAfter(request.getEndDateTime());
    }
}
