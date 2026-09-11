package com.cnj49.propertymaintenance.dto;

import java.math.BigDecimal;

/** So lieu hop tac cua mot nha thau, hien thi tren trang chi tiet nha thau. */
public class ContractorStatistics {

    private long quotationCount;
    private long workOrderCount;
    private BigDecimal totalPaid = BigDecimal.ZERO;

    public long getQuotationCount() {
        return quotationCount;
    }

    public void setQuotationCount(long quotationCount) {
        this.quotationCount = quotationCount;
    }

    public long getWorkOrderCount() {
        return workOrderCount;
    }

    public void setWorkOrderCount(long workOrderCount) {
        this.workOrderCount = workOrderCount;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }
}
