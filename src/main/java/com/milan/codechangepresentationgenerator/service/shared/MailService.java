package com.milan.codechangepresentationgenerator.service.shared;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private Integer port;
    @Value("${spring.mail.username}")
    private String admin_email;
    @Value("${spring.mail.password}")
    private String password;
    private final ExecutorService executorService= Executors.newCachedThreadPool();
    public void sendEmail(String toEmail, String subject, String body) {
        executorService.submit(() -> {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(host);
            mailSender.setPort(port);
            mailSender.setUsername(admin_email);
            mailSender.setPassword(password);
            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = null;
            try {
                helper = new MimeMessageHelper(message, true);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            try {
                helper.setTo(toEmail);
                helper.setSubject(subject);
                helper.setText(body, true); // Use true to indicate HTML content
                mailSender.send(message);
                log.info("Message sent successfully");
            } catch (MessagingException e) {
                log.error("Failed to send email: " + e.getMessage(), e);
            }
        });
    }
}