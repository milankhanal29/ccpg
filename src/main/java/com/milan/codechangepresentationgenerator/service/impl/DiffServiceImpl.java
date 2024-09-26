package com.milan.codechangepresentationgenerator.service.impl;

import com.milan.codechangepresentationgenerator.service.DiffService;
import com.milan.codechangepresentationgenerator.util.DiffResult;
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
        List<String> oldLines = Arrays.asList(oldContent.split("\n"));
        List<String> newLines = Arrays.asList(newContent.split("\n"));
        List<String> changes = calculateDiff(oldLines, newLines);

        DiffResult result = new DiffResult();
        result.setChanges(changes);
        return result;
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
            GHCommit currentCommit = repository.getCommit(currentCommitSha);

            for (String fileName : fileNames) {
                try {
                    // Fetch the last commit where the file was modified, before the current commit
                    GHCommit lastModifiedCommit = findLastModifiedCommitBefore(repository, currentCommit, fileName);

                    // Fetch content for the file in the last modified commit and current commit
                    String oldContent = getFileContent(repository, lastModifiedCommit, fileName);
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
                        result.setAddedContent(Arrays.asList(newContent.split("\n")));
                        results.add(result);
                        log.info("File '{}' was added:\n{}", fileName, result);
                    } else if (oldContent != null && newContent == null) {
                        // File was deleted in the current commit
                        DiffResult result = new DiffResult();
                        result.setFileName(fileName);
                        result.setRemovedContent(Arrays.asList(oldContent.split("\n")));
                        results.add(result);
                        log.info("File '{}' was deleted:\n{}", fileName, result);
                    }
                } catch (IOException e) {
                    log.error("Error processing file '{}': {}", fileName, e.getMessage(), e);
                }
            }

        } catch (IOException e) {
            log.error("Error processing the repository: {}", repoFullName, e);
            throw e;
        }
        return results;
    }

    private List<String> calculateDiff(List<String> oldLines, List<String> newLines) {
        List<String> changes = new ArrayList<>();

        int oldIndex = 0;
        int newIndex = 0;

        while (oldIndex < oldLines.size() || newIndex < newLines.size()) {
            if (oldIndex < oldLines.size() && newIndex < newLines.size()) {
                // Both lines exist, check for equality
                if (oldLines.get(oldIndex).equals(newLines.get(newIndex))) {
                    oldIndex++;
                    newIndex++;
                } else {
                    // Check for the next matching line in newLines
                    if (newIndex + 1 < newLines.size() && oldLines.get(oldIndex).equals(newLines.get(newIndex + 1))) {
                        // Line was added
                        changes.add("+ " + newLines.get(newIndex) + " : line " + (newIndex + 1));
                        newIndex++;
                    } else if (oldIndex + 1 < oldLines.size() && newLines.get(newIndex).equals(oldLines.get(oldIndex + 1))) {
                        // Line was removed
                        changes.add("- " + (oldIndex + 1) + "-" + (oldIndex + 2) + " : " + oldLines.get(oldIndex));
                        oldIndex++;
                    } else {
                        // Both lines are different; record removal and addition
                        changes.add("- " + (oldIndex + 1) + " : " + oldLines.get(oldIndex));
                        changes.add("+ " + newLines.get(newIndex) + " : line " + (newIndex + 1) );
                        oldIndex++;
                        newIndex++;
                    }
                }
            } else if (oldIndex < oldLines.size()) {
                changes.add("- " + (oldIndex + 1) + " : " + oldLines.get(oldIndex));
                oldIndex++;
            } else if (newIndex < newLines.size()) {
                changes.add("+ " + newLines.get(newIndex) + " : line " + (newIndex + 1) );
                newIndex++;
            }
        }
        return changes;
    }

    private GHCommit findLastModifiedCommitBefore(GHRepository repository, GHCommit currentCommit, String fileName) throws IOException {
        GHCommit commit = currentCommit;
        List<GHCommit> parents = commit.getParents();
        if (!parents.isEmpty()) {
            commit = parents.get(0);  // Move to the previous commit
        }
        while (commit != null) {
            log.info("Checking commit '{}' for file '{}'", commit.getSHA1(), fileName);
            for (GHCommit.File file : commit.getFiles()) {
                if (file.getFileName().equals(fileName)) {
                    log.info("Found last modification of '{}' in commit '{}'", fileName, commit.getSHA1());
                    return commit;
                }
            }
            parents = commit.getParents();
            commit = (parents.isEmpty()) ? null : parents.get(0);
        }
        log.warn("No previous commit found where the file '{}' was modified", fileName);
        return null;
    }

    private String getFileContent(GHRepository repository, GHCommit commit, String fileName) throws IOException {
        log.info("Fetching content for file '{}' in commit '{}'", fileName, commit.getSHA1());
        for (GHCommit.File file : commit.getFiles()) {
            if (file.getFileName().equals(fileName)) {
                URL fileUrl = file.getRawUrl();
                log.info("File URL: {}", fileUrl);
                return fetchFileContent(fileUrl);
            }
        }
        log.warn("No file content found for '{}'", fileName);
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
