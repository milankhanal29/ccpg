package com.milan.codechangepresentationgenerator.service.impl;

import com.milan.codechangepresentationgenerator.dto.PresentationRequest;
import com.milan.codechangepresentationgenerator.model.Presentation;
import com.milan.codechangepresentationgenerator.repository.PresentationRepository;
import com.milan.codechangepresentationgenerator.service.DatabaseService;
import com.milan.codechangepresentationgenerator.service.NotificationService;
import com.milan.codechangepresentationgenerator.service.PresentationService;
import com.milan.codechangepresentationgenerator.service.shared.MailService;
import com.milan.codechangepresentationgenerator.util.DiffResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xslf.usermodel.*;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PresentationServiceImpl implements PresentationService {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private MailService mailService;
    @Autowired
    private PresentationRepository presentationRepository;
    @Autowired
    private GitHub github;
    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private NotificationService notificationService;

    @Override
    public Presentation generatePresentation(PresentationRequest request) {
        return null; // Implementation pending
    }
    @Override
    public String createPresentation(List<DiffResult> diffResults, String repoFullName, String commitSha, String email) {
        XMLSlideShow ppt = new XMLSlideShow();

        // Create the first slide with general information
        createGeneralInfoSlide(ppt, repoFullName, commitSha);

        // Process each file's changes and create slides
        for (DiffResult result : diffResults) {
            String fileName = result.getFileName();
            List<String> changes = result.getChanges();

            if (changes == null || changes.isEmpty()) {
                continue; // Skip files with no changes
            }

            createDiffSlides(ppt, fileName, changes);
        }

        // Save presentation and send email
        String presentationId = UUID.randomUUID().toString();
        savePresentationAndSendEmail(ppt, presentationId, repoFullName, commitSha, email);

        return presentationId;
    }

    private void createGeneralInfoSlide(XMLSlideShow ppt, String repoFullName, String commitSha) {
        XSLFSlide firstSlide = ppt.createSlide();

        // Title
        XSLFTextShape title = firstSlide.createTextBox();
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
    }

    private void createDiffSlides(XMLSlideShow ppt, String fileName, List<String> changes) {
        int maxLinesPerSlide = 15;
        int totalLines = changes.size();
        int slideIndex = 0;

        for (int i = 0; i < totalLines; ) {
            // Create a new slide for the diff
            XSLFSlide diffSlide = ppt.createSlide();

            // Diff Title
            XSLFTextShape diffTitle = diffSlide.createTextBox();
            diffTitle.setAnchor(new Rectangle(50, 50, 600, 50));
            XSLFTextParagraph diffTitleParagraph = diffTitle.addNewTextParagraph();
            XSLFTextRun diffTitleRun = diffTitleParagraph.addNewTextRun();
            diffTitleRun.setText("File: " + fileName + (slideIndex > 0 ? " (contd...)" : ""));
            diffTitleRun.setFontColor(new Color(0, 102, 204));
            diffTitleRun.setFontSize(20.0);

            // Diff Content
            XSLFTextShape diffContent = diffSlide.createTextBox();
            diffContent.setAnchor(new Rectangle(50, 120, 600, 400));

            // Add changes to the slide
            int start = i;
            while (i < totalLines && (i - start < maxLinesPerSlide)) {
                String currentChange = changes.get(i);
                addChangeToSlide(diffContent, currentChange);
                i++;
            }
            slideIndex++;
        }
    }
    private void addChangeToSlide(XSLFTextShape diffContent, String currentChange) {
        if (currentChange.startsWith("+ Lines") || currentChange.startsWith("- Lines")) {
            // Handle the line range first
            XSLFTextParagraph lineRangeParagraph = diffContent.addNewTextParagraph();
            XSLFTextRun lineRangeRun = lineRangeParagraph.addNewTextRun();

            // Set the text and styling for the line range
            lineRangeRun.setText(currentChange.split(":")[0] + ":");
            lineRangeRun.setFontSize(12.0);
            if (currentChange.startsWith("+")) {
                lineRangeRun.setFontColor(Color.GREEN);
            } else {
                lineRangeRun.setFontColor(Color.RED);
            }
            lineRangeRun.setBold(true);

            // Handle the actual content change within the line range
            String[] contentLines = currentChange.split(":\n");
            if (contentLines.length > 1) {
                String[] changes = contentLines[1].split("\n");
                for (String change : changes) {
                    XSLFTextParagraph contentParagraph = diffContent.addNewTextParagraph();
                    XSLFTextRun contentRun = contentParagraph.addNewTextRun();
                    contentRun.setText(change.trim());

                    char sign = change.trim().charAt(0);
                    if (sign == '+') {
                        contentRun.setFontColor(Color.GREEN);
                    } else if (sign == '-') {
                        contentRun.setFontColor(Color.RED);
                    } else {
                        contentRun.setFontColor(Color.BLACK);
                    }
                    contentRun.setFontSize(12.0);
                    contentRun.setBold(false);
                }
            }
        } else {
            // Handle single line changes without a range
            XSLFTextParagraph contentParagraph = diffContent.addNewTextParagraph();
            XSLFTextRun contentRun = contentParagraph.addNewTextRun();
            contentRun.setText(currentChange.trim());

            char sign = currentChange.trim().charAt(0);
            if (sign == '+') {
                contentRun.setFontColor(Color.GREEN);
            } else if (sign == '-') {
                contentRun.setFontColor(Color.RED);
            } else {
                contentRun.setFontColor(Color.BLACK);
            }
            contentRun.setFontSize(12.0);
            contentRun.setBold(false);
        }
    }


    private String formatChangeText(String currentChange) {
        // Example input: "+ Lines 4 to 4:\n + some added line"
        String[] parts = currentChange.split(" ", 3);

        if (parts.length >= 3 && parts[1].equals("Lines")) {
            String[] rangeParts = parts[2].split(" to ");
            if (rangeParts.length == 2 && rangeParts[0].equals(rangeParts[1])) {
                // If the line range is the same, format as a single line change
                return parts[0] + " Line " + rangeParts[0] + ":" + currentChange.substring(currentChange.indexOf(":") + 1).trim();
            }
        }
        return currentChange;
    }
    private void savePresentationAndSendEmail(XMLSlideShow ppt, String presentationId, String repoFullName, String commitSha, String email) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ppt.write(out);
            byte[] pptData = out.toByteArray();
            databaseService.savePresentation(presentationId, pptData);

            String downloadLink = "http://localhost:8080/api/presentations/download/" + presentationId;
            notificationService.sendEmailWithLink(downloadLink, repoFullName, commitSha, email);
        } catch (IOException e) {
            log.error("Error creating presentation: " + e.getMessage());
        }
    }
    @Override
    public byte[] getPresentation(Long id) {
        return new byte[0]; // Implementation pending
    }

    @Override
    public void savePresentation(String presentationId) {
        // Implementation pending
    }
}
