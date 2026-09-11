package com.cnj49.propertymaintenance.entity;

import com.cnj49.propertymaintenance.enums.QuotationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Bao gia cua mot nha thau cho mot yeu cau bao tri.
 * BR02: mot MaintenanceRequest chi duoc co toi da MOT bao gia APPROVED.
 * totalAmount luon duoc tinh lai o service = materialCost + laborCost + otherCost,
 * nguoi dung khong duoc tu nhap.
 */
@Entity
@Table(name = "quotations",
        uniqueConstraints = @UniqueConstraint(name = "uk_quotation_code", columnNames = "quotation_code"))
public class Quotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Ma tu sinh dang QT-2026-0001. */
    @Column(name = "quotation_code", nullable = false, length = 30, unique = true)
    private String quotationCode;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "maintenance_request_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_quotation_request"))
    private MaintenanceRequest maintenanceRequest;

    @NotNull(message = "Vui lòng chọn nhà thầu")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contractor_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_quotation_contractor"))
    private Contractor contractor;

    @NotNull(message = "Ngày báo giá không được để trống")
    @Column(name = "quotation_date", nullable = false)
    private LocalDate quotationDate = LocalDate.now();

    @NotNull(message = "Chi phí vật tư không được để trống")
    @DecimalMin(value = "0.0", message = "Chi phí vật tư phải lớn hơn hoặc bằng 0")
    @Column(name = "material_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal materialCost = BigDecimal.ZERO;

    @NotNull(message = "Chi phí nhân công không được để trống")
    @DecimalMin(value = "0.0", message = "Chi phí nhân công phải lớn hơn hoặc bằng 0")
    @Column(name = "labor_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal laborCost = BigDecimal.ZERO;

    @NotNull(message = "Chi phí khác không được để trống")
    @DecimalMin(value = "0.0", message = "Chi phí khác phải lớn hơn hoặc bằng 0")
    @Column(name = "other_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal otherCost = BigDecimal.ZERO;

    /** Duoc tinh tu 3 thanh phan tren, khong nhan tu form. */
    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @NotNull(message = "Số ngày thực hiện không được để trống")
    @Min(value = 1, message = "Số ngày thực hiện phải lớn hơn 0")
    @Column(name = "estimated_days", nullable = false)
    private Integer estimatedDays = 1;

    @Size(max = 1000, message = "Mô tả tối đa 1000 ký tự")
    @Column(name = "description", length = 1000)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private QuotationStatus status = QuotationStatus.PENDING;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Quotation() {
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

    /** Tinh lai tong tien tu 3 thanh phan chi phi. */
    public void recalculateTotal() {
        BigDecimal material = materialCost != null ? materialCost : BigDecimal.ZERO;
        BigDecimal labor = laborCost != null ? laborCost : BigDecimal.ZERO;
        BigDecimal other = otherCost != null ? otherCost : BigDecimal.ZERO;
        this.totalAmount = material.add(labor).add(other);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuotationCode() {
        return quotationCode;
    }

    public void setQuotationCode(String quotationCode) {
        this.quotationCode = quotationCode;
    }

    public MaintenanceRequest getMaintenanceRequest() {
        return maintenanceRequest;
    }

    public void setMaintenanceRequest(MaintenanceRequest maintenanceRequest) {
        this.maintenanceRequest = maintenanceRequest;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }

    public LocalDate getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(LocalDate quotationDate) {
        this.quotationDate = quotationDate;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    }

    public BigDecimal getOtherCost() {
        return otherCost;
    }

    public void setOtherCost(BigDecimal otherCost) {
        this.otherCost = otherCost;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getEstimatedDays() {
        return estimatedDays;
    }

    public void setEstimatedDays(Integer estimatedDays) {
        this.estimatedDays = estimatedDays;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public QuotationStatus getStatus() {
        return status;
    }

    public void setStatus(QuotationStatus status) {
        this.status = status;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
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
