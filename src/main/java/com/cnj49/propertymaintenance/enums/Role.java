package com.cnj49.propertymaintenance.enums;

/**
 * Vai tro nguoi dung.
 * Spring Security se doc duoi dang authority "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_STAFF".
 */
public enum Role {

    ADMIN("Quản trị viên"),
    MANAGER("Quản lý"),
    STAFF("Nhân viên");

    private final String label;

    Role(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getAuthority() {
        return "ROLE_" + name();
    }
}
