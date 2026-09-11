package com.cnj49.propertymaintenance.enums;

/** Cac hanh dong nghiep vu duoc ghi vao audit_logs. */
public enum AuditAction {

    CREATE_MAINTENANCE_REQUEST("Tạo yêu cầu bảo trì"),
    UPDATE_MAINTENANCE_REQUEST("Cập nhật yêu cầu bảo trì"),
    CANCEL_MAINTENANCE_REQUEST("Hủy yêu cầu bảo trì"),
    CLOSE_MAINTENANCE_REQUEST("Đóng yêu cầu bảo trì"),
    CREATE_QUOTATION("Thêm báo giá"),
    APPROVE_QUOTATION("Duyệt báo giá"),
    REJECT_QUOTATION("Từ chối báo giá"),
    CREATE_WORK_ORDER("Tạo phiếu công việc"),
    START_WORK_ORDER("Bắt đầu công việc"),
    PAUSE_WORK_ORDER("Tạm dừng công việc"),
    RESUME_WORK_ORDER("Tiếp tục công việc"),
    COMPLETE_WORK_ORDER("Hoàn thành công việc"),
    CANCEL_WORK_ORDER("Hủy phiếu công việc"),
    INSPECTION_PASSED("Nghiệm thu đạt"),
    INSPECTION_FAILED("Nghiệm thu không đạt"),
    CREATE_EXPENSE("Ghi nhận chi phí");

    private final String label;

    AuditAction(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
