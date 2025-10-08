package com.mpt.journal.domain.model;

public enum Measure {
    Gram("g"),
    KiloGram("kg"),
    Litre("l"),
    Millilitres("ml"),
    Amount("pieces"),
    TableSpoon("tablespoon"),
    TeaSpoon("teaspoon");

    private final String displayName;

    Measure(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
