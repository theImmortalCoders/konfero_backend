package pl.immortal.konfero_backend.infrastructure.tag;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.immortal.konfero_backend.util.StringAsJSON;

import java.util.List;

@RestController
@Tag(name = "Tag", description = "Tag CRUD")
@RequestMapping("/api/tag")
@AllArgsConstructor
public class TagController {
	private final TagService tagService;

	@PostMapping
	@Operation(summary = "Add new tag (Admin)")
	@ApiResponse(responseCode = "200")
	@ApiResponse(responseCode = "403")
	@ApiResponse(responseCode = "401")
	@ApiResponse(responseCode = "400")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public void add(@RequestBody StringAsJSON tagName) {
		tagService.add(tagName.getValue());
	}

	@DeleteMapping("/{tagId}")
	@Operation(summary = "Delete tag (Admin)")
	@ApiResponse(responseCode = "200")
	@ApiResponse(responseCode = "403")
	@ApiResponse(responseCode = "401")
	@ApiResponse(responseCode = "404")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public void delete(@PathVariable Long tagId) {
		tagService.delete(tagId);
	}

	@GetMapping
	@Operation(summary = "Get all tags")
	@ApiResponse(responseCode = "200")
	public ResponseEntity<List<pl.immortal.konfero_backend.model.entity.Tag>> getAll() {
		return ResponseEntity.ok(tagService.getAll());
	}
}
