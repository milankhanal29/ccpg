package com.milan.codechangepresentationgenerator.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ChangeMetrics {
    private int linesAdded;
    private int linesDeleted;
}
