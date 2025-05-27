package com.std_data_mgmt.app.enums;

public enum TranscriptAccessInquiryStatus {

    WAITING_FOR_PARTICIPANTS("WAITING_FOR_PARTICIPANTS"),
    WAITING_FOR_REQUESTEE("WAITING_FOR_REQUESTEE"),
    APPROVED("APPROVED"),
    CLOSED("CLOSED");

    private final String displayName;

    TranscriptAccessInquiryStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

}
