package pl.immortal.konfero_backend.infrastructure.comment.dto;

import org.mapstruct.Mapper;
import pl.immortal.konfero_backend.model.entity.Comment;

@Mapper
public interface CommentMapper {
    CommentSingleResponse map(Comment request);
}
