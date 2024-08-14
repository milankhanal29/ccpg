package com.milan.codechangepresentationgenerator.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommitDto {
    private String commitHash;
    private String author;
    private LocalDateTime timestamp;
}
