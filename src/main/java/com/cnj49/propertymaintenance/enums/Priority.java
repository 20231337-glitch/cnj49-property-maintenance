package com.cnj49.propertymaintenance.enums;

/** Muc do uu tien xu ly cua yeu cau bao tri. */
public enum Priority {

    LOW("Thấp", "secondary", 1),
    MEDIUM("Trung bình", "info", 2),
    HIGH("Cao", "warning", 3),
    URGENT("Khẩn cấp", "danger", 4);

    private final String label;
    private final String badge;
    private final int weight;

    Priority(String label, String badge, int weight) {
        this.label = label;
        this.badge = badge;
        this.weight = weight;
    }

    public String getLabel() {
        return label;
    }

    public String getBadge() {
        return badge;
    }

    public int getWeight() {
        return weight;
    }
}
