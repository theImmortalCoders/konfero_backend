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
import pl.immortal.konfero_backend.infrastructure.conference.dto.ConferenceMapper;
import pl.immortal.konfero_backend.infrastructure.conference.dto.ConferenceMapperImpl;
import pl.immortal.konfero_backend.infrastructure.conference.dto.request.ConferenceSingleRequest;
import pl.immortal.konfero_backend.infrastructure.file.FileUtil;
import pl.immortal.konfero_backend.infrastructure.tag.TagUtil;
import pl.immortal.konfero_backend.model.entity.Conference;
import pl.immortal.konfero_backend.model.entity.File;
import pl.immortal.konfero_backend.model.entity.Tag;
import pl.immortal.konfero_backend.model.entity.User;
import pl.immortal.konfero_backend.model.entity.repository.ConferenceRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ConferenceManageUseCaseTest {
	private Conference conference;
	private User user;
	private File file;
	private Tag tag;
	private final ConferenceRepository conferenceRepository = mock(ConferenceRepository.class);
	@Mock
	private TagUtil tagUtil;
	@Spy
	private final ConferenceMapper conferenceMapper = new ConferenceMapperImpl();
	@Spy
	private final ConferenceUtil conferenceUtil = new ConferenceUtil(conferenceRepository);
	@Mock
	private UserUtil userUtil;
	@Mock
	private FileUtil fileUtil;
	@InjectMocks
	private ConferenceManageUseCase conferenceManageUseCase;

	@BeforeEach
	public void setUp() {
		user = new User();
		user.setId(1L);
		file = new File();
		conference = new Conference();
		conference.setId(1L);
		conference.setOrganizer(user);
		conference.setLogo(file);
		conference.setStartDateTime(LocalDateTime.now().plusMonths(1));
		conference.setStartDateTime(LocalDateTime.now().plusMonths(1).plusDays(1));
		tag = new Tag();
		tag.setId(1L);
		tag.setTagName(Conference.TagName.IT);

		when(userUtil.getCurrentUser()).thenReturn(user);
		when(conferenceRepository.save(any(Conference.class))).thenReturn(conference);
		when(conferenceRepository.findById(1L)).thenReturn(Optional.of(conference));
		when(fileUtil.getImageById(any(Long.class))).thenReturn(file);
		when(tagUtil.getAllByIds(anyList())).thenReturn(new ArrayList<>(List.of(tag)));
	}

	@Test
	public void shouldAddConference() {
		var request = new ConferenceSingleRequest();
		request.setStartDateTime(LocalDateTime.now().plusMonths(1));
		request.setLogoId(1L);

		conferenceManageUseCase.add(request);

		verify(conferenceUtil, times(1)).save(any(Conference.class));
	}

	@Test
	public void shouldThrowBadRequestWhenAddConference() {
		var request = new ConferenceSingleRequest();
		request.setStartDateTime(LocalDateTime.now().plusMonths(1).plusDays(1));
		request.setLogoId(1L);

		request.setStartDateTime(LocalDateTime.now().minusDays(1));

		assertThrows(
				ResponseStatusException.class,
				() -> conferenceManageUseCase.add(request)
		);
	}

	@Test
	public void shouldUpdateConferenceInfo() {
		var request = new ConferenceSingleRequest();
		request.setStartDateTime(LocalDateTime.now().plusMonths(1));
		request.setLogoId(1L);

		conferenceManageUseCase.updateInfo(1L, request);

		verify(conferenceUtil, times(1)).save(any(Conference.class));
	}

	@Test
	public void shouldThrowForbiddenWhenAddConferenceInfo() {
		var request = new ConferenceSingleRequest();
		request.setStartDateTime(LocalDateTime.now().plusMonths(1));
		request.setLogoId(1L);
		User user2 = new User();
		user2.setId(2L);
		when(userUtil.getUserById(2L)).thenReturn(user2);
		conference.setOrganizer(user2);

		assertThrows(
				ResponseStatusException.class,
				() -> conferenceManageUseCase.updateInfo(1L, request)
		);
	}

	@Test
	public void shouldThrowBadRequestWhenUpdateConferenceInfo() {
		var request = new ConferenceSingleRequest();
		request.setStartDateTime(LocalDateTime.now().plusMonths(1).plusDays(1));
		request.setLogoId(1L);
		conference.setOrganizer(user);

		request.setStartDateTime(LocalDateTime.now().minusDays(1));

		assertThrows(
				ResponseStatusException.class,
				() -> conferenceManageUseCase.updateInfo(1L, request)
		);
	}

	@Test
	public void shouldCancelConference() {
		conferenceManageUseCase.cancel(1L);

		assertTrue(conference.isCanceled());
	}

	@Test
	public void shouldThrowBadRequestWhenConferenceCancel() {
		conference.setCanceled(true);

		assertThrows(
				ResponseStatusException.class,
				() -> conferenceManageUseCase.cancel(1L)
		);
	}

	@Test
	public void shouldDeleteConference() {
		conferenceManageUseCase.delete(1L);

		verify(conferenceRepository, times(1)).delete(any(Conference.class));
	}

	@Test
	public void shouldThrowBadRequestWhenDeleteConference() {
		conference.setParticipants(new ArrayList<>(List.of(user)));

		assertThrows(
				ResponseStatusException.class,
				() -> conferenceManageUseCase.delete(1L)
		);
	}
}
