package com.milan.codechangepresentationgenerator.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
public class MyersDiff {

    public DiffResult diff(String oldContent, String newContent) {
        List<String> oldLines = List.of(oldContent.split("\n"));
        List<String> newLines = List.of(newContent.split("\n"));

        List<String> changes = new ArrayList<>();
        Map<Integer, String> oldLineMap = new HashMap<>();
        Map<Integer, String> newLineMap = new HashMap<>();

        for (int i = 0; i < oldLines.size(); i++) {
            oldLineMap.put(i, oldLines.get(i));
        }

        for (int i = 0; i < newLines.size(); i++) {
            newLineMap.put(i, newLines.get(i));
        }

        int oldIndex = 0, newIndex = 0;
        while (oldIndex < oldLines.size() || newIndex < newLines.size()) {
            if (oldIndex < oldLines.size() && newIndex < newLines.size()) {
                String oldLine = oldLines.get(oldIndex);
                String newLine = newLines.get(newIndex);

                if (oldLine.equals(newLine)) {
                    oldIndex++;
                    newIndex++;
                } else {
                    int oldStart = oldIndex;
                    int newStart = newIndex;

                    while (oldIndex < oldLines.size() && newIndex < newLines.size() &&
                            !oldLines.get(oldIndex).equals(newLines.get(newIndex))) {
                        oldIndex++;
                        newIndex++;
                    }

                    if (oldIndex > oldStart) {
                        changes.add(formatLineRange("- Lines", oldStart, oldIndex));
                        for (int i = oldStart; i < oldIndex; i++) {
                            changes.add("  - " + oldLines.get(i));
                        }
                    }

                    if (newIndex > newStart) {
                        changes.add(formatLineRange("+ Lines", newStart, newIndex));
                        for (int i = newStart; i < newIndex; i++) {
                            changes.add("  + " + newLines.get(i));
                        }
                    }
                }
            } else if (oldIndex < oldLines.size()) {
                int oldStart = oldIndex;
                while (oldIndex < oldLines.size()) {
                    oldIndex++;
                }
                changes.add(formatLineRange("- Lines", oldStart, oldIndex));
                for (int i = oldStart; i < oldIndex; i++) {
                    changes.add("  - " + oldLines.get(i));
                }
            } else if (newIndex < newLines.size()) {
                int newStart = newIndex;
                while (newIndex < newLines.size()) {
                    newIndex++;
                }
                changes.add(formatLineRange("+ Lines", newStart, newIndex));
                for (int i = newStart; i < newIndex; i++) {
                    changes.add("  + " + newLines.get(i));
                }
            }
        }

        return new DiffResult(changes);
    }

    private String formatLineRange(String prefix, int start, int end) {
        if (start + 1 == end) {
            return prefix.substring(0, prefix.length() - 1) + " " + (start + 1) + ":";
        } else {
            return prefix + " " + (start + 1) + " to " + end + ":";
        }
    }
}

