package pl.immortal.konfero_backend.infrastructure.auth.dto;

import org.mapstruct.Mapper;
import pl.immortal.konfero_backend.model.entity.User;

@Mapper
public interface UserMapper {
    public UserSingleResponse map(User request);
}
