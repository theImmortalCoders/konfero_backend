package pl.immortal.konfero_backend.infrastructure.image.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.immortal.konfero_backend.model.entity.Image;


@Mapper
public interface ImageMapper {
    @Mapping(target = "authorId", source = "author.id")
    ImageSingleResponse map(Image request);
}
