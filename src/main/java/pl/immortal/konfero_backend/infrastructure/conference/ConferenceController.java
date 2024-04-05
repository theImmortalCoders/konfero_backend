package pl.immortal.konfero_backend.infrastructure.conference;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.immortal.konfero_backend.infrastructure.conference.dto.request.ConferenceSingleRequest;

@RestController
@Tag(name = "Conference", description = "Conference workflow")
@RequestMapping("/api/conference")
@AllArgsConstructor
public class ConferenceController {
    private final ConferenceService conferenceService;

    @PostMapping
    @Operation(summary = "Add new conference (Organizer)", description = "Organizer role required")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "403")
    @ApiResponse(responseCode = "401")
    @ApiResponse(responseCode = "400")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'ADMIN')")
    public void add(@RequestBody ConferenceSingleRequest request) {
        conferenceService.add(request);
    }
}
