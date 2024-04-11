package pl.immortal.konfero_backend.infrastructure.file;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FileService {
    private final String path = "/var/lib/postgresql/data";
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private final UserUtil userUtil;
    private final FileUtil fileUtil;

    FileSingleResponse uploadImage(@NotNull MultipartFile imageFile, Boolean thumbnail) throws IOException {
        String uniqueFileName = LocalDateTime.now()
                .toString()
                .replaceAll(":", "_") + "_"
                + imageFile.getOriginalFilename();

        Path uploadPath = Paths.get(path);
        Path filePath = uploadPath.resolve(uniqueFileName);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        File file = new File();
        file.setAuthor(userUtil.getCurrentUser());
        file.setPath(uniqueFileName);

        if (thumbnail) {
            String thumbnailFileName = "thumbnail_" + uniqueFileName;
            Path thumbnailFilePath = uploadPath.resolve(thumbnailFileName);
            BufferedImage inputImage = ImageIO.read(filePath.toFile());
            createThumbnail(filePath, thumbnailFilePath, inputImage.getWidth() / 3, inputImage.getHeight() / 3);
            file.setHasThumbnail(true);
        }

        return fileMapper.map(saveImage(file));
    }

    List<FileSingleResponse> uploadMultipleImages(@NotNull List<MultipartFile> imageFiles, Boolean thumbnail) {
        List<FileSingleResponse> imagesResponses = new ArrayList<>();

        for (MultipartFile imageFile : imageFiles) {
            try {
                imagesResponses.add(uploadImage(imageFile, thumbnail));
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request:" + imageFile);
            }
        }

        return imagesResponses;
    }

    byte[] downloadImage(Long imageId, @NotNull Boolean thumbnail) {
        String imageName = fileUtil.getImageById(imageId).getPath();

        if (thumbnail) {
            imageName = "thumbnail_" + imageName;
        }

        Path imagePath = Paths.get(path, imageName);

        try {
            return Files.readAllBytes(imagePath);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image '" + imageName + "' not found");
        }
    }

    List<FileSingleResponse> getImagesIdsByUser(Long authorId) {
        User user = userUtil.getUserById(authorId);
        return fileRepository.findAllByAuthor(user)
                .stream()
                .map(fileMapper::map)
                .toList();
    }

    void deleteImage(Long imageId) {
        User user = userUtil.getCurrentUser();
        File file = fileUtil.getImageById(imageId);

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

    private File saveImage(File file) {
        return Option.of(fileRepository.save(file))
                .getOrElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, file.toString())
                );
    }

    private void createThumbnail(@NotNull Path sourcePath, @NotNull Path targetPath, int width, int height) throws IOException {
        BufferedImage originalImage = ImageIO.read(sourcePath.toFile());
        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g = resizedImage.createGraphics();

        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        ImageIO.write(resizedImage, "png", targetPath.toFile());
    }
}
