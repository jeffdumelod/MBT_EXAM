package com.mbt.exam.model;

public enum AccountType {
    S("Savings"),
    C("Checking");

    private final String description;

    AccountType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
