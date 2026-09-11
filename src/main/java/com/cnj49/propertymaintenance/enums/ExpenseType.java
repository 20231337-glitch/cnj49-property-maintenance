package com.cnj49.propertymaintenance.enums;

/** Phan loai chi phi van hanh bat dong san. */
public enum ExpenseType {

    MAINTENANCE("Bảo trì", "primary"),
    REPAIR("Sửa chữa", "info"),
    MATERIAL("Vật tư", "secondary"),
    LABOR("Nhân công", "warning"),
    UTILITY("Tiện ích", "success"),
    CLEANING("Vệ sinh", "info"),
    SECURITY("An ninh", "dark"),
    OTHER("Khác", "secondary");

    private final String label;
    private final String badge;

    ExpenseType(String label, String badge) {
        this.label = label;
        this.badge = badge;
    }

    public String getLabel() {
        return label;
    }

    public String getBadge() {
        return badge;
    }
}
