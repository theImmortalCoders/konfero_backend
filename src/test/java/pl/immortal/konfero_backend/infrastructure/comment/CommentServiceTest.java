package pl.immortal.konfero_backend.infrastructure.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pl.immortal.konfero_backend.infrastructure.auth.UserUtil;
import pl.immortal.konfero_backend.model.entity.Comment;
import pl.immortal.konfero_backend.model.entity.User;
import pl.immortal.konfero_backend.model.entity.repository.CommentRepository;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CommentServiceTest {
	private final Comment comment = new Comment();
	private final User user = new User();
	@Mock
	private CommentRepository commentRepository;
	@Mock
	private UserUtil userUtil;
	@InjectMocks
	private CommentService commentService;

	@BeforeEach
	public void setUp() {
		user.setId(1L);

		comment.setId(1L);
		comment.setAuthor(user);

		when(userUtil.getCurrentUser()).thenReturn(user);
		when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
	}

	@Test
	public void shouldDelete() {
		commentService.delete(1L);

		verify(commentRepository, times(1)).delete(comment);
	}
}
