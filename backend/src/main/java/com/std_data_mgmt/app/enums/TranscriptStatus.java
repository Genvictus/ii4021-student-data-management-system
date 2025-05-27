package com.std_data_mgmt.app.enums;

public enum TranscriptStatus {

    PENDING("PENDING"),
    APPROVED("APPROVED");

    private final String displayName;

    TranscriptStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
