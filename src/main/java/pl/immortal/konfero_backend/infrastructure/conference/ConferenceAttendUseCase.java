package pl.immortal.konfero_backend.infrastructure.conference;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.immortal.konfero_backend.infrastructure.auth.UserUtil;
import pl.immortal.konfero_backend.infrastructure.mail.MailTemplateService;
import pl.immortal.konfero_backend.model.entity.Conference;
import pl.immortal.konfero_backend.model.entity.User;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ConferenceAttendUseCase {
	private final ConferenceUtil conferenceUtil;
	private final UserUtil userUtil;
	private final MailTemplateService mailTemplateService;

	void signUp(Long conferenceId) {
		User user = userUtil.getCurrentUser();
		Conference conference = conferenceUtil.getById(conferenceId);

		if (conference.isCanceled() || conference.getEndDateTime().isBefore(LocalDateTime.now()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conference cancelled or ended");
		if (conference.isParticipantsFull())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Participants full");
		if (conference.getParticipants().contains(user))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You've already signed in");

		conference.getParticipants().add(user);
		conferenceUtil.updateFullStatus(conference);

		conferenceUtil.save(conference);

		mailTemplateService.sendConferenceInfoEmail(
				user.getEmail(),
				conference.getName(),
				conference.getStartDateTime(),
				"Zapisałeś się na konferencję. Do zobaczenia niebawem!"
		);
	}

	void signOut(Long conferenceId) {
		User user = userUtil.getCurrentUser();
		Conference conference = conferenceUtil.getById(conferenceId);

		if (!conference.getParticipants().contains(user))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You haven't been signed up");
		if (conference.isCanceled() || conference.getEndDateTime().isBefore(LocalDateTime.now()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conference cancelled or ended");

		conference.getParticipants().remove(user);
		conferenceUtil.updateFullStatus(conference);

		conferenceUtil.save(conference);

		mailTemplateService.sendConferenceInfoEmail(
				user.getEmail(),
				conference.getName(),
				conference.getStartDateTime(),
				"Zostałeś usunięty z listy uczestników konferencji."
		);
	}

	//


}
