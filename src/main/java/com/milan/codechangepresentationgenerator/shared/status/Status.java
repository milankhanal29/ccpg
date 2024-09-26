package com.milan.codechangepresentationgenerator.shared.status;

public enum Status {
    DELETED("DELETED"),
    ACCEPTED("ACCEPTED"),
    ACTIVE("ACTIVE"),
    PENDING("PENDING"),
    INACTIVE("INACTIVE");
    private final String value;
    Status(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

    // Optionally, you can override toString() method to return the value
    @Override
    public String toString() {
        return value;
    }

}