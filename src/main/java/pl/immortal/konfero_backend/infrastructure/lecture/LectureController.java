package pl.immortal.konfero_backend.infrastructure.lecture;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.request.LectureSingleRequest;

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
    public void add(@PathVariable Long conferenceId, @RequestBody LectureSingleRequest request) {
        lectureService.add(conferenceId, request);
    }

}
