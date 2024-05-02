package pl.immortal.konfero_backend.infrastructure.conference.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import pl.immortal.konfero_backend.infrastructure.conference.dto.request.ConferenceSingleRequest;
import pl.immortal.konfero_backend.infrastructure.conference.dto.response.ConferenceShortResponse;
import pl.immortal.konfero_backend.infrastructure.conference.dto.response.ConferenceSingleResponse;
import pl.immortal.konfero_backend.infrastructure.file.dto.FileMapper;
import pl.immortal.konfero_backend.infrastructure.file.dto.FileMapperImpl;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.response.LectureShortResponse;
import pl.immortal.konfero_backend.model.entity.Conference;
import pl.immortal.konfero_backend.model.entity.Lecture;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ConferenceMapper {
	@Mapping(target = "finished", ignore = true)
	@Mapping(target = "lectures", qualifiedByName = "lectureListToLectureShortResponseList")
	ConferenceSingleResponse map(Conference conference);

	@Mapping(target = "finished", ignore = true)
	@Mapping(target = "participants", ignore = true)
	@Mapping(target = "lectures", qualifiedByName = "lectureListToLectureShortResponseList")
	ConferenceSingleResponse guestMap(Conference conference);

	@Mapping(target = "finished", ignore = true)
	ConferenceShortResponse shortMap(Conference conference);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "organizer", ignore = true)
	@Mapping(target = "logo", ignore = true)
	@Mapping(target = "participants", ignore = true)
	@Mapping(target = "lectures", ignore = true)
	@Mapping(target = "photos", ignore = true)
	Conference map(ConferenceSingleRequest request);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "organizer", ignore = true)
	@Mapping(target = "logo", ignore = true)
	@Mapping(target = "participants", ignore = true)
	@Mapping(target = "lectures", ignore = true)
	@Mapping(target = "photos", ignore = true)
	void update(@MappingTarget Conference conference, ConferenceSingleRequest request);

	@Named("lectureListToLectureShortResponseList")
	static List<LectureShortResponse> lectureListToLectureShortResponseList(List<Lecture> list) {
		if (list == null) {
			return null;
		}

		List<LectureShortResponse> list1 = new ArrayList<>(list.size());
		for (Lecture lecture : list) {
			list1.add(lectureToLectureShortResponse(lecture));
		}

		return list1;
	}

	static LectureShortResponse lectureToLectureShortResponse(Lecture lecture) {
		if (lecture == null) {
			return null;
		}

		LectureShortResponse lectureShortResponse = new LectureShortResponse();
		FileMapper fileMapper = new FileMapperImpl();

		lectureShortResponse.setId(lecture.getId());
		lectureShortResponse.setName(lecture.getName());
		lectureShortResponse.setStartDateTime(lecture.getStartDateTime());
		lectureShortResponse.setDurationMinutes(lecture.getDurationMinutes());
		lectureShortResponse.setImage(fileMapper.map(lecture.getImage()));
		lectureShortResponse.setPlace(lecture.getPlace());
		lectureShortResponse.setInterestedAmount(lecture.getInterested().size());
		lectureShortResponse.setConferenceId(lecture.getConference().getId());
		lectureShortResponse.setConferenceName(lecture.getConference().getName());

		return lectureShortResponse;
	}

}
