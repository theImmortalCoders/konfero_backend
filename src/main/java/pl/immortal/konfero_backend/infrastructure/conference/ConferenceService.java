package pl.immortal.konfero_backend.infrastructure.conference;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.immortal.konfero_backend.infrastructure.conference.dto.ConferenceMapper;
import pl.immortal.konfero_backend.model.entity.repository.ConferenceRepository;

@Service
@AllArgsConstructor
public class ConferenceService {
    private final ConferenceRepository conferenceRepository;
    private final ConferenceMapper conferenceMapper;

}
