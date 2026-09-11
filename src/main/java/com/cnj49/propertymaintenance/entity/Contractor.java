package com.cnj49.propertymaintenance.entity;

import com.cnj49.propertymaintenance.enums.ContractorStatus;
import com.cnj49.propertymaintenance.enums.Specialization;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Nha thau thuc hien cong viec bao tri / sua chua. */
@Entity
@Table(name = "contractors",
        uniqueConstraints = @UniqueConstraint(name = "uk_contractor_code", columnNames = "contractor_code"))
public class Contractor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Ma tu sinh dang CTR-0001. */
    @Column(name = "contractor_code", nullable = false, length = 30, unique = true)
    private String contractorCode;

    @NotBlank(message = "Tên công ty không được để trống")
    @Size(max = 150, message = "Tên công ty tối đa 150 ký tự")
    @Column(name = "company_name", nullable = false, length = 150)
    private String companyName;

    @Size(max = 100, message = "Người liên hệ tối đa 100 ký tự")
    @Column(name = "contact_person", length = 100)
    private String contactPerson;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0|[+]84)[0-9]{8,10}$",
            message = "Số điện thoại không hợp lệ (ví dụ: 0912345678)")
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Email(message = "Email không đúng định dạng")
    @Size(max = 120, message = "Email tối đa 120 ký tự")
    @Column(name = "email", length = 120)
    private String email;

    @Size(max = 255, message = "Địa chỉ tối đa 255 ký tự")
    @Column(name = "address", length = 255)
    private String address;

    @Size(max = 30, message = "Mã số thuế tối đa 30 ký tự")
    @Column(name = "tax_code", length = 30)
    private String taxCode;

    @NotNull(message = "Vui lòng chọn lĩnh vực")
    @Enumerated(EnumType.STRING)
    @Column(name = "specialization", nullable = false, length = 40)
    private Specialization specialization;

    @NotNull(message = "Đánh giá không được để trống")
    @DecimalMin(value = "0.0", message = "Đánh giá phải từ 0 đến 5")
    @DecimalMax(value = "5.0", message = "Đánh giá phải từ 0 đến 5")
    @Column(name = "rating", nullable = false, precision = 3, scale = 1)
    private BigDecimal rating = BigDecimal.ZERO;

    @NotNull(message = "Vui lòng chọn trạng thái")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ContractorStatus status = ContractorStatus.ACTIVE;

    @Size(max = 1000, message = "Ghi chú tối đa 1000 ký tự")
    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Contractor() {
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

    public String getContractorCode() {
        return contractorCode;
    }

    public void setContractorCode(String contractorCode) {
        this.contractorCode = contractorCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public ContractorStatus getStatus() {
        return status;
    }

    public void setStatus(ContractorStatus status) {
        this.status = status;
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
