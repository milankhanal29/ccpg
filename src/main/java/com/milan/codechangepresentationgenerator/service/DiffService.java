package com.milan.codechangepresentationgenerator.service;

import com.milan.codechangepresentationgenerator.util.DiffResult;
import org.kohsuke.github.GHCommit;

import java.io.IOException;
import java.util.List;

public interface DiffService {
    DiffResult compareFiles(String oldContent, String newContent);

    List<DiffResult> compareFileVersions(List<String> oldContents, List<String> newContents);

    List<DiffResult> computeDiff(List<String> fileNames, String previousCommitSha, String currentCommitSha,String repoFullName) throws IOException;
}
