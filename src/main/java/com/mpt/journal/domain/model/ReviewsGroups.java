package com.mpt.journal.domain.model;

public enum ReviewsGroups {
    All("All"), Positive("Positive only"), Negative("Negative only");

    private final String displayName;

    ReviewsGroups(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
