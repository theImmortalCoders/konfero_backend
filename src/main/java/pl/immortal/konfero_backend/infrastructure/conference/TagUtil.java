package pl.immortal.konfero_backend.infrastructure.conference;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import pl.immortal.konfero_backend.model.entity.Tag;
import pl.immortal.konfero_backend.model.entity.repository.TagRepository;

import java.util.List;

@Component
@AllArgsConstructor
public class TagUtil {
    private final TagRepository tagRepository;

    public Tag getById(Long tagId) {
        return tagRepository.findById(tagId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found")
                );
    }

    public List<Tag> getAllByIds(List<Long> tagsIds) {
        return tagRepository.findAllById(tagsIds);
    }
}
