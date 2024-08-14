package com.milan.codechangepresentationgenerator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplexityMetrics {
    private int oldComplexity;
    private int newComplexity;
}
