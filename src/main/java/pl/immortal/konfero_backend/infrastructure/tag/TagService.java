package pl.immortal.konfero_backend.infrastructure.tag;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.immortal.konfero_backend.infrastructure.conference.ConferenceUtil;
import pl.immortal.konfero_backend.model.entity.Conference;
import pl.immortal.konfero_backend.model.entity.Tag;
import pl.immortal.konfero_backend.model.entity.repository.TagRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class TagService {
	private final TagUtil tagUtil;
	private final TagRepository tagRepository;
	private final ConferenceUtil conferenceUtil;

	void add(String tagName) {
		Tag tag = new Tag();
		tag.setTagName(tagName);
		save(tag);
	}

	void delete(Long tagId) {
		Tag tag = tagUtil.getById(tagId);

		List<Conference> conferences = conferenceUtil.findAllByTag(tag);
		for (Conference conference : conferences) {
			conference.getTags().remove(tag);
			conferenceUtil.save(conference);
		}

		tagRepository.deleteById(tagId);
	}

	public List<Tag> getAll() {
		return tagRepository.findAll();
	}

	//

	private void save(Tag tag) {
		Option.of(tagRepository.save(tag))
				.getOrElseThrow(
						() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, tag.toString())
				);
	}
}
