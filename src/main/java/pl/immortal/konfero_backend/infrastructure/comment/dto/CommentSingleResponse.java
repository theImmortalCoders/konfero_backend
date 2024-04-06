package pl.immortal.konfero_backend.infrastructure.comment.dto;

import lombok.Data;
import pl.immortal.konfero_backend.infrastructure.auth.dto.response.UserShortResponse;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentSingleResponse {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private UserShortResponse author;
    private List<CommentSingleResponse> responses;
}
