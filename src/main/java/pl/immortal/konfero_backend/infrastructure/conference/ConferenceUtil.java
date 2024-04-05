package pl.immortal.konfero_backend.infrastructure.conference;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import pl.immortal.konfero_backend.model.entity.Conference;
import pl.immortal.konfero_backend.model.entity.repository.ConferenceRepository;

@Component
@AllArgsConstructor
public class ConferenceUtil {
    private final ConferenceRepository conferenceRepository;

    public void save(Conference request) {
        Option.of(conferenceRepository.save(request))
                .getOrElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, request.toString())
                );
    }
}
