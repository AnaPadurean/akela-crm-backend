package com.example.akela.swim.crm.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${app-mail.from}")
    private String from;

    public void sendPasswordReset(String to, String resetLink) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(to);
        msg.setSubject("Resetare parolă");
        msg.setText(
                "Ai cerut resetarea parolei.\n\n" +
                        "Link (valabil o perioadă limitată):\n" +
                        resetLink + "\n\n" +
                        "Dacă nu ai cerut tu, ignoră acest email."
        );
        mailSender.send(msg);
    }
}
