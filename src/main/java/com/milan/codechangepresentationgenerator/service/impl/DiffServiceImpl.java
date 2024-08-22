package com.milan.codechangepresentationgenerator.service.impl;

import com.milan.codechangepresentationgenerator.service.DiffService;
import com.milan.codechangepresentationgenerator.util.DiffResult;
import com.milan.codechangepresentationgenerator.util.MyersDiff;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiffServiceImpl implements DiffService {

    @Value("${github.token}")
    private String githubToken;

    private final GitHub github;

    @Override
    public DiffResult compareFiles(String oldContent, String newContent) {
        MyersDiff myersDiff = new MyersDiff();
        return myersDiff.diff(oldContent, newContent);
    }

    @Override
    public List<DiffResult> compareFileVersions(List<String> oldContents, List<String> newContents) {
        List<DiffResult> results = new ArrayList<>();
        for (int i = 0; i < oldContents.size(); i++) {
            String oldContent = oldContents.get(i);
            String newContent = newContents.get(i);
            results.add(compareFiles(oldContent, newContent));
        }
        return results;
    }

    @Override
    public List<DiffResult> computeDiff(List<String> fileNames, String previousCommitSha, String currentCommitSha, String repoFullName) throws IOException {
        List<DiffResult> results = new ArrayList<>();
        try {
            GHRepository repository = github.getRepository(repoFullName);

            GHCommit previousCommit = repository.getCommit(previousCommitSha);
            GHCommit currentCommit = repository.getCommit(currentCommitSha);

            for (String fileName : fileNames) {
                try {
                    // Fetch content for the file in both commits
                    String oldContent = getFileContent(repository, previousCommit, fileName);
                    String newContent = getFileContent(repository, currentCommit, fileName);

                    // If the file exists in both commits, compute the diff
                    if (oldContent != null && newContent != null) {
                        DiffResult result = compareFiles(oldContent, newContent);
                        result.setFileName(fileName);
                        results.add(result);
                        log.info("Differences in file '{}':\n{}", fileName, result);
                    } else if (oldContent == null && newContent != null) {
                        // File was added in the current commit
                        DiffResult result = new DiffResult();
                        result.setFileName(fileName);
                        result.setAddedContent(Arrays.asList(newContent.split("\n")));                        results.add(result);
                        log.info("File '{}' was added:\n{}", fileName, result);
                    } else if (oldContent != null && newContent == null) {
                        // File was deleted in the current commit
                        DiffResult result = new DiffResult();
                        result.setFileName(fileName);
                        result.setRemovedContent(Arrays.asList(oldContent.split("\n")));                        results.add(result);
                        log.info("File '{}' was deleted:\n{}", fileName, result);
                    }
                } catch (IOException e) {
                    log.error("Error processing file '{}': {}", fileName, e.getMessage(), e);
                }
            }

            // Additional handling for new files in the current commit not present in previous
            for (GHCommit.File file : currentCommit.getFiles()) {
                String fileName = file.getFileName();
                if (!fileNames.contains(fileName)) {
                    String newContent = getFileContent(repository, currentCommit, fileName);
                    if (newContent != null) {
                        DiffResult result = new DiffResult();
                        result.setFileName(fileName);
                        result.setAddedContent(Arrays.asList(newContent.split("\n")));                        results.add(result);
                        log.info("New file '{}' added in current commit:\n{}", fileName, result);
                    }
                }
            }

        } catch (IOException e) {
            log.error("Error processing the repository: {}", repoFullName, e);
            throw e;
        }
        return results;
    }

    private String getFileContent(GHRepository repository, GHCommit commit, String fileName) throws IOException {
        for (GHCommit.File file : commit.getFiles()) {
            if (file.getFileName().equals(fileName)) {
                URL fileUrl = file.getRawUrl();
                return fetchFileContent(fileUrl);
            }
        }
        return null;
    }

    private String fetchFileContent(URL fileUrl) throws IOException {
        StringBuilder content = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            log.error("Failed to fetch content from URL: {}", fileUrl, e);
            throw e;
        } finally {
            connection.disconnect();
        }
        return content.toString();
    }
}
