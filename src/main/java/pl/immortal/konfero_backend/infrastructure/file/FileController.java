package pl.immortal.konfero_backend.infrastructure.file;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.immortal.konfero_backend.infrastructure.file.dto.FileSingleResponse;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Objects;

@RestController
@Tag(name = "File", description = "Files retrieving operations")
@RequestMapping("/api/file")
@AllArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload file to server (Auth)", description = "Max file size: 10MB")
    @ApiResponse(responseCode = "200", description = "File uploaded successfully")
    @ApiResponse(responseCode = "413", description = "File too large")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FileSingleResponse> uploadFile(
            @RequestBody MultipartFile uploadedFile,
            @RequestParam String description
    ) throws IOException {
        return ResponseEntity.ok(fileService.uploadFile(uploadedFile, description));
    }

    @GetMapping("/{fileId}")
    @Operation(summary = "Download file by id", description = "Downloads file or opens in new tab if pdf")
    @ApiResponse(responseCode = "200", description = "File downloaded successfully")
    @ApiResponse(responseCode = "404", description = "File not found")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) throws MalformedURLException, FileNotFoundException {
        Resource file = fileService.getFile(fileId);
        String fileName = file.getFilename();
        String fileExtension = fileService.getFileExtension(Objects.requireNonNull(file.getFilename()));

        if (fileExtension.equalsIgnoreCase("pdf")) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .body(file);
        } else {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(file);
        }
    }

    @GetMapping("/{imageId}/image")
    @Operation(summary = "Download image from server by id", description = "Returns file as byte array")
    @ApiResponse(responseCode = "200", description = "File downloaded successfully")
    @ApiResponse(responseCode = "404", description = "File not found")
    public ResponseEntity<byte[]> downloadImage(
            @PathVariable Long imageId
    ) {
        byte[] imageData = fileService.downloadImage(imageId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

    @GetMapping
    @Operation(summary = "Get all images for user", description = "Returns array of image data")
    @ApiResponse(responseCode = "200", description = "Image downloaded successfully")
    @ApiResponse(responseCode = "404", description = "Image not found")
    public ResponseEntity<List<FileSingleResponse>> getFilesByUser(
            @RequestParam Long authorId
    ) {
        return ResponseEntity.ok(fileService.getFilesByUser(authorId));
    }

    @DeleteMapping("/{fileId}")
    @Operation(summary = "Delete image from server by id (Auth)", description = "Deletes image from server by name")
    @ApiResponse(responseCode = "200", description = "Image deleted successfully")
    @ApiResponse(responseCode = "404", description = "Image not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @PreAuthorize("isAuthenticated()")
    public void deleteFile(@PathVariable Long fileId) {
        fileService.deleteFile(fileId);
    }
}
