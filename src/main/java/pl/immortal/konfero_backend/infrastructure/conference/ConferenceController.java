package pl.immortal.konfero_backend.infrastructure.conference;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.immortal.konfero_backend.infrastructure.conference.dto.request.ConferenceSingleRequest;
import pl.immortal.konfero_backend.infrastructure.conference.dto.response.ConferenceShortResponse;
import pl.immortal.konfero_backend.infrastructure.conference.dto.response.ConferenceSingleResponse;
import pl.immortal.konfero_backend.model.ConferenceSearchFields;
import pl.immortal.konfero_backend.model.ConferenceStatus;
import pl.immortal.konfero_backend.model.entity.Conference;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Tag(name = "Conference", description = "Conference workflow")
@RequestMapping("/api/conference")
@AllArgsConstructor
public class ConferenceController {
	private final ConferenceManageUseCase conferenceManageUseCase;
	private final ConferenceGetUseCase conferenceGetUseCase;
	private final ConferenceAttendUseCase conferenceAttendUseCase;

	@PostMapping
	@Operation(summary = "Add new conference (Organizer, admin)", description = "Organizer role required")
	@ApiResponse(responseCode = "200")
	@ApiResponse(responseCode = "403")
	@ApiResponse(responseCode = "401")
	@ApiResponse(responseCode = "400")
	@PreAuthorize("hasAnyAuthority('ORGANIZER', 'ADMIN')")
	public void add(@RequestBody @Valid ConferenceSingleRequest request) {
		conferenceManageUseCase.add(request);
	}

	@PutMapping("/{conferenceId}")
	@Operation(summary = "Update info about conference (Organizer, admin)", description = "Organizer role required")
	@ApiResponse(responseCode = "200")
	@ApiResponse(responseCode = "403", description = "You not own the conference or not have role")
	@ApiResponse(responseCode = "401")
	@ApiResponse(responseCode = "400")
	@ApiResponse(responseCode = "404", description = "Conference not found")
	@PreAuthorize("hasAnyAuthority('ORGANIZER', 'ADMIN')")
	public void updateInfo(@PathVariable Long conferenceId, @RequestBody @Valid ConferenceSingleRequest request) {
		conferenceManageUseCase.updateInfo(conferenceId, request);
	}

	@DeleteMapping("/{conferenceId}/cancel")
	@Operation(summary = "Cancel conference (Organizer, Admin)", description = "Organizer role required")
	@ApiResponse(responseCode = "200")
	@ApiResponse(responseCode = "403", description = "You not own the conference or not have role")
	@ApiResponse(responseCode = "401")
	@ApiResponse(responseCode = "404", description = "Conference not found")
	@PreAuthorize("hasAnyAuthority('ORGANIZER', 'ADMIN')")
	public void cancel(@PathVariable Long conferenceId) {
		conferenceManageUseCase.cancel(conferenceId);
	}

	@DeleteMapping("/{conferenceId}")
	@Operation(summary = "Delete conference (Organizer, Admin)", description = "Only no participants, organizer role required")
	@ApiResponse(responseCode = "200")
	@ApiResponse(responseCode = "403", description = "You not own the conference or not have role")
	@ApiResponse(responseCode = "401")
	@ApiResponse(responseCode = "400", description = "Conference has participants")
	@ApiResponse(responseCode = "404", description = "Conference not found")
	@PreAuthorize("hasAnyAuthority('ORGANIZER', 'ADMIN')")
	public void deleteConference(@PathVariable Long conferenceId) {
		conferenceManageUseCase.delete(conferenceId);
	}

	@PostMapping("/{conferenceId}/attend")
	@Operation(summary = "Sign up for conference")
	@ApiResponse(responseCode = "200")
	@ApiResponse(responseCode = "401")
	@ApiResponse(responseCode = "400", description = "You already take part")
	@ApiResponse(responseCode = "404", description = "Conference not found")
	@PreAuthorize("isAuthenticated()")
	public void signUp(@PathVariable Long conferenceId) {
		conferenceAttendUseCase.signUp(conferenceId);
	}

	@DeleteMapping("/{conferenceId}/attend")
	@Operation(summary = "Sign out from conference")
	@ApiResponse(responseCode = "200")
	@ApiResponse(responseCode = "401")
	@ApiResponse(responseCode = "400", description = "You haven't been signed up")
	@ApiResponse(responseCode = "404", description = "Conference not found")
	@PreAuthorize("isAuthenticated()")
	public void signOut(@PathVariable Long conferenceId) {
		conferenceAttendUseCase.signOut(conferenceId);
	}

	@GetMapping("/{conferenceId}/details")
	@Operation(summary = "Get conference details with role checking")
	@ApiResponse(responseCode = "200")
	@ApiResponse(responseCode = "404", description = "Conference not found")
	public ResponseEntity<ConferenceSingleResponse> getDetails(@PathVariable Long conferenceId) {
		return ResponseEntity.ok(conferenceGetUseCase.getDetails(conferenceId));
	}

	@GetMapping
	@Operation(summary = "Get all conferences with filters and sorting")
	@ApiResponse(responseCode = "200")
	public ResponseEntity<Page<ConferenceShortResponse>> getAll(
			@RequestParam(defaultValue = "20") int size,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "startDateTime") Conference.SortFields sort,
			@RequestParam(defaultValue = "ASC") Sort.Direction sortDirection,
			@RequestParam(required = false) LocalDateTime startDateTimeFrom,
			@RequestParam(required = false) LocalDateTime startDateTimeTo,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) List<Long> tagsIds,
			@RequestParam(required = false) Boolean canceled,
			@RequestParam(required = false) Integer participantsLimit,
			@RequestParam(required = false) Boolean verified,
			@RequestParam(required = false) Boolean participantsFull,
			@RequestParam(required = false) Boolean finishedOnly,
			@RequestParam(required = false) Long organizerId,
			@RequestParam(required = false) String locationName
	) {
		var pageRequest = PageRequest.of(page, size, sortDirection, sort.toString());
		var searchFields = new ConferenceSearchFields(
				startDateTimeFrom,
				startDateTimeTo,
				name,
				tagsIds,
				canceled,
				participantsLimit,
				verified,
				participantsFull,
				finishedOnly,
				organizerId,
				locationName
		);

		return ResponseEntity.ok(conferenceGetUseCase.getAll(pageRequest, searchFields));
	}

	@GetMapping("/my")
	@Operation(summary = "Get all conferences I am signed for")
	@ApiResponse(responseCode = "200")
	@ApiResponse(responseCode = "401")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<ConferenceShortResponse>> getMyConferences(
			@RequestParam(required = false) ConferenceStatus conferenceStatus
	) {
		return ResponseEntity.ok(conferenceGetUseCase.getMy(conferenceStatus));
	}

}
