package pl.immortal.konfero_backend.infrastructure.lecture.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.immortal.konfero_backend.infrastructure.auth.dto.response.UserShortResponse;
import pl.immortal.konfero_backend.infrastructure.file.dto.FileMapper;
import pl.immortal.konfero_backend.infrastructure.file.dto.FileMapperImpl;
import pl.immortal.konfero_backend.infrastructure.file.dto.FileSingleResponse;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.request.LectureSingleOrganizerRequest;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.response.LectureShortResponse;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.response.LectureSingleResponse;
import pl.immortal.konfero_backend.model.entity.Conference;
import pl.immortal.konfero_backend.model.entity.File;
import pl.immortal.konfero_backend.model.entity.Lecture;
import pl.immortal.konfero_backend.model.entity.User;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface LectureMapper {
	default LectureShortResponse shortMap(Lecture lecture) {
		var response = new LectureShortResponse();
		var fileMapper = new FileMapperImpl();

		response.setId(lecture.getId());
		response.setName(lecture.getName());
		response.setStartDateTime(lecture.getStartDateTime());
		response.setDurationMinutes(lecture.getDurationMinutes());
		response.setPlace(lecture.getPlace());
		response.setImage(fileMapper.map(lecture.getImage()));
		response.setInterestedAmount(lecture.getInterested().size());
		response.setConferenceName(lecture.getConference().getName());
		response.setConferenceName(lecture.getConference().getName());
		response.setConferenceId(lecture.getConference().getId());

		return response;
	}

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

	default LectureSingleResponse map(Lecture lecture) {
		if (lecture == null) {
			return null;
		}

		LectureSingleResponse lectureSingleResponse = new LectureSingleResponse();

		lectureSingleResponse.setFormat(lecture.getConference().getFormat());
		lectureSingleResponse.setConferenceId(lectureConferenceId(lecture));
		lectureSingleResponse.setId(lecture.getId());
		lectureSingleResponse.setName(lecture.getName());
		lectureSingleResponse.setDescription(lecture.getDescription());
		lectureSingleResponse.setStartDateTime(lecture.getStartDateTime());
		lectureSingleResponse.setDurationMinutes(lecture.getDurationMinutes());
		lectureSingleResponse.setImage(fileToFileSingleResponse(lecture.getImage()));
		lectureSingleResponse.setLecturers(userListToUserShortResponseList(lecture.getLecturers()));
		lectureSingleResponse.setMaterials(fileListToFileSingleResponseList(lecture.getMaterials()));
		lectureSingleResponse.setInterested(userListToUserShortResponseList(lecture.getInterested()));
		lectureSingleResponse.setPlace(lecture.getPlace());

		return lectureSingleResponse;
	}

	private Long lectureConferenceId(Lecture lecture) {
		if (lecture == null) {
			return null;
		}
		Conference conference = lecture.getConference();
		if (conference == null) {
			return null;
		}
		return conference.getId();
	}

	private FileSingleResponse fileToFileSingleResponse(File file) {
		if (file == null) {
			return null;
		}

		FileMapper fileMapper = new FileMapperImpl();

		return fileMapper.map(file);
	}

	private UserShortResponse userToUserShortResponse(User user) {
		if (user == null) {
			return null;
		}

		UserShortResponse userShortResponse = new UserShortResponse();

		userShortResponse.setId(user.getId());
		userShortResponse.setUsername(user.getUsername());
		userShortResponse.setEmail(user.getEmail());
		userShortResponse.setPhoto(user.getPhoto());
		userShortResponse.setVerified(user.isVerified());

		return userShortResponse;
	}

	private List<UserShortResponse> userListToUserShortResponseList(List<User> list) {
		if (list == null) {
			return null;
		}

		List<UserShortResponse> list1 = new ArrayList<UserShortResponse>(list.size());
		for (User user : list) {
			list1.add(userToUserShortResponse(user));
		}

		return list1;
	}

	private List<FileSingleResponse> fileListToFileSingleResponseList(List<File> list) {
		if (list == null) {
			return null;
		}

		List<FileSingleResponse> list1 = new ArrayList<FileSingleResponse>(list.size());
		for (File file : list) {
			list1.add(fileToFileSingleResponse(file));
		}

		return list1;
	}
}
