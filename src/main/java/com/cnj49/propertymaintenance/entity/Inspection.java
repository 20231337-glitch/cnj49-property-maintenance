package com.cnj49.propertymaintenance.entity;

import com.cnj49.propertymaintenance.enums.InspectionResult;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Bien ban nghiem thu mot phieu cong viec.
 * BR04: chi nghiem thu duoc WorkOrder da COMPLETED.
 * BR12/BR13: ket qua PASSED/FAILED se dieu khien trang thai WorkOrder + MaintenanceRequest.
 */
@Entity
@Table(name = "inspections")
public class Inspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "work_order_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_inspection_work_order"))
    private WorkOrder workOrder;

    @NotNull(message = "Ngày nghiệm thu không được để trống")
    @Column(name = "inspection_date", nullable = false)
    private LocalDate inspectionDate = LocalDate.now();

    @NotBlank(message = "Người nghiệm thu không được để trống")
    @Size(max = 100, message = "Người nghiệm thu tối đa 100 ký tự")
    @Column(name = "inspector_name", nullable = false, length = 100)
    private String inspectorName;

    @NotNull(message = "Vui lòng chọn kết quả nghiệm thu")
    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false, length = 20)
    private InspectionResult result;

    @NotNull(message = "Chi phí thực tế không được để trống")
    @DecimalMin(value = "0.0", message = "Chi phí thực tế phải lớn hơn hoặc bằng 0")
    @Column(name = "actual_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal actualCost = BigDecimal.ZERO;

    @Size(max = 1000, message = "Ghi chú tối đa 1000 ký tự")
    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Inspection() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public LocalDate getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(LocalDate inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public String getInspectorName() {
        return inspectorName;
    }

    public void setInspectorName(String inspectorName) {
        this.inspectorName = inspectorName;
    }

    public InspectionResult getResult() {
        return result;
    }

    public void setResult(InspectionResult result) {
        this.result = result;
    }

    public BigDecimal getActualCost() {
        return actualCost;
    }

    public void setActualCost(BigDecimal actualCost) {
        this.actualCost = actualCost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
