package com.cnj49.propertymaintenance.enums;

/** Trang thai phieu cong viec giao cho nha thau. */
public enum WorkOrderStatus {

    NOT_STARTED("Chưa bắt đầu", "secondary"),
    IN_PROGRESS("Đang thực hiện", "primary"),
    PAUSED("Tạm dừng", "warning"),
    COMPLETED("Hoàn thành", "info"),
    ACCEPTED("Đã nghiệm thu", "success"),
    CANCELLED("Đã hủy", "danger");

    private final String label;
    private final String badge;

    WorkOrderStatus(String label, String badge) {
        this.label = label;
        this.badge = badge;
    }

    public String getLabel() {
        return label;
    }

    public String getBadge() {
        return badge;
    }

    /** Phieu cong viec dang chay (co the tam dung / hoan thanh / huy). */
    public boolean isActive() {
        return this == NOT_STARTED || this == IN_PROGRESS || this == PAUSED;
    }
}
