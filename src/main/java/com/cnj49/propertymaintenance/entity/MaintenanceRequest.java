package com.cnj49.propertymaintenance.entity;

import com.cnj49.propertymaintenance.enums.Priority;
import com.cnj49.propertymaintenance.enums.RequestStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Yeu cau bao tri - bang nghiep vu trung tam cua he thong.
 * Mot yeu cau di qua vong doi: NEW -> ... -> COMPLETED -> CLOSED
 * va co the sinh ra nhieu Quotation, mot WorkOrder dang hoat dong tai mot thoi diem.
 */
@Entity
@Table(name = "maintenance_requests",
        uniqueConstraints = @UniqueConstraint(name = "uk_request_code", columnNames = "request_code"))
public class MaintenanceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Ma tu sinh dang MR-2026-0001. */
    @Column(name = "request_code", nullable = false, length = 30, unique = true)
    private String requestCode;

    @NotNull(message = "Vui lòng chọn bất động sản")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "property_id", nullable = false, foreignKey = @ForeignKey(name = "fk_request_property"))
    private Property property;

    /** Co the de trong neu su co xay ra o pham vi toan toa nha. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", foreignKey = @ForeignKey(name = "fk_request_unit"))
    private Unit unit;

    @NotNull(message = "Vui lòng chọn hạng mục")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "fk_request_category"))
    private MaintenanceCategory category;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 200, message = "Tiêu đề tối đa 200 ký tự")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @NotBlank(message = "Mô tả sự cố không được để trống")
    @Size(max = 2000, message = "Mô tả tối đa 2000 ký tự")
    @Column(name = "description", nullable = false, length = 2000)
    private String description;

    @NotNull(message = "Vui lòng chọn mức độ ưu tiên")
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 20)
    private Priority priority = Priority.MEDIUM;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private RequestStatus status = RequestStatus.NEW;

    @NotNull(message = "Ngày phát hiện không được để trống")
    @Column(name = "reported_date", nullable = false)
    private LocalDate reportedDate = LocalDate.now();

    @Column(name = "expected_completion_date")
    private LocalDate expectedCompletionDate;

    @Column(name = "actual_completion_date")
    private LocalDate actualCompletionDate;

    @DecimalMin(value = "0.0", message = "Chi phí dự kiến phải lớn hơn hoặc bằng 0")
    @Column(name = "estimated_cost", precision = 15, scale = 2)
    private BigDecimal estimatedCost = BigDecimal.ZERO;

    @Size(max = 100, message = "Người báo cáo tối đa 100 ký tự")
    @Column(name = "reported_by", length = 100)
    private String reportedBy;

    @Size(max = 1000, message = "Ghi chú tối đa 1000 ký tự")
    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "maintenanceRequest", fetch = FetchType.LAZY)
    private List<Quotation> quotations = new ArrayList<>();

    @OneToMany(mappedBy = "maintenanceRequest", fetch = FetchType.LAZY)
    private List<WorkOrder> workOrders = new ArrayList<>();

    public MaintenanceRequest() {
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

    /**
     * Yeu cau bi qua han khi da vuot ngay du kien hoan thanh ma van chua ket thuc.
     * Dung cho bao cao 7 - Yeu cau qua han.
     */
    @Transient
    public boolean isOverdue() {
        return expectedCompletionDate != null
                && status.isOpen()
                && expectedCompletionDate.isBefore(LocalDate.now());
    }

    /** Vi tri xay ra su co, hien thi gon tren danh sach. */
    @Transient
    public String getLocation() {
        return unit != null ? unit.getName() : "Toàn bộ khu";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public MaintenanceCategory getCategory() {
        return category;
    }

    public void setCategory(MaintenanceCategory category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public LocalDate getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(LocalDate reportedDate) {
        this.reportedDate = reportedDate;
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

    public BigDecimal getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Quotation> getQuotations() {
        return quotations;
    }

    public void setQuotations(List<Quotation> quotations) {
        this.quotations = quotations;
    }

    public List<WorkOrder> getWorkOrders() {
        return workOrders;
    }

    public void setWorkOrders(List<WorkOrder> workOrders) {
        this.workOrders = workOrders;
    }
}
