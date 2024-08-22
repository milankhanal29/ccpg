package com.milan.codechangepresentationgenerator.service;

public interface NotificationService {
    void sendEmailWithLink(String downloadLink, String repoFullName, String commitSha, String email);
}
