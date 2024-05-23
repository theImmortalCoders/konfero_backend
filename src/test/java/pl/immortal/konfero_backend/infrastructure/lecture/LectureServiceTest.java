package pl.immortal.konfero_backend.infrastructure.lecture;

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
import pl.immortal.konfero_backend.infrastructure.conference.ConferenceUtil;
import pl.immortal.konfero_backend.infrastructure.file.FileUtil;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.LectureMapper;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.LectureMapperImpl;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.request.LectureSingleLecturerRequest;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.request.LectureSingleOrganizerRequest;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.response.LectureSingleResponse;
import pl.immortal.konfero_backend.infrastructure.mail.MailTemplateService;
import pl.immortal.konfero_backend.model.entity.Conference;
import pl.immortal.konfero_backend.model.entity.File;
import pl.immortal.konfero_backend.model.entity.Lecture;
import pl.immortal.konfero_backend.model.entity.User;
import pl.immortal.konfero_backend.model.entity.repository.ConferenceRepository;
import pl.immortal.konfero_backend.model.entity.repository.LectureRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LectureServiceTest {
	private Conference conference;
	private Lecture lecture;
	private User user;
	private File file;
	private LocalDateTime startTime = LocalDateTime.now();
	private final LectureRepository lectureRepository = mock(LectureRepository.class);
	@Spy
	private LectureUtil lectureUtil = new LectureUtil(lectureRepository);
	@Spy
	private LectureMapper lectureMapper = new LectureMapperImpl();
	private final ConferenceRepository conferenceRepository = mock(ConferenceRepository.class);
	@Spy
	private final ConferenceUtil conferenceUtil = new ConferenceUtil(conferenceRepository);
	@Mock
	private UserUtil userUtil;
	@Mock
	private FileUtil fileUtil;
	@Mock
	private MailTemplateService mailTemplateService;
	@InjectMocks
	private LectureService lectureService;

	@BeforeEach
	public void setUp() {
		conference = new Conference();
		conference.setId(1L);
		conference.setStartDateTime(startTime.plusDays(1));
		conference.setOrganizer(user);
		lecture = new Lecture();
		lecture.setId(1L);
		lecture.setConference(conference);
		user = new User();
		user.setId(1L);
		file = new File();
		file.setId(1L);

		when(conferenceRepository.findById(1L)).thenReturn(Optional.of(conference));
		when(userUtil.getUsersByIds(new ArrayList<>(List.of(1L)))).thenReturn(new ArrayList<>(List.of(user)));
		when(fileUtil.getImageById(any(Long.class))).thenReturn(file);
		when(userUtil.getCurrentUser()).thenReturn(user);
		when(conferenceRepository.save(any(Conference.class))).thenReturn(conference);
		when(lectureRepository.save(any(Lecture.class))).thenReturn(lecture);
		when(lectureRepository.findById(1L)).thenReturn(Optional.of(lecture));
	}

	@Test
	public void shouldAddLecture() {
		var request = new LectureSingleOrganizerRequest();
		request.setStartDateTime(startTime.plusDays(1));
		request.setDurationMinutes(60);
		request.setLecturersIds(new ArrayList<>(List.of(1L)));
		conference.setOrganizer(user);

		lectureService.add(1L, request);

		assertEquals(conference.getEndDateTime(), conference.getStartDateTime().plusMinutes(60));
	}

	@Test
	public void shouldThrowBadRequestWhenAddLecture() {
		var request = new LectureSingleOrganizerRequest();
		request.setStartDateTime(startTime.minusDays(1));
		request.setDurationMinutes(60);
		request.setLecturersIds(new ArrayList<>(List.of(1L)));
		conference.setOrganizer(user);

		assertThrows(
				ResponseStatusException.class,
				() -> lectureService.add(1L, request)
		);
	}

	@Test
	public void shouldUpdateLectureAsOrganizer() {
		var request = new LectureSingleOrganizerRequest();
		request.setStartDateTime(startTime.plusDays(1));
		request.setDurationMinutes(60);
		request.setLecturersIds(new ArrayList<>(List.of(1L)));
		conference.setOrganizer(user);
		conference.getLectures().add(lecture);

		lectureService.updateAsOrganizer(1L, request);

		assertEquals(conference.getEndDateTime(), conference.getStartDateTime().plusMinutes(60));
	}

	@Test
	public void shouldUpdateLectureAsLecturer() {
		var request = new LectureSingleLecturerRequest();
		conference.setOrganizer(user);
		conference.getLectures().add(lecture);

		lectureService.updateAsLecturer(1L, request);

		verify(lectureRepository, times(1)).save(any(Lecture.class));
	}

	@Test
	public void shouldDeleteLecture() {
		conference.setOrganizer(user);
		conference.getLectures().add(lecture);

		lectureService.delete(1L);

		verify(lectureRepository, times(1)).delete(any(Lecture.class));
	}

	@Test
	public void shouldGetLectureResponse() {
		var response = new LectureSingleResponse();
		response.setId(1L);
		response.setLecturers(new ArrayList<>());
		response.setMaterials(new ArrayList<>());
		response.setInterested(new ArrayList<>());
		response.setConferenceId(1L);
		response.setFormat(Conference.Format.STATIONARY);

		assertEquals(response, lectureService.getById(1L));
	}

	@Test
	public void shouldAddToFavourites() {
		conference.setParticipants(new ArrayList<>(List.of(user)));

		lectureService.addToFavourites(1L);

		verify(lectureRepository, times(1)).save(any(Lecture.class));
		assertEquals(lecture.getInterested(), List.of(user));
	}

	@Test
	public void shouldThrowForbiddenWhenNotSignedIn() {
		assertThrows(
				ResponseStatusException.class,
				() -> lectureService.addToFavourites(1L)
		);
	}

	@Test
	public void shouldRemoveFromFavourites() {
		conference.setParticipants(new ArrayList<>(List.of(user)));
		lecture.setInterested(new ArrayList<>(List.of(user)));
		conference.setEndDateTime(LocalDateTime.now().plusMinutes(1));

		lectureService.removeFromFavourites(1L);

		verify(lectureRepository, times(1)).save(any(Lecture.class));
		assertEquals(lecture.getInterested(), new ArrayList<>());
	}

	@Test
	public void shouldThrowForbiddenWhenRemoveFromFavourites() {
		conference.setParticipants(new ArrayList<>(List.of(user)));
		conference.setEndDateTime(LocalDateTime.now().plusMinutes(1));

		assertThrows(
				ResponseStatusException.class,
				() -> lectureService.removeFromFavourites(1L)
		);
	}
}
