package com.milan.codechangepresentationgenerator.service.impl;

import com.milan.codechangepresentationgenerator.model.Presentation;
import com.milan.codechangepresentationgenerator.repository.PresentationRepository;
import com.milan.codechangepresentationgenerator.service.DatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class DatabaseServiceImpl implements DatabaseService {

    @Autowired
    private PresentationRepository presentationRepository;

    @Override
    public void savePresentation(String presentationId, byte[] pptData, String userEmail, LocalDateTime createdDate) {
        Presentation presentation = new Presentation();
        presentation.setId(presentationId);
        log.info("Presentation id {}", presentationId);
        presentation.setUserEmail(userEmail);
        presentation.setData(pptData);
        presentation.setCreatedAt(createdDate);

        presentationRepository.save(presentation);
    }
}
