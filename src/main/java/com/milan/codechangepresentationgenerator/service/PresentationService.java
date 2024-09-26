package com.milan.codechangepresentationgenerator.service;

import com.milan.codechangepresentationgenerator.dto.PresentationRequest;
import com.milan.codechangepresentationgenerator.model.Presentation;
import com.milan.codechangepresentationgenerator.util.DiffResult;
import org.springframework.core.io.Resource;

import java.util.List;

public interface PresentationService {
    Presentation generatePresentation(PresentationRequest request);


    String createPresentation(List<DiffResult> diffResults, String repoName, String commitSha,String emailToUse);
    byte[] getPresentation(Long id);

    void savePresentation(String presentationId);
    List<Presentation> getPresentationsForUserEmail(String userEmail);
    void deletePresentation(String id);
}
