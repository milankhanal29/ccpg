package com.milan.codechangepresentationgenerator.dto;

import com.milan.codechangepresentationgenerator.model.ChangeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MethodChangeDto {

    private String methodName;
    private ChangeType changeType;
}
