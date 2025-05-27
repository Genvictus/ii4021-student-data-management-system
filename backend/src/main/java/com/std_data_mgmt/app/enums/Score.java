package com.std_data_mgmt.app.enums;

public enum Score {
    A("A"),
    AB("AB"),
    B("B"),
    BC("BC"),
    C("C"),
    D("D"),
    E("E");

    private final String displayName;

    Score(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
