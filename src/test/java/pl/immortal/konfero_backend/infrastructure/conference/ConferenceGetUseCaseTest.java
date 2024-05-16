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
import pl.immortal.konfero_backend.infrastructure.auth.UserUtil;
import pl.immortal.konfero_backend.infrastructure.conference.dto.ConferenceMapper;
import pl.immortal.konfero_backend.infrastructure.conference.dto.ConferenceMapperImpl;
import pl.immortal.konfero_backend.infrastructure.conference.dto.response.ConferenceSingleResponse;
import pl.immortal.konfero_backend.model.entity.Conference;
import pl.immortal.konfero_backend.model.entity.User;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ConferenceGetUseCaseTest {
	private Conference conference;
	private ConferenceSingleResponse conferenceSingleResponse;
	private User user;
	@Mock
	private ConferenceUtil conferenceUtil;
	@Spy
	private ConferenceMapper conferenceMapper = new ConferenceMapperImpl();
	@Mock
	private UserUtil userUtil;
	@InjectMocks
	private ConferenceGetUseCase conferenceGetUseCase;

	@BeforeEach
	public void setUp() {
		conference = new Conference();
		conference.setId(1L);
		user = new User();
		user.setId(1L);

		conferenceSingleResponse = new ConferenceSingleResponse();
		conferenceSingleResponse.setId(1L);
		conferenceSingleResponse.setTags(new ArrayList<>());
		conferenceSingleResponse.setParticipantsFull(false);
		conferenceSingleResponse.setLectures(new ArrayList<>());
		conferenceSingleResponse.setFormat(Conference.Format.STATIONARY);
		conferenceSingleResponse.setPhotos(new ArrayList<>());
		conferenceSingleResponse.setComments(new ArrayList<>());

		when(conferenceUtil.getById(any(Long.class))).thenReturn(conference);
		when(userUtil.getCurrentUser()).thenReturn(user);
	}

	@Test
	public void shouldGetConference() {
		assertEquals(conferenceSingleResponse, conferenceGetUseCase.getDetails(1L));
	}
}
