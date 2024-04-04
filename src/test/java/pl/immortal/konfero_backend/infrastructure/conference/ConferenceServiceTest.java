package pl.immortal.konfero_backend.infrastructure.conference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ConferenceServiceTest {
    @InjectMocks
    private ConferenceService conferenceService;

    @BeforeEach
    public void setUp() {

    }
}
