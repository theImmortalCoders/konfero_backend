package pl.immortal.konfero_backend.infrastructure.file;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import pl.immortal.konfero_backend.model.entity.File;
import pl.immortal.konfero_backend.model.entity.repository.FileRepository;

import java.util.List;

@Component
@AllArgsConstructor
public class FileUtil {
    private final FileRepository fileRepository;

    public File getFileById(Long imageId) {
        return Option.ofOptional(fileRepository.findById(imageId))
                .getOrElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image " + imageId + " not found."));
    }

    public File getImageById(Long imageId) {
        return Option.ofOptional(fileRepository.findById(imageId))
                .filter(f -> f.getFileType() != null && f.getFileType().equals(File.FileType.IMAGE))
                .getOrElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image " + imageId + " not found."));
    }

    public List<File> getImagesByIds(List<Long> ids) {
        return fileRepository.findAllById(ids)
                .stream()
                .filter(f -> f.getFileType().equals(File.FileType.IMAGE))
                .toList();
    }
}
