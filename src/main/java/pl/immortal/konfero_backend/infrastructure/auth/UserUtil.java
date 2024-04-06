package pl.immortal.konfero_backend.infrastructure.auth;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import pl.immortal.konfero_backend.model.entity.User;
import pl.immortal.konfero_backend.model.entity.repository.UserRepository;

import java.util.List;

@Component
@AllArgsConstructor
public class UserUtil {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return Option.ofOptional(userRepository.findByEmail(username))
                .getOrElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public User getCurrentUserOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            return getCurrentUser();
        }
        return null;
    }


    public User getUserById(Long userId) {
        return Option.ofOptional(userRepository.findById(userId))
                .getOrElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                );
    }

    public List<User> getUsersByIds(List<Long> usersIds) {
        return userRepository.findAllById(usersIds);
    }

    public void saveUser(User user) {
        Option.of(userRepository.save(user))
                .getOrElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, user.toString())
                );
    }
}
