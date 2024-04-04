package pl.immortal.konfero_backend.infrastructure.lecture.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.response.LectureShortResponse;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.response.LectureSingleResponse;
import pl.immortal.konfero_backend.model.entity.Lecture;

@Mapper
public interface LectureMapper {
    @Mapping(target = "conferenceId", source = "conference.id")
    LectureSingleResponse map(Lecture lecture);

    @Mapping(target = "conferenceId", source = "conference.id")
    LectureShortResponse shortMap(Lecture lecture);
}
