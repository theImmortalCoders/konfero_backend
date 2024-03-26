package pl.immortal.konfero_backend.infrastructure.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.immortal.konfero_backend.infrastructure.auth.dto.OrganizerSingleBecomeRequest;
import pl.immortal.konfero_backend.infrastructure.auth.dto.ProfileUpdateSingleRequest;
import pl.immortal.konfero_backend.infrastructure.auth.dto.UserSingleResponse;
import pl.immortal.konfero_backend.model.Role;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "User authentication operations (api/oauth2/authorize/google), (api/oauth2/logout)")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get info about current user (Auth)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserSingleResponse> me() {
        return ResponseEntity.ok(userService.getCurrentUserResponse());
    }

    @PostMapping("/update-profile")
    @Operation(summary = "Update profile with additional data (Auth)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Bad request")
    @PreAuthorize("isAuthenticated()")
    public void updateProfile(@RequestBody ProfileUpdateSingleRequest request) {
        userService.updateProfile(request);
    }

    @PostMapping("/become-organizer")
    @Operation(summary = "Become organizer (update data) (Auth)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Bad request")
    @PreAuthorize("isAuthenticated()")
    public void becomeOrganizer(@RequestBody OrganizerSingleBecomeRequest request) {
        userService.becomeOrganizer(request);
    }

    @PatchMapping("/{userId}/role")
    @Operation(summary = "Change user role (Admin)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void updateRole(
            @RequestBody Role newRole,
            @PathVariable Long userId,
            HttpServletRequest request,
            HttpServletResponse response) {
        userService.updateRole(newRole, userId, request, response);
    }

    @PatchMapping("/{userId}/ban")
    @Operation(summary = "Ban user (Admin)")
    @ApiResponse(responseCode = "200", description = "Successfully authenticated")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void banUser(@PathVariable Long userId) {
        userService.banUser(userId);
    }
}
