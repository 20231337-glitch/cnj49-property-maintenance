package com.cnj49.propertymaintenance.enums;

/**
 * Trang thai vong doi cua mot yeu cau bao tri (MaintenanceRequest).
 * Luong chuan: NEW -> UNDER_REVIEW -> WAITING_QUOTATION -> QUOTATION_APPROVED
 * -> IN_PROGRESS -> WAITING_INSPECTION -> COMPLETED -> CLOSED
 */
public enum RequestStatus {

    NEW("Moi", "Mới", "secondary"),
    UNDER_REVIEW("Dang xem xet", "Đang xem xét", "info"),
    WAITING_QUOTATION("Cho bao gia", "Chờ báo giá", "warning"),
    QUOTATION_APPROVED("Da duyet bao gia", "Đã duyệt báo giá", "primary"),
    IN_PROGRESS("Dang thuc hien", "Đang thực hiện", "primary"),
    WAITING_INSPECTION("Cho nghiem thu", "Chờ nghiệm thu", "warning"),
    COMPLETED("Hoan thanh", "Hoàn thành", "success"),
    CLOSED("Da dong", "Đã đóng", "dark"),
    CANCELLED("Da huy", "Đã hủy", "danger");

    private final String code;
    private final String label;
    private final String badge;

    RequestStatus(String code, String label, String badge) {
        this.code = code;
        this.label = label;
        this.badge = badge;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public String getBadge() {
        return badge;
    }

    /** Yeu cau van con dang mo (chua ket thuc vong doi). */
    public boolean isOpen() {
        return this != COMPLETED && this != CLOSED && this != CANCELLED;
    }

    /** Khong the thay doi nghiep vu tren yeu cau da ket thuc. */
    public boolean isFinal() {
        return this == CLOSED || this == CANCELLED;
    }
}
