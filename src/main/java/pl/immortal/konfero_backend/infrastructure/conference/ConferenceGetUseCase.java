package pl.immortal.konfero_backend.infrastructure.conference;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.immortal.konfero_backend.infrastructure.auth.UserUtil;
import pl.immortal.konfero_backend.infrastructure.conference.dto.ConferenceMapper;
import pl.immortal.konfero_backend.infrastructure.conference.dto.response.ConferenceSingleResponse;
import pl.immortal.konfero_backend.model.entity.Conference;
import pl.immortal.konfero_backend.model.entity.User;

@Service
@AllArgsConstructor
public class ConferenceGetUseCase {
    private final ConferenceUtil conferenceUtil;
    private final ConferenceMapper conferenceMapper;
    private final UserUtil userUtil;

    public ConferenceSingleResponse getDetails(Long conferenceId) {
        User user = userUtil.getCurrentUserOrNull();

        if (user == null) {
            Conference conference = conferenceUtil.getById(conferenceId);
            updateFullStatus(conference);
            return conferenceMapper.guestMap(conference);
        }

        Conference conference = conferenceUtil.getByIdWithAuthorLecturerOrParticipantCheck(user, conferenceId);
        updateFullStatus(conference);
        return conferenceMapper.map(conference);
    }

    //

    private static void updateFullStatus(Conference conference) {
        if (conference.getParticipantsLimit() >= conference.getParticipants().size()) {
            conference.setParticipantsFull(true);
        }
    }
}
