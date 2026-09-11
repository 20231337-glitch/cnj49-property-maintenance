package com.cnj49.propertymaintenance.entity;

import com.cnj49.propertymaintenance.enums.PropertyStatus;
import com.cnj49.propertymaintenance.enums.PropertyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Bat dong san cho thue - goc cua toan bo cay du lieu:
 * Property 1---N Unit, Property 1---N MaintenanceRequest, Property 1---N Expense.
 */
@Entity
@Table(name = "properties", uniqueConstraints = @UniqueConstraint(name = "uk_property_code", columnNames = "code"))
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Ma tu sinh dang PROP-0001, nguoi dung khong tu nhap. */
    @Column(name = "code", nullable = false, length = 30, unique = true)
    private String code;

    @NotBlank(message = "Tên bất động sản không được để trống")
    @Size(max = 150, message = "Tên bất động sản tối đa 150 ký tự")
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @NotNull(message = "Vui lòng chọn loại bất động sản")
    @Enumerated(EnumType.STRING)
    @Column(name = "property_type", nullable = false, length = 40)
    private PropertyType propertyType;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 255, message = "Địa chỉ tối đa 255 ký tự")
    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @NotNull(message = "Số tầng không được để trống")
    @Min(value = 0, message = "Số tầng phải lớn hơn hoặc bằng 0")
    @Column(name = "number_of_floors", nullable = false)
    private Integer numberOfFloors = 0;

    @NotNull(message = "Số căn/phòng không được để trống")
    @Min(value = 0, message = "Số căn/phòng phải lớn hơn hoặc bằng 0")
    @Column(name = "number_of_units", nullable = false)
    private Integer numberOfUnits = 0;

    @NotNull(message = "Diện tích không được để trống")
    @DecimalMin(value = "0.0", message = "Diện tích phải lớn hơn hoặc bằng 0")
    @Column(name = "area", nullable = false, precision = 12, scale = 2)
    private BigDecimal area = BigDecimal.ZERO;

    @Column(name = "operation_date")
    private LocalDate operationDate;

    @NotNull(message = "Vui lòng chọn trạng thái")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private PropertyStatus status = PropertyStatus.ACTIVE;

    @Size(max = 1000, message = "Mô tả tối đa 1000 ký tự")
    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY)
    private List<Unit> units = new ArrayList<>();

    public Property() {
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

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getNumberOfFloors() {
        return numberOfFloors;
    }

    public void setNumberOfFloors(Integer numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
    }

    public Integer getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(Integer numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public LocalDate getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(LocalDate operationDate) {
        this.operationDate = operationDate;
    }

    public PropertyStatus getStatus() {
        return status;
    }

    public void setStatus(PropertyStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }
}
