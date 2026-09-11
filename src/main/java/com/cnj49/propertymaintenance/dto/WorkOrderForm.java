package com.cnj49.propertymaintenance.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Form tao phieu cong viec tu mot bao gia da duoc duyet.
 * Nha thau khong cho chon: he thong lay tu Quotation da APPROVED (BR09).
 */
public class WorkOrderForm {

    private Long id;

    @NotNull
    private Long maintenanceRequestId;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate = LocalDate.now();

    @NotNull(message = "Ngày dự kiến hoàn thành không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate expectedCompletionDate;

    @NotBlank(message = "Nội dung công việc không được để trống")
    @Size(max = 2000, message = "Nội dung công việc tối đa 2000 ký tự")
    private String workDescription;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate warrantyUntil;

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

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public LocalDate getWarrantyUntil() {
        return warrantyUntil;
    }

    public void setWarrantyUntil(LocalDate warrantyUntil) {
        this.warrantyUntil = warrantyUntil;
    }
}
