package com.milan.codechangepresentationgenerator.service.impl;

import com.milan.codechangepresentationgenerator.service.NotificationService;
import com.milan.codechangepresentationgenerator.service.shared.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private MailService mailService;

    @Override
    public void sendEmailWithLink(String downloadLink, String repoFullName, String commitSha, String email) {
        try {
            String body = mailService.loadTemplate("templates/emailTemplete.html",
                    "{{repoFullName}}", repoFullName,
                    "{{commitSha}}", commitSha,
                    "{{downloadLink}}", downloadLink);

            mailService.sendEmail(email, "Your Code Changes Presentation is Ready", body);
        } catch (Exception e) {
            log.error("Failed to send email with link due to an error: {}", e.getMessage(), e);
        }
    }
}