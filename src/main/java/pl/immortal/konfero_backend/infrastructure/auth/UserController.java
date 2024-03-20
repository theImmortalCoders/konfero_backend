package pl.immortal.konfero_backend.infrastructure.auth;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.immortal.konfero_backend.model.entity.User;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    private final OidcAuthService oidcAuthService;
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> me(){
        return ResponseEntity.ok(oidcAuthService.getCurrentUser());
    }
}
