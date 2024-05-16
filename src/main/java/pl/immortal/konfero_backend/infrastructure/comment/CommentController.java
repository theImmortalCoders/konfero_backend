package pl.immortal.konfero_backend.infrastructure.comment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.immortal.konfero_backend.util.StringAsJSON;

@RestController
@Tag(name = "Comment", description = "Comment workflow")
@RequestMapping("/api/comment")
@AllArgsConstructor
public class CommentController {
	private final CommentService commentService;

	@PostMapping
	@Operation(summary = "Add comment to conference (Login)")
	@ApiResponse(responseCode = "200")
	@ApiResponse(responseCode = "401")
	@ApiResponse(responseCode = "400")
	@PreAuthorize("isAuthenticated()")
	public void add(@RequestBody StringAsJSON content, @RequestParam Long conferenceId) {
		commentService.add(content, conferenceId);
	}

	@PostMapping("/{commentId}/respond")
	@Operation(summary = "Respond to comment (Login)")
	@ApiResponse(responseCode = "200")
	@ApiResponse(responseCode = "401")
	@ApiResponse(responseCode = "404")
	@ApiResponse(responseCode = "400")
	@PreAuthorize("isAuthenticated()")
	public void respond(@RequestBody StringAsJSON content, @PathVariable Long commentId) {
		commentService.respond(content, commentId);
	}

	@DeleteMapping("/{commentId}")
	@Operation(summary = "Delete comment (Admin or organizer or author)")
	@ApiResponse(responseCode = "200")
	@ApiResponse(responseCode = "403")
	@ApiResponse(responseCode = "401")
	@ApiResponse(responseCode = "404")
	@PreAuthorize("isAuthenticated()")
	public void delete(@PathVariable Long commentId) {
		commentService.delete(commentId);
	}
}
