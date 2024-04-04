package pl.immortal.konfero_backend.infrastructure.lecture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LectureServiceTest {
    @InjectMocks
    private LectureService lectureService;

    @BeforeEach
    public void setUp() {

    }
}
