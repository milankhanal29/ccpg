package com.milan.codechangepresentationgenerator.util;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MyersDiff {

    public DiffResult diff(String oldContent, String newContent) {
        log.info("Calculating diff between old content:\n{}\nand new content:\n{}", oldContent, newContent);
        List<String> oldLines = List.of(oldContent.split("\n"));
        List<String> newLines = List.of(newContent.split("\n"));

        List<String> changes = new ArrayList<>();

        int oldIndex = 0, newIndex = 0;

        // Iterate through old and new lines
        while (oldIndex < oldLines.size() || newIndex < newLines.size()) {
            if (oldIndex < oldLines.size() && newIndex < newLines.size()) {
                String oldLine = oldLines.get(oldIndex);
                String newLine = newLines.get(newIndex);

                if (oldLine.equals(newLine)) {
                    // If lines are the same, move to the next
                    oldIndex++;
                    newIndex++;
                } else {
                    // Find matching lines ahead to limit impact of a single change
                    int matchOffset = findNextMatchingLine(oldLines, newLines, oldIndex, newIndex);
                    if (matchOffset > 0) {
                        // Process the unmatched lines before the matching block
                        changes.addAll(processUnmatchedLines(oldLines, newLines, oldIndex, newIndex, matchOffset));
                        oldIndex += matchOffset;
                        newIndex += matchOffset;
                    } else {
                        // Handle the line changes if no match is found
                        changes.addAll(handleLineChanges(oldLine, newLine));
                        oldIndex++;
                        newIndex++;
                    }
                }
            } else if (oldIndex < oldLines.size()) {
                // Handle remaining removed lines
                changes.add(formatLineRange("- Lines", oldIndex, oldIndex + 1));
                changes.add("  - " + oldLines.get(oldIndex));
                oldIndex++;
            } else if (newIndex < newLines.size()) {
                // Handle remaining added lines
                changes.add(formatLineRange("+ Lines", newIndex, newIndex + 1));
                changes.add("  + " + newLines.get(newIndex));
                newIndex++;
            }
        }

        return new DiffResult(changes);
    }
    private int findNextMatchingLine(List<String> oldLines, List<String> newLines, int oldIndex, int newIndex) {
        int maxLookAhead = Math.min(oldLines.size() - oldIndex, newLines.size() - newIndex);
        for (int offset = 1; offset <= maxLookAhead; offset++) {
            // Ensure indices do not go out of bounds
            if (oldIndex + offset < oldLines.size() && newIndex + offset < newLines.size()) {
                if (oldLines.get(oldIndex + offset).equals(newLines.get(newIndex + offset))) {
                    return offset; // Found a match
                }
            }
        }
        return 0;
    }

    private List<String> processUnmatchedLines(List<String> oldLines, List<String> newLines, int oldIndex, int newIndex, int matchOffset) {
        List<String> changes = new ArrayList<>();

        // Handle removed lines in the unmatched section
        for (int i = oldIndex; i < oldIndex + matchOffset && i < oldLines.size(); i++) {
            changes.add(formatLineRange("- Lines", i, i + 1));
            changes.add("  - " + oldLines.get(i));
        }

        // Handle added lines in the unmatched section
        for (int i = newIndex; i < newIndex + matchOffset && i < newLines.size(); i++) {
            changes.add(formatLineRange("+ Lines", i, i + 1));
            changes.add("  + " + newLines.get(i));
        }

        return changes;
    }

    private List<String> handleLineChanges(String oldLine, String newLine) {
        List<String> changes = new ArrayList<>();

        // If lines have different content, but the difference is not a complete replacement
        if (isSimilarEnough(oldLine, newLine)) {
            changes.add("~ Modified Line:");
            changes.add("  - " + highlightChanges(oldLine, newLine, '-')); // Highlight removed characters
            changes.add("  + " + highlightChanges(oldLine, newLine, '+')); // Highlight added characters
        } else {
            // If the lines are completely different, treat them as removed/added
            changes.add("- " + oldLine);
            changes.add("+ " + newLine);
        }

        return changes;
    }

    private boolean isSimilarEnough(String oldLine, String newLine) {
        int maxLength = Math.max(oldLine.length(), newLine.length());
        int threshold = (int) (0.3 * maxLength);  // Allow 30% difference
        return Math.abs(oldLine.length() - newLine.length()) < threshold;
    }

    private String highlightChanges(String oldLine, String newLine, char diffType) {
        StringBuilder result = new StringBuilder();
        int oldIndex = 0, newIndex = 0;

        while (oldIndex < oldLine.length() || newIndex < newLine.length()) {
            if (oldIndex < oldLine.length() && newIndex < newLine.length()) {
                char oldChar = oldLine.charAt(oldIndex);
                char newChar = newLine.charAt(newIndex);

                if (oldChar == newChar) {
                    result.append(oldChar);
                    oldIndex++;
                    newIndex++;
                } else {
                    result.append(diffType == '-' ? "[" + oldChar + "]" : "<" + newChar + ">");
                    oldIndex++;
                    newIndex++;
                }
            } else if (oldIndex < oldLine.length()) {
                result.append(diffType == '-' ? "[" + oldLine.charAt(oldIndex) + "]" : "");
                oldIndex++;
            } else if (newIndex < newLine.length()) {
                result.append(diffType == '+' ? "<" + newLine.charAt(newIndex) + ">" : "");
                newIndex++;
            }
        }

        return result.toString();
    }

    private String formatLineRange(String prefix, int start, int end) {
        return prefix + " " + start + " to " + end;
    }
}


