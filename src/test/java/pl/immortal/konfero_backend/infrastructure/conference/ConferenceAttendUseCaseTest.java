package pl.immortal.konfero_backend.infrastructure.conference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.server.ResponseStatusException;
import pl.immortal.konfero_backend.infrastructure.auth.UserUtil;
import pl.immortal.konfero_backend.infrastructure.mail.MailTemplateService;
import pl.immortal.konfero_backend.model.entity.Conference;
import pl.immortal.konfero_backend.model.entity.User;
import pl.immortal.konfero_backend.model.entity.repository.ConferenceRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ConferenceAttendUseCaseTest {
	private final Conference conference = new Conference();
	private final User user = new User();
	@Mock
	private UserUtil userUtil;
	@Mock
	private MailTemplateService mailTemplateService;
	private final ConferenceRepository conferenceRepository = mock(ConferenceRepository.class);
	@Spy
	private ConferenceUtil conferenceUtil = new ConferenceUtil(conferenceRepository);
	@InjectMocks
	private ConferenceAttendUseCase conferenceAttendUseCase;

	@BeforeEach
	public void setUp() {
		conference.setId(1L);
		conference.setParticipants(new ArrayList<>());
		user.setId(1L);

		when(userUtil.getCurrentUser()).thenReturn(user);
		when(conferenceRepository.findById(1L)).thenReturn(Optional.of(conference));
		when(conferenceRepository.save(any(Conference.class))).thenReturn(conference);
	}

	@Test
	public void shouldSignUp() {
		conferenceAttendUseCase.signUp(1L);

		verify(conferenceRepository, times(1)).save(any(Conference.class));
	}

	@Test
	public void shouldThrowOnCancelled() {
		conference.setCanceled(true);

		assertThrows(
				ResponseStatusException.class,
				() -> conferenceAttendUseCase.signUp(1L)
		);
	}

	@Test
	public void shouldThrowOnLimit() {
		conference.setParticipantsFull(true);

		assertThrows(
				ResponseStatusException.class,
				() -> conferenceAttendUseCase.signUp(1L)
		);
	}

	@Test
	public void shouldSignOut() {
		conference.getParticipants().add(user);

		conferenceAttendUseCase.signOut(1L);

		verify(conferenceRepository, times(1)).save(any(Conference.class));
	}

	@Test
	public void shouldThrowOnNotSignedUp() {
		assertThrows(
				ResponseStatusException.class,
				() -> conferenceAttendUseCase.signOut(1L)
		);
	}
}
