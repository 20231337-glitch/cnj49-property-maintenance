package com.cnj49.propertymaintenance.entity;

import com.cnj49.propertymaintenance.enums.ExpenseType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Chi phi van hanh bat dong san.
 * Co the phat sinh tu mot WorkOrder (chi phi bao tri) hoac doc lap
 * (dien, nuoc, ve sinh, an ninh...).
 * BR07: so tien khong duoc am.
 */
@Entity
@Table(name = "expenses",
        uniqueConstraints = @UniqueConstraint(name = "uk_expense_code", columnNames = "expense_code"))
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Ma tu sinh dang EXP-2026-0001. */
    @Column(name = "expense_code", nullable = false, length = 30, unique = true)
    private String expenseCode;

    @NotNull(message = "Vui lòng chọn bất động sản")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "property_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_expense_property"))
    private Property property;

    /** Chi co khi chi phi phat sinh tu mot phieu cong viec. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_id", foreignKey = @ForeignKey(name = "fk_expense_work_order"))
    private WorkOrder workOrder;

    /** Chi co khi chi phi tra cho mot nha thau. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractor_id", foreignKey = @ForeignKey(name = "fk_expense_contractor"))
    private Contractor contractor;

    @NotNull(message = "Vui lòng chọn loại chi phí")
    @Enumerated(EnumType.STRING)
    @Column(name = "expense_type", nullable = false, length = 30)
    private ExpenseType expenseType;

    @NotNull(message = "Số tiền không được để trống")
    @DecimalMin(value = "0.0", message = "Số tiền phải lớn hơn hoặc bằng 0")
    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    @NotNull(message = "Ngày chi không được để trống")
    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate = LocalDate.now();

    @NotBlank(message = "Nội dung chi không được để trống")
    @Size(max = 500, message = "Nội dung chi tối đa 500 ký tự")
    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Size(max = 60, message = "Số chứng từ tối đa 60 ký tự")
    @Column(name = "reference_number", length = 60)
    private String referenceNumber;

    @Size(max = 1000, message = "Ghi chú tối đa 1000 ký tự")
    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Expense() {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExpenseCode() {
        return expenseCode;
    }

    public void setExpenseCode(String expenseCode) {
        this.expenseCode = expenseCode;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(ExpenseType expenseType) {
        this.expenseType = expenseType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
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
}
