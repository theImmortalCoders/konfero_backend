package pl.immortal.konfero_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ConferenceSearchFields {
    private LocalDateTime startDateTimeFrom;
    private LocalDateTime startDateTimeTo;
    private String name;
    private List<Long> tagsIds;
    private Boolean canceled;
    private Integer participantsLimit;
    private Boolean verified;
    private Boolean participantsFull;
}
