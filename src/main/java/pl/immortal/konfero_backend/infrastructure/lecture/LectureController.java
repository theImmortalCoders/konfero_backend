package pl.immortal.konfero_backend.infrastructure.lecture;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Lecture", description = "Lecture CRUD")
@RequestMapping("/api/lecture")
@AllArgsConstructor
public class LectureController {
    private final LectureService lectureService;


}
