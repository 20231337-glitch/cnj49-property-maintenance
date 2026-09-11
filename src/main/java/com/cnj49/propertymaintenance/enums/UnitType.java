package com.cnj49.propertymaintenance.enums;

/** Loai khong gian ben trong mot bat dong san. */
public enum UnitType {

    APARTMENT("Căn hộ"),
    ROOM("Phòng"),
    COMMON_AREA("Khu vực chung"),
    TECHNICAL_ROOM("Phòng kỹ thuật"),
    PARKING("Bãi xe"),
    SHOP("Mặt bằng kinh doanh"),
    OTHER("Khác");

    private final String label;

    UnitType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
