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
import pl.immortal.konfero_backend.model.Role;
import pl.immortal.konfero_backend.model.entity.Conference;
import pl.immortal.konfero_backend.model.entity.User;
import pl.immortal.konfero_backend.model.entity.repository.ConferenceRepository;

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
				pageRequest
		);

		return new PageImpl<>(
				conferences
						.stream()
						.map(c -> {
							var response = conferenceMapper.shortMap(c);
							setConferenceResponseStats(response, c, userUtil.getCurrentUser());
							return response;
						})
						.toList(), pageRequest, conferenceRepository.count()
		);
	}

	//

	private static void setConferenceResponseStats(ConferenceSingleResponse response, Conference conference, User user) {
		response.setAmISignedUp(conference.getParticipants().contains(user));
		response.setParticipantsAmount(conference.getParticipants().size());
	}

	private static void setConferenceResponseStats(ConferenceShortResponse response, Conference conference, User user) {
		response.setAmISignedUp(conference.getParticipants().contains(user));
		response.setParticipantsAmount(conference.getParticipants().size());
	}


}
