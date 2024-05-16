package pl.immortal.konfero_backend.infrastructure.conference;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.immortal.konfero_backend.infrastructure.auth.UserUtil;
import pl.immortal.konfero_backend.infrastructure.conference.dto.ConferenceMapper;
import pl.immortal.konfero_backend.infrastructure.conference.dto.response.ConferenceShortResponse;
import pl.immortal.konfero_backend.infrastructure.conference.dto.response.ConferenceSingleResponse;
import pl.immortal.konfero_backend.model.ConferenceSearchFields;
import pl.immortal.konfero_backend.model.ConferenceStatus;
import pl.immortal.konfero_backend.model.Role;
import pl.immortal.konfero_backend.model.entity.Conference;
import pl.immortal.konfero_backend.model.entity.User;
import pl.immortal.konfero_backend.model.entity.repository.ConferenceRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ConferenceGetUseCase {
	private final ConferenceUtil conferenceUtil;
	private final ConferenceMapper conferenceMapper;
	private final ConferenceRepository conferenceRepository;
	private final UserUtil userUtil;

	public ConferenceSingleResponse getDetails(Long conferenceId) {
		User user = userUtil.getCurrentUserOrNull();

		if (user == null || user.getRole().equals(Role.USER)) {
			Conference conference = conferenceUtil.getById(conferenceId);
			var response = conferenceMapper.guestMap(conference);
			setConferenceResponseStats(response, conference, user);
			return response;
		}

		Conference conference = conferenceUtil.getByIdWithAuthorLecturerOrParticipantCheck(user, conferenceId);
		var response = conferenceMapper.map(conference);
		setConferenceResponseStats(response, conference, user);
		return response;
	}

	public Page<ConferenceShortResponse> getAll(PageRequest pageRequest, ConferenceSearchFields searchFields) {
		Page<Conference> conferences = conferenceRepository.findAllWithFilters(
				searchFields.getName(),
				searchFields.getCanceled(),
				searchFields.getParticipantsLimit(),
				searchFields.getVerified(),
				searchFields.getParticipantsFull(),
				searchFields.getStartDateTimeFrom(),
				searchFields.getStartDateTimeTo(),
				searchFields.getTagsIds(),
				searchFields.getOrganizerId(),
				searchFields.getLocationName(),
				pageRequest
		);

		return new PageImpl<>(
				conferences
						.stream()
						.map(c -> {
							var response = conferenceMapper.shortMap(c);
							setConferenceResponseStats(response, c, userUtil.getCurrentUserOrNull());
							return response;
						})
						.toList(), pageRequest, conferenceRepository.count()
		);
	}

	List<ConferenceShortResponse> getMy(ConferenceStatus lectureStatus) {
		User user = userUtil.getCurrentUser();

		if (lectureStatus == null)
			return conferenceRepository.findAllByParticipantsContaining(user)
					.stream()
					.map(conferenceMapper::shortMap)
					.toList();
		if (lectureStatus.equals(ConferenceStatus.ENDED))
			return conferenceRepository.findAllByParticipantsContainingAndEndDateTimeBefore(user, LocalDateTime.now())
					.stream()
					.map(conferenceMapper::shortMap)
					.toList();
		if (lectureStatus.equals(ConferenceStatus.ONGOING))
			return conferenceRepository.findAllByParticipantsContainingAndStartDateTimeBeforeAndEndDateTimeAfter(user, LocalDateTime.now(), LocalDateTime.now())
					.stream()
					.map(conferenceMapper::shortMap)
					.toList();
		return conferenceRepository.findAllByParticipantsContainingAndStartDateTimeAfter(user, LocalDateTime.now())
				.stream()
				.map(conferenceMapper::shortMap)
				.toList();
	}

	//

	private static void setConferenceResponseStats(ConferenceSingleResponse response, Conference conference, User user) {
		if (user != null) response.setAmISignedUp(conference.getParticipants().contains(user));
		response.setParticipantsAmount(conference.getParticipants().size());
	}

	private static void setConferenceResponseStats(ConferenceShortResponse response, Conference conference, User user) {
		if (user != null) response.setAmISignedUp(conference.getParticipants().contains(user));
		response.setParticipantsAmount(conference.getParticipants().size());
	}
}
