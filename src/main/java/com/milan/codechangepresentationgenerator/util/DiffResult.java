package com.milan.codechangepresentationgenerator.util;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
public class DiffResult {
    private List<String> changes;
    private String fileName;
    private List<String> addedContent;
    private List<String> removedContent;
    public DiffResult(List<String> changes) {
        this.changes = changes;
    }

    @Override
    public String toString() {
        if (changes == null) {
            return "No changes";
        }
        return String.join("\n", changes.stream().map(Object::toString).collect(Collectors.toList()));
    }

}
