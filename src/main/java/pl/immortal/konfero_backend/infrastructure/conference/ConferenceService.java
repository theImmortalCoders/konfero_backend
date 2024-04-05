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

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ConferenceService {
    private final ConferenceMapper conferenceMapper;
    private final ConferenceUtil conferenceUtil;
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

    //

    private static boolean wrongDateTime(ConferenceSingleRequest request) {
        return request.getStartDateTime().isBefore(LocalDateTime.now())
                || request.getStartDateTime().isAfter(request.getEndDateTime());
    }
}
