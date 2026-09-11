package com.cnj49.propertymaintenance.entity;

import com.cnj49.propertymaintenance.enums.UnitStatus;
import com.cnj49.propertymaintenance.enums.UnitType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Can ho / phong / khu vuc / phong ky thuat thuoc mot bat dong san.
 * BR08: mot Unit bat buoc phai thuoc mot Property ton tai.
 */
@Entity
@Table(name = "units",
        uniqueConstraints = @UniqueConstraint(name = "uk_unit_property_code", columnNames = {"property_id", "code"}))
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Vui lòng chọn bất động sản")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "property_id", nullable = false, foreignKey = @ForeignKey(name = "fk_unit_property"))
    private Property property;

    @NotBlank(message = "Mã căn/phòng không được để trống")
    @Size(max = 30, message = "Mã căn/phòng tối đa 30 ký tự")
    @Column(name = "code", nullable = false, length = 30)
    private String code;

    @NotBlank(message = "Tên căn/phòng không được để trống")
    @Size(max = 150, message = "Tên căn/phòng tối đa 150 ký tự")
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @NotNull(message = "Tầng không được để trống")
    @Min(value = 0, message = "Tầng phải lớn hơn hoặc bằng 0")
    @Column(name = "floor_number", nullable = false)
    private Integer floorNumber = 1;

    @NotNull(message = "Vui lòng chọn loại")
    @Enumerated(EnumType.STRING)
    @Column(name = "unit_type", nullable = false, length = 40)
    private UnitType unitType;

    @NotNull(message = "Diện tích không được để trống")
    @DecimalMin(value = "0.0", message = "Diện tích phải lớn hơn hoặc bằng 0")
    @Column(name = "area", nullable = false, precision = 12, scale = 2)
    private BigDecimal area = BigDecimal.ZERO;

    @NotNull(message = "Vui lòng chọn trạng thái")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private UnitStatus status = UnitStatus.VACANT;

    @Size(max = 1000, message = "Mô tả tối đa 1000 ký tự")
    @Column(name = "description", length = 1000)
    private String description;

    public Unit() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
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
