package com.milan.codechangepresentationgenerator.util;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class DiffResult {
    private List<String> changes;
    private String fileName;

    public DiffResult(List<String> changes) {
        this.changes = changes;
    }

    @Override
    public String toString() {
        return String.join("\n", changes);
    }
}
