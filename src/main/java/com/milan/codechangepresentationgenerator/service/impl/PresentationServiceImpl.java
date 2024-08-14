package com.milan.codechangepresentationgenerator.service.impl;

import com.milan.codechangepresentationgenerator.dto.PresentationRequest;
import com.milan.codechangepresentationgenerator.model.Presentation;
import com.milan.codechangepresentationgenerator.repository.PresentationRepository;
import com.milan.codechangepresentationgenerator.service.PresentationService;
import com.milan.codechangepresentationgenerator.util.DiffResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.sl.usermodel.VerticalAlignment;
import org.apache.poi.xslf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
@Slf4j
@Service
public class PresentationServiceImpl implements PresentationService {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private PresentationRepository presentationRepository;
    @Override
    public Presentation generatePresentation(PresentationRequest request) {
        return null;
    }

    @Override
    public String createPresentation(List<DiffResult> diffResults, String repoFullName, String commitSha) {
        XMLSlideShow ppt = new XMLSlideShow();

        // Create the first slide (title slide)
        XSLFSlide firstSlide = ppt.createSlide();

        XSLFTextShape title = firstSlide.createTextBox();
        title.setText("Code Changes Presentation");
        title.setAnchor(new Rectangle(50, 50, 600, 50));

        XSLFTextParagraph titleParagraph = title.addNewTextParagraph();
        XSLFTextRun titleRun = titleParagraph.addNewTextRun();
        titleRun.setText("Code Changes Presentation");
        titleRun.setFontColor(new Color(0, 102, 204));
        titleRun.setFontSize(24.0);

        // Author Info
        XSLFTextShape authorInfo = firstSlide.createTextBox();
        authorInfo.setAnchor(new Rectangle(50, 120, 600, 100));

        XSLFTextParagraph authorParagraph = authorInfo.addNewTextParagraph();
        XSLFTextRun authorRun = authorParagraph.addNewTextRun();
        authorRun.setText("Repository: " + repoFullName +
                "\nCommit SHA: " + commitSha +
                "\nDate: " + LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")) +
                "\nDay: " + LocalDate.now().getDayOfWeek());
        authorRun.setFontColor(new Color(102, 102, 102));
        authorRun.setFontSize(14.0);

        // Add slides for each file diff
        for (DiffResult result : diffResults) {
            XSLFSlide diffSlide = ppt.createSlide();

            // Diff Title
            XSLFTextShape diffTitle = diffSlide.createTextBox();
            diffTitle.setAnchor(new Rectangle(50, 50, 600, 50));

            XSLFTextParagraph diffTitleParagraph = diffTitle.addNewTextParagraph();
            XSLFTextRun diffTitleRun = diffTitleParagraph.addNewTextRun();
            diffTitleRun.setText("File: " + result.getFileName());
            diffTitleRun.setFontColor(new Color(0, 102, 204));
            diffTitleRun.setFontSize(20.0);

            // Diff Content
            XSLFTextShape diffContent = diffSlide.createTextBox();
            diffContent.setAnchor(new Rectangle(50, 120, 600, 400));

            // Iterate through the changes and color them accordingly
            for (String change : result.getChanges()) {
                XSLFTextParagraph diffContentParagraph = diffContent.addNewTextParagraph();
                XSLFTextRun diffContentRun = diffContentParagraph.addNewTextRun();

                if (change.startsWith("+")) {
                    diffContentRun.setFontColor(new Color(0, 128, 0)); // Green for added lines
                } else if (change.startsWith("-")) {
                    diffContentRun.setFontColor(new Color(255, 0, 0)); // Red for removed lines
                } else {
                    diffContentRun.setFontColor(new Color(0, 0, 0)); // Black for unchanged lines (if any)
                }

                diffContentRun.setText(change);
                diffContentRun.setFontSize(12.0);
            }

            diffContent.setTextAutofit(XSLFTextShape.TextAutofit.NORMAL);
            diffContent.setVerticalAlignment(VerticalAlignment.TOP);
        }

        String presentationId = UUID.randomUUID().toString();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ppt.write(out);
            byte[] pptData = out.toByteArray();
            saveToDatabase(presentationId, pptData);
        } catch (IOException e) {
            log.error("Error creating presentation", e);
        }

        return presentationId;
    }


    private String formatChanges(List<String> changes) {
        StringBuilder formattedChanges = new StringBuilder();
        for (String change : changes) {
            if (change.startsWith("+") || change.startsWith("-")) {
                formattedChanges.append(change).append("\n");
            }
        }
        return formattedChanges.toString();
    }


    private void saveToDatabase(String presentationId, byte[] pptData) {
        Presentation presentation = new Presentation();
        presentation.setId(presentationId);
        log.info("Presentation id {}", presentationId);
        presentation.setData(pptData);

        presentationRepository.save(presentation);
    }

    @Override
    public byte[] getPresentation(Long id) {
        return new byte[0];
    }

    @Override
    public void savePresentation(String presentationId) {

    }
}
