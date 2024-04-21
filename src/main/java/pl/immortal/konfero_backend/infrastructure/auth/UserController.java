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
import pl.immortal.konfero_backend.infrastructure.auth.dto.request.OrganizerSingleBecomeRequest;
import pl.immortal.konfero_backend.infrastructure.auth.dto.response.OrganizerRequestSingleResponse;
import pl.immortal.konfero_backend.infrastructure.auth.dto.response.UserShortResponse;
import pl.immortal.konfero_backend.infrastructure.auth.dto.response.UserSingleResponse;
import pl.immortal.konfero_backend.model.Role;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "User authentication operations (api/oauth2/authorize/google), (api/oauth2/logout)")
@AllArgsConstructor
public class UserController {
	private final UserService userService;
	private final OrganizerRequestService organizerRequestService;

	@GetMapping("/me")
	@Operation(summary = "Get info about current user (Auth)")
	@ApiResponse(responseCode = "200", description = "Successfully retrieved")
	@ApiResponse(responseCode = "401", description = "Unauthorized")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<UserSingleResponse> me() {
		return ResponseEntity.ok(userService.getCurrentUserResponse());
	}

	@GetMapping("/all")
	@Operation(summary = "Get all users (Admin, Organizer)")
	@ApiResponse(responseCode = "200", description = "Successfully retrieved")
	@ApiResponse(responseCode = "401", description = "Unauthorized")
	@ApiResponse(responseCode = "403", description = "Forbidden")
	@PreAuthorize("hasAnyAuthority('ORGANIZER', 'ADMIN')")
	public ResponseEntity<List<UserShortResponse>> getAll() {
		return ResponseEntity.ok(userService.getAll());
	}

	@PostMapping("/become-organizer")
	@Operation(summary = "Become organizer (Auth)")
	@ApiResponse(responseCode = "200", description = "Successfully retrieved")
	@ApiResponse(responseCode = "401", description = "Unauthorized")
	@ApiResponse(responseCode = "404", description = "Bad request")
	@PreAuthorize("isAuthenticated()")
	public void becomeOrganizer(@RequestBody OrganizerSingleBecomeRequest request) {
		organizerRequestService.becomeOrganizer(request);
	}

	@PatchMapping("/{userId}/role")
	@Operation(summary = "Change user role (Admin)")
	@ApiResponse(responseCode = "200", description = "Successfully retrieved")
	@ApiResponse(responseCode = "401", description = "Unauthorized")
	@ApiResponse(responseCode = "403", description = "Forbidden")
	@ApiResponse(responseCode = "404", description = "User not found")
	@PreAuthorize("hasAuthority('ADMIN')")
	public void updateRole(
			@RequestParam Role newRole,
			@PathVariable Long userId,
			HttpServletRequest request,
			HttpServletResponse response) {
		userService.updateRole(newRole, userId, request, response);
	}

	@PatchMapping("/{userId}/ban")
	@Operation(summary = "Ban user (Admin)")
	@ApiResponse(responseCode = "200", description = "Successfully retrieved")
	@ApiResponse(responseCode = "401", description = "Unauthorized")
	@ApiResponse(responseCode = "403", description = "Forbidden")
	@ApiResponse(responseCode = "404", description = "User not found")
	@PreAuthorize("hasAuthority('ADMIN')")
	public void banUser(@PathVariable Long userId) {
		userService.banUser(userId);
	}

	@PutMapping("/{requestId}")
	@Operation(summary = "Review organizer request (Admin)")
	@ApiResponse(responseCode = "200", description = "Successfully retrieved")
	@ApiResponse(responseCode = "401", description = "Unauthorized")
	@ApiResponse(responseCode = "403", description = "Forbidden")
	@ApiResponse(responseCode = "404", description = "User not found")
	@PreAuthorize("hasAuthority('ADMIN')")
	public void reviewOrganizer(@PathVariable Long requestId, @RequestParam boolean approve) {
		organizerRequestService.reviewOrganizer(requestId, approve);
	}

	@PatchMapping("/{userId}/verify")
	@Operation(summary = "Verify user (Admin)")
	@ApiResponse(responseCode = "200", description = "Successfully retrieved")
	@ApiResponse(responseCode = "401", description = "Unauthorized")
	@ApiResponse(responseCode = "403", description = "Forbidden")
	@ApiResponse(responseCode = "404", description = "User not found")
	@PreAuthorize("hasAuthority('ADMIN')")
	public void verify(@PathVariable Long userId) {
		userService.verify(userId);
	}

	@GetMapping("/organizer-requests")
	@Operation(summary = "Get all pending become-organizer requests (Admin)")
	@ApiResponse(responseCode = "200", description = "Successfully retrieved")
	@ApiResponse(responseCode = "401", description = "Unauthorized")
	@ApiResponse(responseCode = "403", description = "Forbidden")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<List<OrganizerRequestSingleResponse>> getAllPendingRequests() {
		return ResponseEntity.ok(organizerRequestService.getAllPendingRequests());
	}
}
