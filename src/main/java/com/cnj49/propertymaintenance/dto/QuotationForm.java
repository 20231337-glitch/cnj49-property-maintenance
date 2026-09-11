package com.cnj49.propertymaintenance.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Form them/sua bao gia.
 * Luu y: KHONG co truong totalAmount - tong tien do service tinh
 * tu materialCost + laborCost + otherCost.
 */
public class QuotationForm {

    private Long id;

    @NotNull
    private Long maintenanceRequestId;

    @NotNull(message = "Vui lòng chọn nhà thầu")
    private Long contractorId;

    @NotNull(message = "Ngày báo giá không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate quotationDate = LocalDate.now();

    @NotNull(message = "Chi phí vật tư không được để trống")
    @DecimalMin(value = "0.0", message = "Chi phí vật tư phải lớn hơn hoặc bằng 0")
    private BigDecimal materialCost = BigDecimal.ZERO;

    @NotNull(message = "Chi phí nhân công không được để trống")
    @DecimalMin(value = "0.0", message = "Chi phí nhân công phải lớn hơn hoặc bằng 0")
    private BigDecimal laborCost = BigDecimal.ZERO;

    @NotNull(message = "Chi phí khác không được để trống")
    @DecimalMin(value = "0.0", message = "Chi phí khác phải lớn hơn hoặc bằng 0")
    private BigDecimal otherCost = BigDecimal.ZERO;

    @NotNull(message = "Số ngày thực hiện không được để trống")
    @Min(value = 1, message = "Số ngày thực hiện phải lớn hơn 0")
    private Integer estimatedDays = 1;

    @Size(max = 1000, message = "Mô tả tối đa 1000 ký tự")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMaintenanceRequestId() {
        return maintenanceRequestId;
    }

    public void setMaintenanceRequestId(Long maintenanceRequestId) {
        this.maintenanceRequestId = maintenanceRequestId;
    }

    public Long getContractorId() {
        return contractorId;
    }

    public void setContractorId(Long contractorId) {
        this.contractorId = contractorId;
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
}
