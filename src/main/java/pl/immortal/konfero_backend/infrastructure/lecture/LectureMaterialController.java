package pl.immortal.konfero_backend.infrastructure.lecture;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Lecture material", description = "Lecture materials CRUD")
@RequestMapping("/api/lecture-material")
@AllArgsConstructor
public class LectureMaterialController {
    private final LectureMaterialUseCase lectureMaterialUseCase;

    @PostMapping("/{lectureId}")
    @Operation(summary = "Add material to lecture (Lecturer)")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "403", description = "You not own the lecture")
    @ApiResponse(responseCode = "401")
    @ApiResponse(responseCode = "400", description = "You are not owner of material")
    @ApiResponse(responseCode = "404", description = "Lecture not found")
    @PreAuthorize("isAuthenticated()")
    public void add(@PathVariable Long lectureId, @RequestParam Long materialId) {
        lectureMaterialUseCase.add(lectureId, materialId);
    }

    @DeleteMapping("/{lectureId}")
    @Operation(summary = "Delete material (Lecturer)")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "403", description = "You not own the lecture")
    @ApiResponse(responseCode = "401")
    @ApiResponse(responseCode = "400")
    @ApiResponse(responseCode = "404", description = "Lecture or material not found")
    @PreAuthorize("isAuthenticated()")
    public void remove(@PathVariable Long lectureId, @RequestParam Long materialId) {
        lectureMaterialUseCase.remove(lectureId, materialId);
    }

}
