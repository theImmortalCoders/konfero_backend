package pl.immortal.konfero_backend.infrastructure.auth;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.immortal.konfero_backend.infrastructure.auth.dto.OrganizerSingleBecomeRequest;
import pl.immortal.konfero_backend.infrastructure.auth.dto.ProfileUpdateSingleRequest;
import pl.immortal.konfero_backend.infrastructure.auth.dto.UserMapper;
import pl.immortal.konfero_backend.infrastructure.auth.dto.UserSingleResponse;
import pl.immortal.konfero_backend.model.Role;

@Service
@AllArgsConstructor
public class UserService {
    private final OidcAuthService oidcAuthService;
    private final UserMapper userMapper;

    UserSingleResponse getCurrentUserResponse() {
        return userMapper.map(oidcAuthService.getCurrentUser());
    }

    void updateProfile(ProfileUpdateSingleRequest request) {
    }

    void becomeOrganizer(OrganizerSingleBecomeRequest request) {
    }

    void updateRole(Role newRole, String userId) {
    }

    void banUser(String userId) {
    }
}
