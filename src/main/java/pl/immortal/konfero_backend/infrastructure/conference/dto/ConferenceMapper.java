package pl.immortal.konfero_backend.infrastructure.conference.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.immortal.konfero_backend.infrastructure.conference.dto.request.ConferenceSingleRequest;
import pl.immortal.konfero_backend.infrastructure.conference.dto.response.ConferenceShortResponse;
import pl.immortal.konfero_backend.infrastructure.conference.dto.response.ConferenceSingleResponse;
import pl.immortal.konfero_backend.model.entity.Conference;

@Mapper
public interface ConferenceMapper {
    @Mapping(target = "finished", ignore = true)
    ConferenceSingleResponse map(Conference conference);

    @Mapping(target = "finished", ignore = true)
    @Mapping(target = "participants", ignore = true)
    ConferenceSingleResponse guestMap(Conference conference);

    @Mapping(target = "finished", ignore = true)
    ConferenceShortResponse shortMap(Conference conference);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "logo", ignore = true)
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "lectures", ignore = true)
    @Mapping(target = "photos", ignore = true)
    Conference map(ConferenceSingleRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "logo", ignore = true)
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "lectures", ignore = true)
    @Mapping(target = "photos", ignore = true)
    void update(@MappingTarget Conference conference, ConferenceSingleRequest request);
}
