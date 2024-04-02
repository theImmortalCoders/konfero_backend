package pl.immortal.konfero_backend.infrastructure.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import pl.immortal.konfero_backend.infrastructure.auth.dto.request.ProfileUpdateSingleRequest;
import pl.immortal.konfero_backend.infrastructure.mail.MailTemplateService;
import pl.immortal.konfero_backend.model.Role;
import pl.immortal.konfero_backend.model.entity.User;
import pl.immortal.konfero_backend.model.entity.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {
    private User user;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private SecurityContextHolderStrategy securityContextHolderStrategy;
    @Mock
    private SecurityContextRepository securityContextRepository;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @Spy
    private UserMapper userMapper = new UserMapperImpl();
    private final MailTemplateService mailTemplateService = mock(MailTemplateService.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    @Spy
    private UserUtil userUtil = new UserUtil(userRepository);
    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(securityContext.getAuthentication().getName()).thenReturn("cinek@gmail.com");
        when(securityContextHolderStrategy.createEmptyContext()).thenReturn(securityContext);

        user = new User();
        user.setId(1L);
        user.setGoogleId("35346345342");
        user.setEmail("cinek@gmail.com");
        user.setPhoto("photo");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
    }

    @Test
    public void shouldUpdateProfile() {
        var updateRequest = new ProfileUpdateSingleRequest();
        updateRequest.setCity("city");
        updateRequest.setPhone("phone");

        userService.updateProfile(updateRequest);

        assertEquals(updateRequest.getCity(), user.getCity());
        assertEquals(updateRequest.getPhone(), user.getPhone());
    }

    @Test
    public void shouldUpdateRole() {
        Role newRole = Role.ADMIN;

        userService.updateRole(newRole, 1L, request, response);

        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    public void shouldBanUser() {
        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@mail.com");

        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        userService.banUser(2L);

        assertFalse(user2.isActive());
    }

    @Test
    public void shouldThrowBadRequestWhenBanUser() {
        assertThrows(
                ResponseStatusException.class,
                () -> userService.banUser(1L)
        );
    }
}
