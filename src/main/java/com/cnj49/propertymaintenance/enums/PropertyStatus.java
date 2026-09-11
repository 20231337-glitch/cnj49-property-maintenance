package com.cnj49.propertymaintenance.enums;

/** Trang thai van hanh cua bat dong san. */
public enum PropertyStatus {

    ACTIVE("Đang vận hành", "success"),
    UNDER_MAINTENANCE("Đang bảo trì", "warning"),
    INACTIVE("Ngừng hoạt động", "secondary");

    private final String label;
    private final String badge;

    PropertyStatus(String label, String badge) {
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
