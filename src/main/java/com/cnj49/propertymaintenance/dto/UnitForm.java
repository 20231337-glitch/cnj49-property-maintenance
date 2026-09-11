package com.cnj49.propertymaintenance.dto;

import com.cnj49.propertymaintenance.enums.UnitStatus;
import com.cnj49.propertymaintenance.enums.UnitType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/** Form object cho man hinh them/sua can - phong (co quan he toi Property). */
public class UnitForm {

    private Long id;

    @NotNull(message = "Vui lòng chọn bất động sản")
    private Long propertyId;

    @NotBlank(message = "Mã căn/phòng không được để trống")
    @Size(max = 30, message = "Mã căn/phòng tối đa 30 ký tự")
    private String code;

    @NotBlank(message = "Tên căn/phòng không được để trống")
    @Size(max = 150, message = "Tên căn/phòng tối đa 150 ký tự")
    private String name;

    @NotNull(message = "Tầng không được để trống")
    @Min(value = 0, message = "Tầng phải lớn hơn hoặc bằng 0")
    private Integer floorNumber = 1;

    @NotNull(message = "Vui lòng chọn loại")
    private UnitType unitType;

    @NotNull(message = "Diện tích không được để trống")
    @DecimalMin(value = "0.0", message = "Diện tích phải lớn hơn hoặc bằng 0")
    private BigDecimal area = BigDecimal.ZERO;

    @NotNull(message = "Vui lòng chọn trạng thái")
    private UnitStatus status = UnitStatus.VACANT;

    @Size(max = 1000, message = "Mô tả tối đa 1000 ký tự")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public UnitStatus getStatus() {
        return status;
    }

    public void setStatus(UnitStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
