package pl.immortal.konfero_backend.infrastructure.image;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import pl.immortal.konfero_backend.model.entity.Image;
import pl.immortal.konfero_backend.model.entity.repository.ImageRepository;

import java.util.List;

@Component
@AllArgsConstructor
public class ImageUtil {
    private final ImageRepository imageRepository;

    public Image getImageById(Long imageId) {
        return Option.ofOptional(imageRepository.findById(imageId))
                .getOrElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image " + imageId + " not found."));
    }

    public List<Image> getImagesByIds(List<Long> ids) {
        return imageRepository.findAllById(ids);
    }
}
