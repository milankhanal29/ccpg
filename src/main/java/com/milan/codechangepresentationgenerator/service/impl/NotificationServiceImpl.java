package com.milan.codechangepresentationgenerator.service.impl;

import com.milan.codechangepresentationgenerator.service.NotificationService;
import com.milan.codechangepresentationgenerator.service.shared.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private MailService mailService;

    @Override
    public void sendEmail(String toEmail, String subject, String body) {
        mailService.sendEmail(toEmail, subject, body);
    }
}
