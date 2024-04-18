package pl.immortal.konfero_backend.infrastructure.auth;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.immortal.konfero_backend.infrastructure.auth.dto.UserMapper;
import pl.immortal.konfero_backend.infrastructure.auth.dto.request.OrganizerSingleBecomeRequest;
import pl.immortal.konfero_backend.infrastructure.auth.dto.response.OrganizerRequestSingleResponse;
import pl.immortal.konfero_backend.model.Role;
import pl.immortal.konfero_backend.model.entity.OrganizerRequest;
import pl.immortal.konfero_backend.model.entity.User;
import pl.immortal.konfero_backend.model.entity.repository.OrganizerRequestRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class OrganizerRequestService {
	private final OrganizerRequestRepository organizerRequestRepository;
	private final UserMapper userMapper;
	private final UserUtil userUtil;

	void becomeOrganizer(OrganizerSingleBecomeRequest request) {
		User user = userUtil.getCurrentUser();

		if (!user.getRole().equals(Role.USER)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already has authority");
		}

		user.setPhone(request.getPhone());
		user.setCity(request.getCity());
		user.setCompanyName(request.getCompanyName());
		user.setAddress(request.getAddress());
		userUtil.saveUser(user);

		OrganizerRequest organizerRequest = new OrganizerRequest();
		organizerRequest.setUser(user);
		organizerRequest.setStatus(OrganizerRequest.OrganizerRequestStatus.PENDING);
		saveRequest(organizerRequest);
	}

	void reviewOrganizer(Long requestId, boolean approve) {
		OrganizerRequest request = organizerRequestRepository.findById(requestId)
				.filter(r -> r.getStatus().equals(OrganizerRequest.OrganizerRequestStatus.PENDING))
				.orElseThrow(
						() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found.")
				);

		User user = userUtil.getUserById(request.getUser().getId());

		if (user.getRole().equals(Role.ADMIN)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already has authority");
		}

		user.setRole(Role.ORGANIZER);

		request.setStatus(approve ? OrganizerRequest.OrganizerRequestStatus.APPROVED : OrganizerRequest.OrganizerRequestStatus.DECLINED);
		saveRequest(request);
	}

	List<OrganizerRequestSingleResponse> getAllPendingRequests() {
		return organizerRequestRepository.findAllByStatus(OrganizerRequest.OrganizerRequestStatus.PENDING)
				.stream()
				.map(userMapper::map)
				.toList();
	}

	//

	private void saveRequest(OrganizerRequest request) {
		Option.of(organizerRequestRepository.save(request))
				.getOrElseThrow(
						() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, request.toString())
				);
	}
}
