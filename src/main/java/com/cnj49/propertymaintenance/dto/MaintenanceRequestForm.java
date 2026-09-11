package com.cnj49.propertymaintenance.dto;

import com.cnj49.propertymaintenance.enums.Priority;
import com.cnj49.propertymaintenance.enums.RequestStatus;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Form object cho man hinh them/sua yeu cau bao tri. */
public class MaintenanceRequestForm {

    private Long id;

    /** Chi hien thi (read-only) khi sua, ma do he thong sinh. */
    private String requestCode;

    @NotNull(message = "Vui lòng chọn bất động sản")
    private Long propertyId;

    /** Co the de trong neu su co o pham vi toan khu. */
    private Long unitId;

    @NotNull(message = "Vui lòng chọn hạng mục")
    private Long categoryId;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 200, message = "Tiêu đề tối đa 200 ký tự")
    private String title;

    @NotBlank(message = "Mô tả sự cố không được để trống")
    @Size(max = 2000, message = "Mô tả tối đa 2000 ký tự")
    private String description;

    @NotNull(message = "Vui lòng chọn mức độ ưu tiên")
    private Priority priority = Priority.MEDIUM;

    /** Chi cho phep sua truc tiep khi chinh sua ban ghi, khong dung khi tao moi. */
    private RequestStatus status;

    @NotNull(message = "Ngày phát hiện không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate reportedDate = LocalDate.now();

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate expectedCompletionDate;

    @DecimalMin(value = "0.0", message = "Chi phí dự kiến phải lớn hơn hoặc bằng 0")
    private BigDecimal estimatedCost = BigDecimal.ZERO;

    @Size(max = 100, message = "Người báo cáo tối đa 100 ký tự")
    private String reportedBy;

    @Size(max = 1000, message = "Ghi chú tối đa 1000 ký tự")
    private String notes;

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

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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
}
