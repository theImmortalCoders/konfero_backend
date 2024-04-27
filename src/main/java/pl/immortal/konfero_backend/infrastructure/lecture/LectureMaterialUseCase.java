package pl.immortal.konfero_backend.infrastructure.lecture;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import pl.immortal.konfero_backend.infrastructure.auth.UserUtil;
import pl.immortal.konfero_backend.infrastructure.file.FileUtil;
import pl.immortal.konfero_backend.model.entity.File;
import pl.immortal.konfero_backend.model.entity.Lecture;
import pl.immortal.konfero_backend.model.entity.User;

import java.util.ArrayList;

@Component
@AllArgsConstructor
public class LectureMaterialUseCase {
	private final FileUtil fileUtil;
	private final UserUtil userUtil;
	private final LectureUtil lectureUtil;

	public void add(Long lectureId, Long materialId) {
		User user = userUtil.getCurrentUser();
		Lecture lecture = lectureUtil.getByIdAsOrganizerOrAdminOrLecturer(lectureId, user);
		File material = fileUtil.getFileById(materialId);

		checkMaterialOwnership(material, user);

		lecture.getMaterials().add(material);
		lecture.setMaterials(new ArrayList<>(lecture.getMaterials()
						.stream()
								.distinct().toList()));
		lectureUtil.save(lecture);
	}

	public void remove(Long lectureId, Long materialId) {
		User user = userUtil.getCurrentUser();
		Lecture lecture = lectureUtil.getByIdAsOrganizerOrAdminOrLecturer(lectureId, user);
		File material = fileUtil.getFileById(materialId);

		lecture.getMaterials().remove(material);

		lectureUtil.save(lecture);
	}

	//

	private static void checkMaterialOwnership(File material, User user) {
		if (!material.getAuthor().equals(user)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not owner of the material.");
		}
	}
}
