package pl.immortal.konfero_backend.infrastructure.lecture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pl.immortal.konfero_backend.infrastructure.auth.UserUtil;
import pl.immortal.konfero_backend.infrastructure.file.FileUtil;
import pl.immortal.konfero_backend.model.entity.File;
import pl.immortal.konfero_backend.model.entity.Lecture;
import pl.immortal.konfero_backend.model.entity.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LectureMaterialUseCaseTest {
    private final Lecture lecture = new Lecture();
    private final File file = new File();
    private final User user = new User();
    @Mock
    private FileUtil fileUtil;
    @Mock
    private UserUtil userUtil;
    @Mock
    private LectureUtil lectureUtil;
    @InjectMocks
    private LectureMaterialUseCase lectureMaterialUseCase;

    @BeforeEach
    public void setUp() {
        user.setId(1L);

        file.setId(1L);
        file.setAuthor(user);

        lecture.setId(1L);
        lecture.setLecturers(new ArrayList<>(List.of(user)));

        when(userUtil.getCurrentUser()).thenReturn(user);
        when(lectureUtil.getByIdWithAuthorityCheck(any(Long.class), any(User.class))).thenReturn(lecture);
        when(fileUtil.getFileById(1L)).thenReturn(file);
    }

    @Test
    public void shouldAddMaterial() {
        lectureMaterialUseCase.add(1L, 1L);

        assertEquals(List.of(file), lecture.getMaterials());
        verify(lectureUtil, times(1)).save(lecture);
    }

    @Test
    public void shouldDeleteMaterial() {
        lectureMaterialUseCase.remove(1L, 1L);

        assertEquals(new ArrayList<>(), lecture.getMaterials());
        verify(lectureUtil, times(1)).save(lecture);
    }
}
