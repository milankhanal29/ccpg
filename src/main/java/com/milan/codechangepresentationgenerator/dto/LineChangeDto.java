package com.milan.codechangepresentationgenerator.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineChangeDto {
    private int lineNumber;
    private String oldLine;
    private String newLine;
}
