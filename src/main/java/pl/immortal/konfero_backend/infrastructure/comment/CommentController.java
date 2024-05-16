package pl.immortal.konfero_backend.infrastructure.comment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Comment", description = "Comment workflow")
@RequestMapping("/api/comment")
@AllArgsConstructor
public class CommentController {
	private final CommentService commentService;

	@DeleteMapping("/{commentId}")
	@Operation(summary = "Delete comment (Admin or author)")
	@ApiResponse(responseCode = "200")
	@ApiResponse(responseCode = "403")
	@ApiResponse(responseCode = "401")
	@ApiResponse(responseCode = "404")
	@PreAuthorize("isAuthenticated()")
	public void delete(@PathVariable Long commentId) {
		commentService.delete(commentId);
	}
}
