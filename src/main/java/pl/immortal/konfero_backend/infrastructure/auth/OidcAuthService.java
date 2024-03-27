package pl.immortal.konfero_backend.infrastructure.auth;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.immortal.konfero_backend.infrastructure.mail.MailTemplateService;
import pl.immortal.konfero_backend.model.entity.User;
import pl.immortal.konfero_backend.model.entity.repository.UserRepository;

import java.util.Map;

@Service
@AllArgsConstructor
public class OidcAuthService extends OidcUserService {

    private final UserRepository userRepository;
    private final MailTemplateService mailTemplateService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        var attributes = oidcUser.getAttributes();
        User user = userRepository
                .findByUsername((String) attributes.get("email"))
                .orElseGet(() -> createUser(attributes));
        userRepository.save(user);

        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Konto użytkownika jest nieaktywne.");
        }

        return new DefaultOidcUser(user.getAuthorities(), oidcUser.getIdToken(),
                new OidcUserInfo(attributes), "email");
    }

    private User createUser(Map<String, Object> attributes) {
        return Option.ofOptional(userRepository
                        .getByEmailOrUsername((String) attributes.get("email"), (String) attributes.get("name")))
                .map(usr -> {
                    usr.setGoogleId((String) attributes.get("sub"));
                    return usr;
                })
                .getOrElse(() -> {
                    User newUser = new User();
                    newUser.setUsername((String) attributes.get("name"));
                    newUser.setEmail((String) attributes.get("email"));
                    newUser.setPhoto((String) attributes.get("picture"));
                    newUser.setGoogleId((String) attributes.get("sub"));

                    mailTemplateService.sendWelcomeEmail(newUser.getEmail(), newUser.getName());

                    return userRepository.save(newUser);
                });
    }

}
