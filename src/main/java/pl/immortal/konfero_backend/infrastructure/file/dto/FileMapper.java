package pl.immortal.konfero_backend.infrastructure.file.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.immortal.konfero_backend.model.entity.File;


@Mapper
public interface FileMapper {
    @Mapping(target = "authorId", source = "author.id")
    FileSingleResponse map(File request);
}
