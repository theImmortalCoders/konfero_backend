package pl.immortal.konfero_backend.infrastructure.conference.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.immortal.konfero_backend.infrastructure.conference.dto.request.ConferenceSingleRequest;
import pl.immortal.konfero_backend.infrastructure.conference.dto.response.ConferenceShortResponse;
import pl.immortal.konfero_backend.infrastructure.conference.dto.response.ConferenceSingleResponse;
import pl.immortal.konfero_backend.model.entity.Conference;

@Mapper
public interface ConferenceMapper {
    ConferenceSingleResponse map(Conference conference);

    ConferenceShortResponse shortMap(Conference conference);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "lectures", ignore = true)
    Conference map(ConferenceSingleRequest request);
}
