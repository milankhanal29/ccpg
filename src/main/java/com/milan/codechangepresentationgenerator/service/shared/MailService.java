package com.milan.codechangepresentationgenerator.service.shared;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void sendEmail(String toEmail, String subject, String body) {
        executorService.submit(() -> {
            try {
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(toEmail);
                helper.setSubject(subject);
                helper.setText(body, true); // Use true to indicate HTML content
                javaMailSender.send(message);
                log.info("Message sent successfully to {}", toEmail);
            } catch (MessagingException e) {
                log.error("Failed to send email: {}", e.getMessage(), e);
            }
        });
    }

    public String loadTemplate(String templatePath, String... placeholders) {
        try {
            Resource resource = new ClassPathResource(templatePath);
            String template = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

            // Replace placeholders in the template
            for (int i = 0; i < placeholders.length; i += 2) {
                String placeholder = placeholders[i];
                String value = placeholders[i + 1];
                template = template.replace(placeholder, value);
            }

            return template;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load email template", e);
        }
    }
}
