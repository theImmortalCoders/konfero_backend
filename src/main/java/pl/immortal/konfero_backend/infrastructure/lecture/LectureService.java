package pl.immortal.konfero_backend.infrastructure.lecture;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.immortal.konfero_backend.infrastructure.auth.UserUtil;
import pl.immortal.konfero_backend.infrastructure.conference.ConferenceUtil;
import pl.immortal.konfero_backend.infrastructure.image.ImageUtil;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.LectureMapper;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.request.LectureSingleLecturerRequest;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.request.LectureSingleOrganizerRequest;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.response.LectureSingleResponse;
import pl.immortal.konfero_backend.infrastructure.mail.MailTemplateService;
import pl.immortal.konfero_backend.model.entity.Conference;
import pl.immortal.konfero_backend.model.entity.Lecture;
import pl.immortal.konfero_backend.model.entity.User;
import pl.immortal.konfero_backend.model.entity.repository.LectureRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;
    private final LectureMapper lectureMapper;
    private final LectureUtil lectureUtil;
    private final ConferenceUtil conferenceUtil;
    private final UserUtil userUtil;
    private final ImageUtil imageUtil;
    private final MailTemplateService mailTemplateService;

    void add(Long conferenceId, LectureSingleOrganizerRequest request) {
        Conference conference = getConferenceWithTimeCheck(conferenceId, request.getStartDateTime());
        List<User> lecturers = userUtil.getUsersByIds(request.getLecturersIds());

        Lecture lecture = Option.of(request)
                .map(lectureMapper::map)
                .peek(l -> {
                    updateConferenceData(request, l, lecturers, conference);
                })
                .peek(lectureUtil::save)
                .get();

        conference.getLectures().add(lecture);
        conferenceUtil.sortLectures(conference);
        conferenceUtil.updateConferenceEndTimeByLectures(conference);
        conferenceUtil.save(conference);
    }


    void updateAsOrganizer(Long lectureId, LectureSingleOrganizerRequest request) {
        Lecture lecture = lectureUtil.getByIdWithAuthorityCheck(lectureId, userUtil.getCurrentUser());
        List<User> lecturers = userUtil.getUsersByIds(request.getLecturersIds());
        Conference conference = lecture.getConference();
        conference.getLectures().remove(lecture);

        Option.of(lecture)
                .peek(l -> lectureMapper.update(l, request))
                .peek(l -> {
                    updateConferenceData(request, l, lecturers, conference);
                    sendAddInfoToParticipants(l, conference);
                })
                .peek(lectureUtil::save);

        conference.getLectures().add(lecture);
        conferenceUtil.sortLectures(conference);
        conferenceUtil.updateConferenceEndTimeByLectures(conference);
        conferenceUtil.save(conference);
    }

    void updateAsLecturer(Long lectureId, LectureSingleLecturerRequest request) {
        Lecture lecture = lectureUtil.getByIdWithAuthorityCheck(lectureId, userUtil.getCurrentUser());
        lecture.setDescription(request.getDescription());
        if (request.getImageId() != null) {
            lecture.setImage(imageUtil.getImageById(request.getImageId()));
        }
        lectureUtil.save(lecture);
    }

    void delete(Long lectureId) {
        Lecture lecture = lectureUtil.getByIdWithAuthorityCheck(lectureId, userUtil.getCurrentUser());
        Conference conference = lecture.getConference();

        conference.getLectures().remove(lecture);
        conferenceUtil.sortLectures(conference);
        conferenceUtil.updateConferenceEndTimeByLectures(conference);
        conferenceUtil.save(conference);

        lectureRepository.delete(lecture);
    }

    LectureSingleResponse getById(Long lectureId) {
        return Option.of(lectureUtil.getById(lectureId))
                .map(lectureMapper::map)
                .get();
    }

    //

    private void updateConferenceData(LectureSingleOrganizerRequest request, Lecture l, List<User> lecturers, Conference conference) {
        if (request.getImageId() != null) {
            l.setImage(imageUtil.getImageById(request.getImageId()));
        }
        l.setLecturers(lecturers);
        l.setConference(conference);
        sendAddInfoToLecturers(l, conference);
    }

    private void sendAddInfoToLecturers(Lecture l, Conference conference) {
        for (var lecturer : l.getLecturers()) {
            mailTemplateService.sendConferenceInfoEmail(
                    lecturer.getEmail(),
                    conference.getName(),
                    conference.getStartDateTime(),
                    "Zostałeś dodany jako prelegent do wykładu '" + l.getName() + "'."
            );
        }
    }

    private void sendAddInfoToParticipants(Lecture l, Conference conference) {
        for (var participant : conference.getParticipants()) {
            mailTemplateService.sendConferenceInfoEmail(
                    participant.getEmail(),
                    conference.getName(),
                    conference.getStartDateTime(),
                    "Do konferencji dodano nowe wydarzenie: '" + l.getName() + "'."
            );
        }
    }

    private Conference getConferenceWithTimeCheck(Long conferenceId, LocalDateTime lectureStartTime) {
        Conference conference = conferenceUtil.getByIdWithAuthorCheck(userUtil.getCurrentUser(), conferenceId);

        if (conference.getStartDateTime().isAfter(lectureStartTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lecture start time is before conference start time");
        }

        return conference;
    }
}
