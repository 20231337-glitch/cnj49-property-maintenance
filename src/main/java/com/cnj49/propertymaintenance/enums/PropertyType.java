package com.cnj49.propertymaintenance.enums;

/** Phan loai bat dong san cho thue. */
public enum PropertyType {

    APARTMENT_BUILDING("Chung cư"),
    MINI_APARTMENT("Chung cư mini"),
    BOARDING_HOUSE("Nhà trọ"),
    RENTAL_HOUSE("Nhà cho thuê"),
    OFFICE("Văn phòng"),
    OTHER("Khác");

    private final String label;

    PropertyType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
