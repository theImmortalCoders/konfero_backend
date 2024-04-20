package pl.immortal.konfero_backend.infrastructure.mail;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class MailTemplateService {
	private final MailSendingUtil emailSendingUtil;

	@Async
	public void sendWelcomeEmail(String email, String name) {
		var message = new Context();

		message.setVariable("email", email);
		message.setVariable("name", name);

		emailSendingUtil.sendEmailWithHtmlTemplate(
				email,
				"Witaj w Konfero, " + name + "!", "welcome",
				message
		);
	}

	@Async
	public void sendConferenceInfoEmail(
			String email,
			String conferenceName,
			LocalDateTime conferenceDateTime,
			String messageText
	) {
		var message = new Context();

		message.setVariable("email", email);
		message.setVariable("conferenceName", conferenceName);
		message.setVariable("conferenceDateTime", conferenceDateTime);
		message.setVariable("messageText", messageText);

		emailSendingUtil.sendEmailWithHtmlTemplate(
				email,
				"Informacje o konferencji '" + conferenceName + "'", "conference_info",
				message
		);
	}
}
