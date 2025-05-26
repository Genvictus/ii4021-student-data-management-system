package com.std_data_mgmt.app.entity;

public enum Role {
    STUDENT("STUDENT"),
    SUPERVISOR("SUPERVISOR"),
    HOD("HOD");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
