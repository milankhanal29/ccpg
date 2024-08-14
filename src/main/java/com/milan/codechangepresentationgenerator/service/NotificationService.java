package com.milan.codechangepresentationgenerator.service;

public interface NotificationService {
    void sendEmail(String toEmail, String subject, String body);
}
