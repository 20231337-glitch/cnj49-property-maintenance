package com.cnj49.propertymaintenance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Danh muc hang muc bao tri: Dien, Nuoc, Dieu hoa, Thang may, PCCC... */
@Entity
@Table(name = "maintenance_categories",
        uniqueConstraints = @UniqueConstraint(name = "uk_category_name", columnNames = "name"))
public class MaintenanceCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên hạng mục không được để trống")
    @Size(max = 100, message = "Tên hạng mục tối đa 100 ký tự")
    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;

    @Size(max = 500, message = "Mô tả tối đa 500 ký tự")
    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "active", nullable = false)
    private Boolean active = Boolean.TRUE;

    public MaintenanceCategory() {
    }

    public MaintenanceCategory(String name, String description) {
        this.name = name;
        this.description = description;
        this.active = Boolean.TRUE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
