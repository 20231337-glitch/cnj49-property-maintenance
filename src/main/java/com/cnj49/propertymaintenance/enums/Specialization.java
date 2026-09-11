package com.cnj49.propertymaintenance.enums;

/** Linh vuc chuyen mon cua nha thau. */
public enum Specialization {

    ELECTRICAL("Điện"),
    PLUMBING("Nước"),
    HVAC("Điều hòa"),
    ELEVATOR("Thang máy"),
    FIRE_SAFETY("PCCC"),
    CONSTRUCTION("Xây dựng"),
    INTERIOR("Nội thất"),
    EQUIPMENT("Thiết bị"),
    MULTI_SERVICE("Đa dịch vụ");

    private final String label;

    Specialization(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
