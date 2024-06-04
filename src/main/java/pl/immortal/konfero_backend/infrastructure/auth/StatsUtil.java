package pl.immortal.konfero_backend.infrastructure.auth;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.immortal.konfero_backend.infrastructure.auth.dto.response.UserStatsResponse;
import pl.immortal.konfero_backend.model.Role;
import pl.immortal.konfero_backend.model.entity.repository.ConferenceRepository;
import pl.immortal.konfero_backend.model.entity.repository.UserRepository;

@Component
@AllArgsConstructor
public class StatsUtil {
	private final UserRepository userRepository;
	private final ConferenceRepository conferenceRepository;


	UserStatsResponse getStats() {
		var stats = new UserStatsResponse();
		stats.setUsersAmount(userRepository.count());
		stats.setOrganizersAmount(userRepository.countAllByRole(Role.ORGANIZER));
		stats.setConferencesAmount(conferenceRepository.count());
		return stats;
	}
}
