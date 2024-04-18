package pl.immortal.konfero_backend.infrastructure.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.server.ResponseStatusException;
import pl.immortal.konfero_backend.infrastructure.auth.dto.UserMapper;
import pl.immortal.konfero_backend.infrastructure.auth.dto.UserMapperImpl;
import pl.immortal.konfero_backend.infrastructure.auth.dto.request.OrganizerSingleBecomeRequest;
import pl.immortal.konfero_backend.infrastructure.auth.dto.response.OrganizerRequestSingleResponse;
import pl.immortal.konfero_backend.infrastructure.auth.dto.response.UserSingleResponse;
import pl.immortal.konfero_backend.model.Role;
import pl.immortal.konfero_backend.model.entity.OrganizerRequest;
import pl.immortal.konfero_backend.model.entity.User;
import pl.immortal.konfero_backend.model.entity.repository.OrganizerRequestRepository;
import pl.immortal.konfero_backend.model.entity.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OrganizerRequestServiceTest {
	private User user;
	private OrganizerRequest organizerRequest;
	@Mock
	private SecurityContextHolderStrategy securityContextHolderStrategy;
	@Mock
	private SecurityContextRepository securityContextRepository;
	@Mock
	private Authentication authentication;
	@Mock
	private SecurityContext securityContext;
	@Mock
	private OrganizerRequestRepository organizerRequestRepository;
	@Spy
	private UserMapper userMapper = new UserMapperImpl();
	private final UserRepository userRepository = mock(UserRepository.class);
	@Spy
	private UserUtil userUtil = new UserUtil(userRepository);
	@InjectMocks
	private OrganizerRequestService organizerRequestService;

	@BeforeEach
	public void setUp() {
		user = new User();
		user.setId(1L);
		user.setRole(Role.USER);

		organizerRequest = new OrganizerRequest();
		organizerRequest.setId(1L);
		organizerRequest.setUser(user);
		organizerRequest.setStatus(OrganizerRequest.OrganizerRequestStatus.PENDING);

		SecurityContextHolder.setContext(securityContext);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(securityContext.getAuthentication().getName()).thenReturn("cinek@gmail.com");
		when(securityContextHolderStrategy.createEmptyContext()).thenReturn(securityContext);

		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
		when(userRepository.save(any(User.class))).thenReturn(user);
		when(organizerRequestRepository.findAllByStatus(OrganizerRequest.OrganizerRequestStatus.PENDING))
				.thenReturn(new ArrayList<>(List.of(organizerRequest)));
		when(organizerRequestRepository.findById(1L)).thenReturn(Optional.of(organizerRequest));
		when(organizerRequestRepository.save(any(OrganizerRequest.class))).thenReturn(organizerRequest);
	}

	@Test
	public void shouldSendOrganizerRequest() {
		var request = new OrganizerSingleBecomeRequest();

		organizerRequestService.becomeOrganizer(request);

		verify(userRepository, times(1)).save(user);
		verify(organizerRequestRepository, times(1)).save(any(OrganizerRequest.class));
	}

	@Test
	public void shouldThrowBadRequestWhenSendRequest() {
		var request = new OrganizerSingleBecomeRequest();
		user.setRole(Role.ADMIN);

		assertThrows(
				ResponseStatusException.class,
				() -> organizerRequestService.becomeOrganizer(request)
		);
	}

	@Test
	public void shouldReviewOrganizer() {
		organizerRequestService.reviewOrganizer(1L, true);

		assertEquals(Role.ORGANIZER, user.getRole());
		assertEquals(OrganizerRequest.OrganizerRequestStatus.APPROVED, organizerRequest.getStatus());
	}

	@Test
	public void shouldThrowNotFoundWhenReviewOrganizer() {
		when(organizerRequestRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(
				ResponseStatusException.class,
				() -> organizerRequestService.reviewOrganizer(1L, true)
		);
	}

	@Test
	public void shouldThrowBadRequestWhenReviewOrganizer() {
		user.setRole(Role.ADMIN);

		assertThrows(
				ResponseStatusException.class,
				() -> organizerRequestService.reviewOrganizer(1L, true)
		);
	}

	@Test
	public void shouldGetAllPendingRequests() {
		UserSingleResponse userResponse = new UserSingleResponse();
		userResponse.setId(1L);
		userResponse.setRole(Role.USER);
		userResponse.setActive(true);
		userResponse.setConferencesOrganized(new ArrayList<>());
		userResponse.setConferencesParticipated(new ArrayList<>());

		OrganizerRequestSingleResponse response = new OrganizerRequestSingleResponse();
		response.setId(1L);
		response.setUser(userResponse);
		response.setStatus(OrganizerRequest.OrganizerRequestStatus.PENDING);

		assertEquals(List.of(response), organizerRequestService.getAllPendingRequests());
	}

}
