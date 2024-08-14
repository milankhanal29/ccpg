package com.milan.codechangepresentationgenerator.service.impl;

import com.milan.codechangepresentationgenerator.service.DiffService;
import com.milan.codechangepresentationgenerator.service.NotificationService;
import com.milan.codechangepresentationgenerator.service.PresentationService;
import com.milan.codechangepresentationgenerator.service.WebhookService;
import com.milan.codechangepresentationgenerator.util.DiffResult;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class WebhookServiceImpl implements WebhookService {
    @Autowired
    private DiffService diffService;

    @Autowired
    private PresentationService presentationService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private GitHub gitHub; // Make sure GitHub is correctly injected
    @Override
    public void processWebhook(Map<String, Object> payload) throws IOException {
        log.info("Received GitHub webhook with payload");
//        log.info("Received GitHub webhook with payload: {}", payload);

        if (!(payload instanceof Map)) {
            log.error("Payload is not a Map.");
            return;
        }

        Map<String, Object> repoMap = (Map<String, Object>) payload.get("repository");
        if (repoMap == null || !(repoMap instanceof Map)) {
            log.error("Repository data is missing or not a Map.");
            return;
        }

        String repoFullName = (String) repoMap.get("full_name");
        if (repoFullName == null || !repoFullName.contains("/")) {
            throw new IllegalArgumentException("Repository name must be in format owner/repo");
        }

        String repoName = (String) repoMap.get("name");
        String repoOwner = (String) ((Map<String, Object>) repoMap.get("owner")).get("login");
        String repoUrl = (String) repoMap.get("html_url");

        log.info("Received GitHub webhook for repository: Name = {}, Owner = {}, URL = {}",
                repoName, repoOwner, repoUrl);

        String currentCommitSha = (String) payload.get("after");
        String previousCommitSha = (String) payload.get("before");

        if (currentCommitSha == null || previousCommitSha == null) {
            throw new IllegalArgumentException("Missing 'after' or 'before' commit SHA in payload");
        }
        GHRepository ghRepository = gitHub.getRepository(repoFullName);
        GHCommit currentCommit = ghRepository.getCommit(currentCommitSha);
        GHCommit previousCommit = ghRepository.getCommit(previousCommitSha);

        List<GHCommit.File> files = currentCommit.getFiles();

        List<String> fileNames = files.stream()
                .map(GHCommit.File::getFileName)
                .toList();

        List<DiffResult> diffResults = diffService.computeDiff(fileNames, previousCommitSha, currentCommitSha,repoFullName);

        String presentationId = presentationService.createPresentation(diffResults, repoFullName, currentCommitSha);
        presentationService.savePresentation(presentationId);

        notificationService.sendEmail("developer@example.com","", "Download your presentation at /download/" + presentationId);
        log.info("Webhook processed successfully for repository: {}", repoFullName);
    }

}
