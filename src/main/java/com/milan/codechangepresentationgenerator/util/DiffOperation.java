package com.milan.codechangepresentationgenerator.util;
public class DiffOperation {
    private String operationType; // e.g., "ADD", "DEL", "MOD"
    private String content; // The content that was added, deleted, or modified
    private int lineNumber;

    public DiffOperation(String operationType, String content, int lineNumber) {
        this.operationType = operationType;
        this.content = content;
        this.lineNumber = lineNumber;
    }

    public String getOperationType() {
        return operationType;
    }

    public String getContent() {
        return content;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
