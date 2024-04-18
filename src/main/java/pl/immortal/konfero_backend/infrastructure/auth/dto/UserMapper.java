package pl.immortal.konfero_backend.infrastructure.auth.dto;

import org.mapstruct.Mapper;
import pl.immortal.konfero_backend.infrastructure.auth.dto.response.OrganizerRequestSingleResponse;
import pl.immortal.konfero_backend.infrastructure.auth.dto.response.UserShortResponse;
import pl.immortal.konfero_backend.infrastructure.auth.dto.response.UserSingleResponse;
import pl.immortal.konfero_backend.model.entity.OrganizerRequest;
import pl.immortal.konfero_backend.model.entity.User;

@Mapper
public interface UserMapper {
	UserSingleResponse map(User request);

	UserShortResponse shortMap(User request);

	OrganizerRequestSingleResponse map(OrganizerRequest request);
}
