package pl.immortal.konfero_backend.infrastructure.file.dto;

import org.mapstruct.Mapper;
import pl.immortal.konfero_backend.infrastructure.auth.dto.UserMapper;
import pl.immortal.konfero_backend.infrastructure.auth.dto.UserMapperImpl;
import pl.immortal.konfero_backend.model.entity.File;


@Mapper
public interface FileMapper {
	UserMapper userMapper = new UserMapperImpl();

	default FileSingleResponse map(File request) {
		var response = new FileSingleResponse();
		response.setId(request.getId());
		response.setAuthor(userMapper.shortMap(request.getAuthor()));
		response.setPath(request.getPath());
		response.setFileType(request.getFileType());
		response.setDescription(request.getDescription());
		response.setCreatedDate(request.getCreatedDate());
		return response;
	}
}
