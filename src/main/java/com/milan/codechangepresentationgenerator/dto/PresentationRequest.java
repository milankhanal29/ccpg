package com.milan.codechangepresentationgenerator.dto;

import com.milan.codechangepresentationgenerator.util.DiffResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PresentationRequest {
    private String repoName;
    private String commitId;
    private String emailToUse;
    private List<DiffResult> diffResults;
}
