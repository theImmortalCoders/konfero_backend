package pl.immortal.konfero_backend.infrastructure.conference.dto;

import org.mapstruct.Mapper;
import pl.immortal.konfero_backend.infrastructure.conference.dto.response.ConferenceShortResponse;
import pl.immortal.konfero_backend.infrastructure.conference.dto.response.ConferenceSingleResponse;
import pl.immortal.konfero_backend.model.entity.Conference;

@Mapper
public interface ConferenceMapper {
    ConferenceSingleResponse map(Conference conference);

    ConferenceShortResponse shortMap(Conference conference);
}
