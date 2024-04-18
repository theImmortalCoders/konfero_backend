package pl.immortal.konfero_backend.model.entity.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.immortal.konfero_backend.model.entity.File;
import pl.immortal.konfero_backend.model.entity.User;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
	List<File> findAllByAuthor(User author);
}
