package com.cnj49.propertymaintenance.entity;

import com.cnj49.propertymaintenance.enums.WorkOrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Phieu cong viec giao cho nha thau.
 * BR01: chi duoc tao khi MaintenanceRequest da co Quotation APPROVED.
 * BR09: contractor cua WorkOrder phai trung contractor cua Quotation duoc duyet.
 */
@Entity
@Table(name = "work_orders",
        uniqueConstraints = @UniqueConstraint(name = "uk_work_order_code", columnNames = "work_order_code"))
public class WorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Ma tu sinh dang WO-2026-0001. */
    @Column(name = "work_order_code", nullable = false, length = 30, unique = true)
    private String workOrderCode;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "maintenance_request_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_work_order_request"))
    private MaintenanceRequest maintenanceRequest;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quotation_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_work_order_quotation"))
    private Quotation quotation;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contractor_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_work_order_contractor"))
    private Contractor contractor;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate = LocalDate.now();

    @NotNull(message = "Ngày dự kiến hoàn thành không được để trống")
    @Column(name = "expected_completion_date", nullable = false)
    private LocalDate expectedCompletionDate;

    @Column(name = "actual_completion_date")
    private LocalDate actualCompletionDate;

    @NotBlank(message = "Nội dung công việc không được để trống")
    @Size(max = 2000, message = "Nội dung công việc tối đa 2000 ký tự")
    @Column(name = "work_description", nullable = false, length = 2000)
    private String workDescription;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private WorkOrderStatus status = WorkOrderStatus.NOT_STARTED;

    @Size(max = 2000, message = "Kết quả tối đa 2000 ký tự")
    @Column(name = "result", length = 2000)
    private String result;

    @Column(name = "warranty_until")
    private LocalDate warrantyUntil;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public WorkOrder() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /** Phan tram tien do uoc tinh de hien thi progress bar. */
    @Transient
    public int getProgressPercent() {
        return switch (status) {
            case NOT_STARTED -> 0;
            case IN_PROGRESS -> 50;
            case PAUSED -> 40;
            case COMPLETED -> 80;
            case ACCEPTED -> 100;
            case CANCELLED -> 0;
        };
    }

    /** Cong viec cham tien do so voi ngay du kien hoan thanh. */
    @Transient
    public boolean isLate() {
        return expectedCompletionDate != null
                && status.isActive()
                && expectedCompletionDate.isBefore(LocalDate.now());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkOrderCode() {
        return workOrderCode;
    }

    public void setWorkOrderCode(String workOrderCode) {
        this.workOrderCode = workOrderCode;
    }

    public MaintenanceRequest getMaintenanceRequest() {
        return maintenanceRequest;
    }

    public void setMaintenanceRequest(MaintenanceRequest maintenanceRequest) {
        this.maintenanceRequest = maintenanceRequest;
    }

    public Quotation getQuotation() {
        return quotation;
    }

    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getExpectedCompletionDate() {
        return expectedCompletionDate;
    }

    public void setExpectedCompletionDate(LocalDate expectedCompletionDate) {
        this.expectedCompletionDate = expectedCompletionDate;
    }

    public LocalDate getActualCompletionDate() {
        return actualCompletionDate;
    }

    public void setActualCompletionDate(LocalDate actualCompletionDate) {
        this.actualCompletionDate = actualCompletionDate;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public WorkOrderStatus getStatus() {
        return status;
    }

    public void setStatus(WorkOrderStatus status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDate getWarrantyUntil() {
        return warrantyUntil;
    }

    public void setWarrantyUntil(LocalDate warrantyUntil) {
        this.warrantyUntil = warrantyUntil;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
