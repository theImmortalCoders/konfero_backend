package pl.immortal.konfero_backend.infrastructure.conference;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.immortal.konfero_backend.infrastructure.auth.UserUtil;
import pl.immortal.konfero_backend.infrastructure.conference.dto.ConferenceMapper;
import pl.immortal.konfero_backend.infrastructure.conference.dto.request.ConferenceSingleRequest;
import pl.immortal.konfero_backend.infrastructure.file.FileUtil;
import pl.immortal.konfero_backend.infrastructure.mail.MailTemplateService;
import pl.immortal.konfero_backend.infrastructure.tag.TagUtil;
import pl.immortal.konfero_backend.model.entity.Conference;
import pl.immortal.konfero_backend.model.entity.Tag;
import pl.immortal.konfero_backend.model.entity.User;
import pl.immortal.konfero_backend.model.entity.repository.ConferenceRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ConferenceManageUseCase {
	private final ConferenceRepository conferenceRepository;
	private final ConferenceMapper conferenceMapper;
	private final ConferenceUtil conferenceUtil;
	private final MailTemplateService mailTemplateService;
	private final FileUtil fileUtil;
	private final UserUtil userUtil;
	private final TagUtil tagUtil;

	void add(ConferenceSingleRequest request) {
		if (wrongDateTime(request)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad date or time");
		}

		List<Tag> tags = tagUtil.getAllByIds(request.getTagsIds());

		Option.of(request)
				.map(conferenceMapper::map)
				.peek(c -> updateConferenceData(request, c, tags))
				.peek(conferenceUtil::save);
	}

	public void updateInfo(Long conferenceId, ConferenceSingleRequest request) {
		Conference conference = getConferenceWithUserCheck(conferenceId);

		if (wrongDateTime(request)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad date or time");
		}

		List<Tag> tags = tagUtil.getAllByIds(request.getTagsIds());

		Option.of(conference)
				.peek(c -> conferenceMapper.update(c, request))
				.peek(c -> updateConferenceData(request, c, tags))
				.peek(conferenceUtil::save);
	}

	public void cancel(Long conferenceId) {
		Conference conference = getConferenceWithUserCheck(conferenceId);

		if (conference.isCanceled()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conference already canceled");
		}

		conference.setCanceled(true);
		conferenceUtil.save(conference);

		for (var participant : conference.getParticipants()) {
			mailTemplateService.sendConferenceInfoEmail(
					participant.getEmail(),
					conference.getName(),
					conference.getStartDateTime(),
					"Twoja konferencja została anulowana przez organizatora. Przepraszamy."
			);
		}
	}

	public void delete(Long conferenceId) {
		Conference conference = getConferenceWithUserCheck(conferenceId);

		if (!conference.getParticipants().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conference has participants");
		}

		conferenceRepository.delete(conference);
	}

	//

	private void updateConferenceData(ConferenceSingleRequest request, Conference c, List<Tag> tags) {
		if (request.getLogoId() != null) {
			c.setLogo(fileUtil.getImageById(request.getLogoId()));
		}
		c.setTags(new ArrayList<>(tags));
		c.setOrganizer(userUtil.getCurrentUser());
		c.setPhotos(new ArrayList<>(fileUtil.getImagesByIds(request.getPhotosIds())));
		conferenceUtil.updateConferenceEndTimeByLectures(c);
		conferenceUtil.updateFullStatus(c);
		if (c.getOrganizer().isVerified()) {
			c.setVerified(true);
		}
		if(c.getParticipantsLimit() == 0){
			c.setParticipantsFull(true);
		}
	}

	private Conference getConferenceWithUserCheck(Long conferenceId) {
		User user = userUtil.getCurrentUser();
		return conferenceUtil.getByIdWithAuthorCheck(user, conferenceId);
	}

	private static boolean wrongDateTime(ConferenceSingleRequest request) {
		return request.getStartDateTime().isBefore(LocalDateTime.now());
	}
}
