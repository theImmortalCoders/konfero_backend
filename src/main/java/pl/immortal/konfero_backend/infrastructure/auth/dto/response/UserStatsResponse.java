package pl.immortal.konfero_backend.infrastructure.auth.dto.response;

import lombok.Data;

@Data
public class UserStatsResponse {
	private long organizersAmount;
	private long conferencesAmount;
	private long usersAmount;
}
