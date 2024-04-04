package pl.immortal.konfero_backend.infrastructure.mail;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class MailTemplateService {
    private final MailSendingUtil emailSendingUtil;

    public void sendWelcomeEmail(String email, String name) {
        var message = new Context();
        message.setVariable("email", email);
        message.setVariable("name", name);
        emailSendingUtil.sendEmailWithHtmlTemplate(email, "Witaj w Konfero, " + name + "!", "welcome", message);
    }
}
