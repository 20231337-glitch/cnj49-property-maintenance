package com.cnj49.propertymaintenance.enums;

/** Ket qua nghiem thu cong viec. */
public enum InspectionResult {

    PASSED("Đạt", "success"),
    FAILED("Không đạt", "danger");

    private final String label;
    private final String badge;

    InspectionResult(String label, String badge) {
        this.label = label;
        this.badge = badge;
    }

    public String getLabel() {
        return label;
    }

    public String getBadge() {
        return badge;
    }
}
