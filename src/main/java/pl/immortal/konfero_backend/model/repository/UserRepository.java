package pl.immortal.konfero_backend.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.immortal.konfero_backend.model.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String email);

    Optional<User> getByEmailOrUsername(String email, String name);

    Optional<User> findByEmail(String username);
}
