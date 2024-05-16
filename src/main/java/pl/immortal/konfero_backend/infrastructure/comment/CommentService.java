package pl.immortal.konfero_backend.infrastructure.comment;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.immortal.konfero_backend.infrastructure.auth.UserUtil;
import pl.immortal.konfero_backend.infrastructure.conference.ConferenceUtil;
import pl.immortal.konfero_backend.model.Role;
import pl.immortal.konfero_backend.model.entity.Comment;
import pl.immortal.konfero_backend.model.entity.Conference;
import pl.immortal.konfero_backend.model.entity.User;
import pl.immortal.konfero_backend.model.entity.repository.CommentRepository;
import pl.immortal.konfero_backend.util.StringAsJSON;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
	private final UserUtil userUtil;
	private final ConferenceUtil conferenceUtil;

	void add(StringAsJSON content, Long conferenceId) {
		User author = userUtil.getCurrentUser();
		Conference conference = conferenceUtil.getById(conferenceId);

		Comment comment = new Comment();
		comment.setAuthor(author);
		comment.setContent(content.getValue());
		comment.setCreatedAt(LocalDateTime.now());
		comment.setConference(conference);

		commentRepository.save(comment);

		conference.getComments().add(comment);
		conferenceUtil.save(conference);
	}

	void respond(StringAsJSON content, Long commentId) {
		User author = userUtil.getCurrentUser();
		Comment comment = findById(commentId);

		Comment response = new Comment();
		response.setAuthor(author);
		response.setContent(content.getValue());
		response.setCreatedAt(LocalDateTime.now());
		commentRepository.save(response);

		comment.getResponses().add(response);
		commentRepository.save(comment);
	}

	void delete(Long commentId) {
		Comment comment = findByIdWithAuthorOrAdminOrOrganizerCheck(commentId, userUtil.getCurrentUser());

		for (var r : comment.getResponses()) {
			commentRepository.deleteById(r.getId());
		}

		if (comment.getConference() != null) {
			Conference conference = conferenceUtil.getById(comment.getConference().getId());
			conference.getComments().remove(comment);
			conferenceUtil.save(conference);
		}

		commentRepository.delete(comment);
	}

	//

	private Comment findById(Long commentId) {
		return commentRepository.findById(commentId)
				.orElseThrow(
						() -> new ResponseStatusException(HttpStatus.NOT_FOUND)
				);
	}

	private Comment findByIdWithAuthorOrAdminOrOrganizerCheck(Long commentId, User user) {
		return Option.ofOptional(commentRepository.findById(commentId))
				.filter(c -> user.getRole().equals(Role.ADMIN) || c.getAuthor().equals(user) || c.getConference().getOrganizer().equals(user))
				.getOrElseThrow(
						() -> new ResponseStatusException(HttpStatus.NOT_FOUND)
				);
	}
}
