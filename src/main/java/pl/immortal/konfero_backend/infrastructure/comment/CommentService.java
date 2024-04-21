package pl.immortal.konfero_backend.infrastructure.comment;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.immortal.konfero_backend.infrastructure.auth.UserUtil;
import pl.immortal.konfero_backend.model.Role;
import pl.immortal.konfero_backend.model.entity.Comment;
import pl.immortal.konfero_backend.model.entity.User;
import pl.immortal.konfero_backend.model.entity.repository.CommentRepository;

@Service
@AllArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
	private final UserUtil userUtil;

	void delete(Long commentId) {
		Comment comment = findByIdWithAuthorOrAdminCheck(commentId, userUtil.getCurrentUser());
		commentRepository.delete(comment);
	}

	//

	private Comment findById(Long commentId) {
		return commentRepository.findById(commentId)
				.orElseThrow(
						() -> new ResponseStatusException(HttpStatus.NOT_FOUND)
				);
	}

	private Comment findByIdWithAuthorOrAdminCheck(Long commentId, User author) {
		return Option.ofOptional(commentRepository.findById(commentId))
				.filter(c -> author.getRole().equals(Role.ADMIN) || c.getAuthor().equals(author))
				.getOrElseThrow(
						() -> new ResponseStatusException(HttpStatus.NOT_FOUND)
				);
	}
}
