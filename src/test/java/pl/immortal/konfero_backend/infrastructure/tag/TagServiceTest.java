package pl.immortal.konfero_backend.infrastructure.tag;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pl.immortal.konfero_backend.infrastructure.conference.ConferenceUtil;
import pl.immortal.konfero_backend.model.entity.Conference;
import pl.immortal.konfero_backend.model.entity.Tag;
import pl.immortal.konfero_backend.model.entity.User;
import pl.immortal.konfero_backend.model.entity.repository.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TagServiceTest {
	private final Tag tag = new Tag();
	private final User user = new User();
	private final Conference conference = new Conference();
	@Mock
	private ConferenceUtil conferenceUtil;
	private final TagRepository tagRepository = mock(TagRepository.class);
	@Spy
	private TagUtil tagUtil = new TagUtil(tagRepository);
	@InjectMocks
	private TagService tagService;

	@BeforeEach
	public void setUp() {
		tag.setId(1L);
		tag.setTagName("IT");

		conference.setId(1L);

		user.setId(1L);
		when(tagRepository.save(any(Tag.class))).thenReturn(tag);
		when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
		when(tagRepository.findAll()).thenReturn(new ArrayList<>(List.of(tag)));
		when(conferenceUtil.findAllByTag(tag)).thenReturn(new ArrayList<>(List.of(conference)));
	}

	@Test
	public void shouldAdd() {
		tagService.add("IT");

		verify(tagRepository, times(1)).save(any(Tag.class));
	}

	@Test
	public void shouldDelete() {
		tagService.delete(1L);

		verify(tagRepository, times(1)).deleteById(1L);
	}

	@Test
	public void shouldGetAll() {
		assertEquals(List.of(tag), tagService.getAll());
	}
}
