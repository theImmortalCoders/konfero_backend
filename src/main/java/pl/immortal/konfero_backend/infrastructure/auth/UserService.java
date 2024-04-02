package pl.immortal.konfero_backend.infrastructure.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.immortal.konfero_backend.infrastructure.auth.dto.UserMapper;
import pl.immortal.konfero_backend.infrastructure.auth.dto.request.ProfileUpdateSingleRequest;
import pl.immortal.konfero_backend.infrastructure.auth.dto.response.UserSingleResponse;
import pl.immortal.konfero_backend.model.Role;
import pl.immortal.konfero_backend.model.entity.User;

@Service
@AllArgsConstructor
public class UserService {
    private final UserUtil userUtil;
    private final UserMapper userMapper;
    private final OrganizerRequestService organizerRequestService;

    UserSingleResponse getCurrentUserResponse() {
        return userMapper.map(userUtil.getCurrentUser());
    }

    void updateProfile(ProfileUpdateSingleRequest request) {
        User user = userUtil.getCurrentUser();

        user.setPhone(request.getPhone());
        user.setCity(request.getCity());

        userUtil.saveUser(user);
    }

    void updateRole(Role newRole, Long userId, HttpServletRequest request, HttpServletResponse response) {
        User user = userUtil.getUserById(userId);

        user.setRole(newRole);

        userUtil.saveUser(user);

        if (userId.equals(userUtil.getCurrentUser().getId())) {
            logoutUser(request, response);
        }
    }

    void banUser(Long userId) {
        User user = userUtil.getUserById(userId);

        if (user.getId().equals(userUtil.getCurrentUser().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot ban yourself");
        }

        user.setActive(false);

        userUtil.saveUser(user);
    }

    //

    private static void logoutUser(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
