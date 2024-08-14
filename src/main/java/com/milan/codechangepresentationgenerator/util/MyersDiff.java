package com.milan.codechangepresentationgenerator.util;
import java.util.ArrayList;
import java.util.List;

public class MyersDiff {

    public DiffResult diff(String oldContent, String newContent) {
        List<String> oldLines = List.of(oldContent.split("\n"));
        List<String> newLines = List.of(newContent.split("\n"));

        List<String> changes = new ArrayList<>();
        int i = 0, j = 0;

        while (i < oldLines.size() && j < newLines.size()) {
            if (oldLines.get(i).equals(newLines.get(j))) {
                i++;
                j++;
            } else {
                changes.add("- Line " + (i + 1) + ": " + oldLines.get(i)); // Removed line
                changes.add("+ Line " + (j + 1) + ": " + newLines.get(j)); // Added line
                i++;
                j++;
            }
        }

        while (i < oldLines.size()) {
            changes.add("- Line " + (i + 1) + ": " + oldLines.get(i));
            i++;
        }

        while (j < newLines.size()) {
            changes.add("+ Line " + (j + 1) + ": " + newLines.get(j));
            j++;
        }

        return new DiffResult(changes);
    }
}
