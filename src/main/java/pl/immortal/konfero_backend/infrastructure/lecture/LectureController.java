package pl.immortal.konfero_backend.infrastructure.lecture;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.request.LectureSingleLecturerRequest;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.request.LectureSingleOrganizerRequest;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.response.LectureSingleResponse;

@RestController
@Tag(name = "Lecture", description = "Lecture CRUD")
@RequestMapping("/api/lecture")
@AllArgsConstructor
public class LectureController {
    private final LectureService lectureService;

    @PostMapping("/{conferenceId}")
    @Operation(summary = "Add lecture to the conference (Organizer)", description = "Organizer role required")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "403", description = "You not own the conference or not have role")
    @ApiResponse(responseCode = "401")
    @ApiResponse(responseCode = "400")
    @ApiResponse(responseCode = "404", description = "Conference not found")
    @PreAuthorize("hasAnyAuthority('ORGANIZER')")
    public void add(@PathVariable Long conferenceId, @RequestBody @Valid LectureSingleOrganizerRequest request) {
        lectureService.add(conferenceId, request);
    }

    @PatchMapping("/{lectureId}/organizer")
    @Operation(summary = "Modify lecture info (Organizer)", description = "Organizer role required")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "403", description = "You not own the conference or not have role")
    @ApiResponse(responseCode = "401")
    @ApiResponse(responseCode = "400")
    @ApiResponse(responseCode = "404", description = "Conference not found")
    @PreAuthorize("hasAnyAuthority('ORGANIZER')")
    public void updateOrganizer(@PathVariable Long lectureId, @RequestBody @Valid LectureSingleOrganizerRequest request) {
        lectureService.updateAsOrganizer(lectureId, request);
    }

    @PatchMapping("/{lectureId}/lecturer")
    @Operation(summary = "Modify lecture info (Lecturer)")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "403", description = "You are not lecturer")
    @ApiResponse(responseCode = "401")
    @ApiResponse(responseCode = "400")
    @ApiResponse(responseCode = "404", description = "Conference not found")
    @PreAuthorize("isAuthenticated()")
    public void updateLecturer(@PathVariable Long lectureId, @RequestBody @Valid LectureSingleLecturerRequest request) {
        lectureService.updateAsLecturer(lectureId, request);
    }

    @DeleteMapping("/{lectureId}")
    @Operation(summary = "Delete lecture (Organizer)", description = "Organizer role required")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "403", description = "You not own the conference or not have role")
    @ApiResponse(responseCode = "401")
    @ApiResponse(responseCode = "400")
    @ApiResponse(responseCode = "404", description = "Conference not found")
    @PreAuthorize("hasAnyAuthority('ORGANIZER')")
    public void deleteLecture(@PathVariable Long lectureId) {
        lectureService.delete(lectureId);
    }

    @GetMapping("/{lectureId}")
    @Operation(summary = "Get lecture details")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404", description = "Lecture not found")
    public ResponseEntity<LectureSingleResponse> getById(@PathVariable Long lectureId) {
        return ResponseEntity.ok(lectureService.getById(lectureId));
    }

}
