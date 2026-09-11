package com.cnj49.propertymaintenance.dto;

import com.cnj49.propertymaintenance.enums.ExpenseType;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Form them/sua chi phi van hanh. */
public class ExpenseForm {

    private Long id;

    private String expenseCode;

    @NotNull(message = "Vui lòng chọn bất động sản")
    private Long propertyId;

    /** Tuy chon: chi phi phat sinh tu mot phieu cong viec. */
    private Long workOrderId;

    /** Tuy chon: chi phi tra cho mot nha thau. */
    private Long contractorId;

    @NotNull(message = "Vui lòng chọn loại chi phí")
    private ExpenseType expenseType;

    @NotNull(message = "Số tiền không được để trống")
    @DecimalMin(value = "0.0", message = "Số tiền phải lớn hơn hoặc bằng 0")
    private BigDecimal amount = BigDecimal.ZERO;

    @NotNull(message = "Ngày chi không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate expenseDate = LocalDate.now();

    @NotBlank(message = "Nội dung chi không được để trống")
    @Size(max = 500, message = "Nội dung chi tối đa 500 ký tự")
    private String description;

    @Size(max = 60, message = "Số chứng từ tối đa 60 ký tự")
    private String referenceNumber;

    @Size(max = 1000, message = "Ghi chú tối đa 1000 ký tự")
    private String notes;

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

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Long getContractorId() {
        return contractorId;
    }

    public void setContractorId(Long contractorId) {
        this.contractorId = contractorId;
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
}
