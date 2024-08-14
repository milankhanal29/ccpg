package com.milan.codechangepresentationgenerator.service.impl;

import com.milan.codechangepresentationgenerator.dto.PresentationRequest;
import com.milan.codechangepresentationgenerator.model.Presentation;
import com.milan.codechangepresentationgenerator.repository.PresentationRepository;
import com.milan.codechangepresentationgenerator.service.PresentationService;
import com.milan.codechangepresentationgenerator.service.shared.MailService;
import com.milan.codechangepresentationgenerator.util.DiffResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.sl.usermodel.VerticalAlignment;
import org.apache.poi.xslf.usermodel.*;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
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
    private MailService mailService;
    @Autowired
    private PresentationRepository presentationRepository;
    @Autowired
    private  GitHub github;

    @Override
    public Presentation generatePresentation(PresentationRequest request) {
        return null;
    }
    @Override
    public String createPresentation(List<DiffResult> diffResults, String repoFullName, String commitSha) {
        XMLSlideShow ppt = new XMLSlideShow();

        // Create the first slide
        XSLFSlide firstSlide = ppt.createSlide();

        // Title
        XSLFTextShape title = firstSlide.createTextBox();
        title.setText("Code Changes Presentation");
        title.setAnchor(new Rectangle(50, 50, 600, 50)); // Positioning the title

        XSLFTextParagraph titleParagraph = title.addNewTextParagraph();
        XSLFTextRun titleRun = titleParagraph.addNewTextRun();
        titleRun.setText("Code Changes Presentation");
        titleRun.setFontColor(new Color(0, 102, 204)); // Dark blue color for title
        titleRun.setFontSize(24.0); // Title font size

        // Author Info
        XSLFTextShape authorInfo = firstSlide.createTextBox();
        authorInfo.setAnchor(new Rectangle(50, 120, 600, 100)); // Positioning author info

        XSLFTextParagraph authorParagraph = authorInfo.addNewTextParagraph();
        XSLFTextRun authorRun = authorParagraph.addNewTextRun();
        authorRun.setText("Repository: " + repoFullName +
                "\nCommit SHA: " + commitSha +
                "\nDate: " + LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")) +
                "\nDay: " + LocalDate.now().getDayOfWeek());
        authorRun.setFontColor(new Color(102, 102, 102)); // Gray color for author info
        authorRun.setFontSize(14.0); // Author info font size

        // Add slides for each file diff
        for (DiffResult result : diffResults) {
            XSLFSlide diffSlide = ppt.createSlide();

            // Diff Title
            XSLFTextShape diffTitle = diffSlide.createTextBox();
            diffTitle.setAnchor(new Rectangle(50, 50, 600, 50)); // Positioning the title

            XSLFTextParagraph diffTitleParagraph = diffTitle.addNewTextParagraph();
            XSLFTextRun diffTitleRun = diffTitleParagraph.addNewTextRun();
            diffTitleRun.setText("File: " + result.getFileName());
            diffTitleRun.setFontColor(new Color(0, 102, 204)); // Dark blue color for diff title
            diffTitleRun.setFontSize(20.0); // Diff title font size

            // Diff Content
            XSLFTextShape diffContent = diffSlide.createTextBox();
            diffContent.setAnchor(new Rectangle(50, 120, 600, 400)); // Adjust text box size

            for (String change : result.getChanges()) {
                XSLFTextParagraph diffContentParagraph = diffContent.addNewTextParagraph();
                XSLFTextRun diffContentRun = diffContentParagraph.addNewTextRun();

                if (change.startsWith("+")) {
                    diffContentRun.setText(change);
                    diffContentRun.setFontColor(Color.GREEN); // Green for additions
                } else if (change.startsWith("-")) {
                    diffContentRun.setText(change);
                    diffContentRun.setFontColor(Color.RED); // Red for deletions
                } else {
                    diffContentRun.setText(change);
                    diffContentRun.setFontColor(Color.BLACK); // Black for unchanged lines
                }

                diffContentRun.setFontSize(12.0); // Diff content font size
            }

            diffContent.setTextAutofit(XSLFTextShape.TextAutofit.NORMAL); // Autofit text
            diffContent.setVerticalAlignment(VerticalAlignment.TOP);
        }

        String presentationId = UUID.randomUUID().toString();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ppt.write(out);
            byte[] pptData = out.toByteArray();
            saveToDatabase(presentationId, pptData);

            String downloadLink = "http://localhost:8080/api/presentations/download/" + presentationId;
            sendEmailWithLink(downloadLink, repoFullName, commitSha);
        } catch (IOException e) {
            log.error("Error creating presentation", e);
        }

        return presentationId;
    }

    private void sendEmailWithLink(String downloadLink, String repoFullName, String commitSha) {
        try {
            // Retrieve the repository and commit
            GHRepository repository = github.getRepository(repoFullName);
            GHCommit commit = repository.getCommit(commitSha);

            // Extract the author's email from the commit
            String authorEmail = commit.getCommitter().getEmail();
            String subject = "Your Code Changes Presentation is Ready";
            String body = "Your presentation has been generated. You can download it using the following link:\n" + downloadLink;

            // Send the email to the extracted author email
            mailService.sendEmail(authorEmail, subject, body);
        } catch (IOException e) {
            log.error("Failed to send email with link due to an error: {}", e.getMessage(), e);
        }
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
    private void sss(String presentationId, byte[] pptData) {
        Presentation presentation = new Presentation();
        presentation.setId(presentationId);
        log.info("Presentation id {}", presentationId);
        presentation.setData(pptData);

        presentationRepository.save(presentation);
    }//jkkj

    @Override
    public byte[] getPresentation(Long id) {
        return new byte[0];
    }

    @Override
    public void savePresentation(String presentationId) {
//.hdjhdjduhuihuiehui
    }
}
