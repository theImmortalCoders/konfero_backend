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
import pl.immortal.konfero_backend.infrastructure.auth.UserUtil;
import pl.immortal.konfero_backend.infrastructure.conference.ConferenceUtil;
import pl.immortal.konfero_backend.infrastructure.image.ImageUtil;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.LectureMapper;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.LectureMapperImpl;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.request.LectureSingleRequest;
import pl.immortal.konfero_backend.infrastructure.mail.MailTemplateService;
import pl.immortal.konfero_backend.model.entity.Conference;
import pl.immortal.konfero_backend.model.entity.Image;
import pl.immortal.konfero_backend.model.entity.Lecture;
import pl.immortal.konfero_backend.model.entity.User;
import pl.immortal.konfero_backend.model.entity.repository.ConferenceRepository;
import pl.immortal.konfero_backend.model.entity.repository.LectureRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LectureServiceTest {
    private Conference conference;
    private Lecture lecture;
    private User user;
    private Image image;
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
    private ImageUtil imageUtil;
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
        user = new User();
        user.setId(1L);
        image = new Image();
        image.setId(1L);

        when(conferenceRepository.findById(1L)).thenReturn(Optional.of(conference));
        when(userUtil.getUsersByIds(new ArrayList<>(List.of(1L)))).thenReturn(new ArrayList<>(List.of(user)));
        when(imageUtil.getImageById(any(Long.class))).thenReturn(image);
        when(userUtil.getCurrentUser()).thenReturn(user);
        when(conferenceRepository.save(any(Conference.class))).thenReturn(conference);
        when(lectureRepository.save(any(Lecture.class))).thenReturn(lecture);
    }

    @Test
    public void shouldAddLecture() {
        var request = new LectureSingleRequest();
        request.setStartDateTime(startTime.plusDays(1));
        request.setDurationMinutes(60);
        request.setLecturersIds(new ArrayList<>(List.of(1L)));
        conference.setOrganizer(user);

        lectureService.add(1L, request);

        assertEquals(conference.getEndDateTime(), conference.getStartDateTime().plusMinutes(60));
    }
}
