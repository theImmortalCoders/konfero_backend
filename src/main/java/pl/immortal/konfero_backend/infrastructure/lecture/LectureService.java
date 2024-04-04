package pl.immortal.konfero_backend.infrastructure.lecture;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.immortal.konfero_backend.infrastructure.lecture.dto.LectureMapper;
import pl.immortal.konfero_backend.model.entity.repository.LectureRepository;

@Service
@AllArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;
    private final LectureMapper lectureMapper;
}
