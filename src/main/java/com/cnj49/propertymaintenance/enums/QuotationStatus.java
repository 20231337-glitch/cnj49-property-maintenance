package com.cnj49.propertymaintenance.enums;

/** Trang thai bao gia cua nha thau. */
public enum QuotationStatus {

    PENDING("Chờ duyệt", "warning"),
    APPROVED("Đã duyệt", "success"),
    REJECTED("Từ chối", "danger");

    private final String label;
    private final String badge;

    QuotationStatus(String label, String badge) {
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
