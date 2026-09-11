package com.cnj49.propertymaintenance.dto;

import java.math.BigDecimal;

/** Tong hop chi phi hien thi tren dau man hinh /expenses. */
public class ExpenseSummary {

    private BigDecimal total = BigDecimal.ZERO;
    private BigDecimal maintenance = BigDecimal.ZERO;
    private BigDecimal labor = BigDecimal.ZERO;
    private BigDecimal material = BigDecimal.ZERO;
    private BigDecimal other = BigDecimal.ZERO;

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(BigDecimal maintenance) {
        this.maintenance = maintenance;
    }

    public BigDecimal getLabor() {
        return labor;
    }

    public void setLabor(BigDecimal labor) {
        this.labor = labor;
    }

    public BigDecimal getMaterial() {
        return material;
    }

    public void setMaterial(BigDecimal material) {
        this.material = material;
    }

    public BigDecimal getOther() {
        return other;
    }

    public void setOther(BigDecimal other) {
        this.other = other;
    }
}
