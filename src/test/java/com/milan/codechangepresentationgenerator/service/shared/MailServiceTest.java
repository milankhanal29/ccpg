package com.milan.codechangepresentationgenerator.service.shared;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.mockito.Mockito.*;

class MailServiceTest {

    private MailService mailService;
    private JavaMailSender javaMailSender;

    @BeforeEach
    public void setUp() {
        javaMailSender = Mockito.mock(JavaMailSender.class);
        mailService = new MailService(javaMailSender);
    }

    @Test
    public void testSendEmail() throws MessagingException {
        // Arrange
        String toEmail = "milankhanal29@gmail.com";
        String subject = "Test Subject";
        String body = "Test Body";

        // Act
        mailService.sendEmail(toEmail, subject, body);

        // Assert
        ArgumentCaptor<MimeMessage> messageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(javaMailSender, times(1)).send(messageCaptor.capture());

        MimeMessage sentMessage = messageCaptor.getValue();
        MimeMessageHelper helper = new MimeMessageHelper(sentMessage, true);
    }
}
