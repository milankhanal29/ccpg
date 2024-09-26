package com.milan.codechangepresentationgenerator.service;

import java.time.LocalDateTime;

public interface DatabaseService {
    void savePresentation(String presentationId, byte[] pptData, String email, LocalDateTime createdDate);
}
