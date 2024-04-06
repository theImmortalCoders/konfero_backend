package pl.immortal.konfero_backend.infrastructure.lecture.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.request.LectureSingleOrganizerRequest;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.response.LectureShortResponse;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.response.LectureSingleResponse;
import pl.immortal.konfero_backend.model.entity.Lecture;

@Mapper
public interface LectureMapper {
    @Mapping(target = "conferenceId", source = "conference.id")
    LectureSingleResponse map(Lecture lecture);

    @Mapping(target = "conferenceId", source = "conference.id")
    @Mapping(target = "interestedAmount", ignore = true)
    LectureShortResponse shortMap(Lecture lecture);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "conference", ignore = true)
    @Mapping(target = "lecturers", ignore = true)
    @Mapping(target = "materials", ignore = true)
    Lecture map(LectureSingleOrganizerRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "conference", ignore = true)
    @Mapping(target = "lecturers", ignore = true)
    @Mapping(target = "materials", ignore = true)
    void update(@MappingTarget Lecture lecture, LectureSingleOrganizerRequest request);
}
