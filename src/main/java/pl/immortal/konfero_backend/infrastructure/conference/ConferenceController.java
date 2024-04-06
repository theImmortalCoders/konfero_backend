package pl.immortal.konfero_backend.infrastructure.conference;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Tag(name = "Conference", description = "Conference workflow")
@RequestMapping("/api/conference")
@AllArgsConstructor
public class ConferenceController {
    private final ConferenceManageUseCase conferenceManageUseCase;
    private final ConferenceGetUseCase conferenceGetUseCase;

    @PostMapping
    @Operation(summary = "Add new conference (Organizer)", description = "Organizer role required")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "403")
    @ApiResponse(responseCode = "401")
    @ApiResponse(responseCode = "400")
    @PreAuthorize("hasAnyAuthority('ORGANIZER')")
    public void add(@RequestBody ConferenceSingleRequest request) {
        conferenceManageUseCase.add(request);
    }

    @PutMapping("/{conferenceId}")
    @Operation(summary = "Update info about conference (Organizer)", description = "Organizer role required")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "403", description = "You not own the conference or not have role")
    @ApiResponse(responseCode = "401")
    @ApiResponse(responseCode = "400")
    @ApiResponse(responseCode = "404", description = "Conference not found")
    @PreAuthorize("hasAnyAuthority('ORGANIZER')")
    public void updateInfo(@PathVariable Long conferenceId, @RequestBody ConferenceSingleRequest request) {
        conferenceManageUseCase.updateInfo(conferenceId, request);
    }

    @PatchMapping("/{conferenceId}/cancel")
    @Operation(summary = "Cancel conference (Organizer)", description = "Organizer role required")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "403", description = "You not own the conference or not have role")
    @ApiResponse(responseCode = "401")
    @ApiResponse(responseCode = "404", description = "Conference not found")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'ADMIN')")
    public void cancel(@PathVariable Long conferenceId) {
        conferenceManageUseCase.cancel(conferenceId);
    }

    @DeleteMapping("/{conferenceId}")
    @Operation(summary = "Delete conference (Organizer)", description = "Only no participants, organizer role required")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "403", description = "You not own the conference or not have role")
    @ApiResponse(responseCode = "401")
    @ApiResponse(responseCode = "400", description = "Conference has participants")
    @ApiResponse(responseCode = "404", description = "Conference not found")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'ADMIN')")
    public void deleteConference(@PathVariable Long conferenceId) {
        conferenceManageUseCase.delete(conferenceId);
    }

    @GetMapping("/{conferenceId}/details")
    @Operation(summary = "Get conference details with role filtering")
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
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
            @RequestParam(required = false) LocalDateTime startDateTimeFrom,
            @RequestParam(required = false) LocalDateTime startDateTimeTo,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<Long> tagsIds,
            @RequestParam(required = false) Boolean canceled,
            @RequestParam(required = false) Integer participantsLimit,
            @RequestParam(required = false) Boolean verified,
            @RequestParam(required = false) Boolean participantsFull
    ) {
        var pageRequest = PageRequest.of(page, size, sortDirection, sort);
        var searchFields = new ConferenceSearchFields(
                startDateTimeFrom,
                startDateTimeTo,
                name,
                tagsIds,
                canceled,
                participantsLimit,
                verified,
                participantsFull
        );

        return ResponseEntity.ok(conferenceGetUseCase.getAll(pageRequest, searchFields));
    }
}
