package com.cnj49.propertymaintenance.dto;

import com.cnj49.propertymaintenance.enums.InspectionResult;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Form nghiem thu mot phieu cong viec. */
public class InspectionForm {

    @NotNull
    private Long workOrderId;

    @NotNull(message = "Ngày nghiệm thu không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate inspectionDate = LocalDate.now();

    @NotBlank(message = "Người nghiệm thu không được để trống")
    @Size(max = 100, message = "Người nghiệm thu tối đa 100 ký tự")
    private String inspectorName;

    @NotNull(message = "Vui lòng chọn kết quả nghiệm thu")
    private InspectionResult result;

    @NotNull(message = "Chi phí thực tế không được để trống")
    @DecimalMin(value = "0.0", message = "Chi phí thực tế phải lớn hơn hoặc bằng 0")
    private BigDecimal actualCost = BigDecimal.ZERO;

    @Size(max = 1000, message = "Ghi chú tối đa 1000 ký tự")
    private String notes;

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
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
}
