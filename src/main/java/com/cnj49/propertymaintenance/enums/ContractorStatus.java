package com.cnj49.propertymaintenance.enums;

/** Trang thai hop tac voi nha thau. */
public enum ContractorStatus {

    ACTIVE("Đang hợp tác", "success"),
    SUSPENDED("Tạm ngưng", "warning"),
    INACTIVE("Ngừng hợp tác", "secondary");

    private final String label;
    private final String badge;

    ContractorStatus(String label, String badge) {
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
