package com.cnj49.propertymaintenance.enums;

/** Trang thai su dung cua can / phong / khu vuc. */
public enum UnitStatus {

    OCCUPIED("Đang cho thuê", "success"),
    VACANT("Còn trống", "secondary"),
    UNDER_MAINTENANCE("Đang bảo trì", "warning"),
    UNAVAILABLE("Không sử dụng", "dark");

    private final String label;
    private final String badge;

    UnitStatus(String label, String badge) {
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
