package pl.immortal.konfero_backend.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import pl.immortal.konfero_backend.model.Role;

import java.util.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails, OAuth2User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String googleId;
    @NotNull
    private String username;
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;
    @Email
    @Column(unique = true)
    private String email;
    private String photo;
    private boolean active = true;
    private String companyName;
    private String address;
    private String city;
    private String phone;
    @ManyToMany
    @ToString.Exclude
    private List<Conference> conferencesOrganized = new ArrayList<>();
    @ManyToMany
    @ToString.Exclude
    private List<Conference> conferencesParticipated = new ArrayList<>();
    private boolean verified;

    @Override
    public String getName() {
        return this.username;
    }

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", id);
        attributes.put("sub", googleId);
        attributes.put("email", email);
        attributes.put("role", role);
        attributes.put("login", username);
        attributes.put("avatar_url", photo);
        attributes.put("picture", photo);
        attributes.put("active", active);
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getRole().toString()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.active;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }


}