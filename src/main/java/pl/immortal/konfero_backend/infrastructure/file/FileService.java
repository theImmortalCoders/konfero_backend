package pl.immortal.konfero_backend.infrastructure.file;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pl.immortal.konfero_backend.infrastructure.auth.UserUtil;
import pl.immortal.konfero_backend.infrastructure.file.dto.FileMapper;
import pl.immortal.konfero_backend.infrastructure.file.dto.FileSingleResponse;
import pl.immortal.konfero_backend.model.Role;
import pl.immortal.konfero_backend.model.entity.File;
import pl.immortal.konfero_backend.model.entity.User;
import pl.immortal.konfero_backend.model.entity.repository.FileRepository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class FileService {
    private final String path = "/var/lib/postgresql/data";
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private final UserUtil userUtil;
    private final FileUtil fileUtil;

    FileSingleResponse uploadFile(@NotNull MultipartFile fileRequest, String description) throws IOException {
        String uniqueFileName = LocalDateTime.now()
                .toString()
                .replaceAll(":", "_") + "_"
                + fileRequest.getOriginalFilename();

        Path uploadPath = Paths.get(path);
        Path filePath = uploadPath.resolve(uniqueFileName);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Files.copy(fileRequest.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        File file = new File();
        file.setAuthor(userUtil.getCurrentUser());
        file.setPath(uniqueFileName);
        file.setDescription(description);
        updateFileType(file);

        return fileMapper.map(saveFileDb(file));
    }

    public Resource getFile(Long fileId) throws FileNotFoundException, MalformedURLException {
        String fileName = fileUtil.getFileById(fileId).getPath();

        Path filePath = Paths.get(path).resolve(fileName);
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new FileNotFoundException("File not found: " + fileId);
        }
    }

    byte[] downloadImage(Long imageId) {
        String imageName = fileUtil.getFileById(imageId).getPath();
        Path imagePath = Paths.get(path, imageName);

        try {
            return Files.readAllBytes(imagePath);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image '" + imageName + "' not found");
        }
    }

    String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    List<FileSingleResponse> getFilesByUser(Long authorId) {
        User user = userUtil.getUserById(authorId);
        return fileRepository.findAllByAuthor(user)
                .stream()
                .map(fileMapper::map)
                .toList();
    }

    void deleteFile(Long imageId) {
        User user = userUtil.getCurrentUser();
        File file = fileUtil.getFileById(imageId);

        if (!user.getId().equals(file.getAuthor().getId()) && !user.getRole().equals(Role.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete image you not own");
        }

        Path imagePath = Paths.get(path, file.getPath());
        try {
            Files.delete(imagePath);
            fileRepository.delete(file);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image '" + imageId + "' not found");
        }
    }

    //

    private File saveFileDb(File file) {
        return Option.of(fileRepository.save(file))
                .getOrElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, file.toString())
                );
    }

    private void updateFileType(File file) {
        String extension = getFileExtension(file.getPath());
        List<String> documents = List.of("pdf", "docx", "doc", "odt");
        List<String> images = List.of("jpg", "jpeg", "png");
        List<String> videos = List.of("mp4", "mpeg-4", "mov");
        List<String> sounds = List.of("mp3", "wav");

        if (documents.contains(extension)) file.setFileType(File.FileType.DOCUMENT);
        if (images.contains(extension)) file.setFileType(File.FileType.IMAGE);
        if (videos.contains(extension)) file.setFileType(File.FileType.VIDEO);
        if (sounds.contains(extension)) file.setFileType(File.FileType.SOUND);
    }
}
