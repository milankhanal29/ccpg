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
                    String oldContent = getFileContent(repository, previousCommitSha, fileName);
                    String newContent = getFileContent(repository, currentCommitSha, fileName);

                    if (oldContent == null || newContent == null) {
                        continue;
                    }

                    DiffResult result = compareFiles(oldContent, newContent);
                    result.setFileName(fileName);
                    results.add(result);
                    log.info("Differences in file '{}':\n{}", fileName, result);
                } catch (IOException e) {
                    log.error("Error processing file '{}': {}", fileName, e.getMessage(), e);
                    continue;
                }
            }

        } catch (IOException e) {
            log.error("Error processing the repository: {}", repoFullName, e);
            throw e;
        }
        return results;
    }

    private String getFileContent(GHRepository repository, String commitSha, String fileName) throws IOException {
        GHCommit commit = repository.getCommit(commitSha);
        for (GHCommit.File file : commit.getFiles()) {
            if (file.getFileName().equals(fileName)) {
                URL fileUrl = file.getRawUrl();
                return fetchFileContent(fileUrl);
            }
        }
        throw new IOException("File not found in commit: " + fileName);
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
