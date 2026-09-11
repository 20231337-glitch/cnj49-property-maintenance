package com.cnj49.propertymaintenance.dto;

import java.math.BigDecimal;

/** So lieu cho 6 the thong ke tren Dashboard. Tat ca lay tu database that. */
public class DashboardStats {

    private long totalProperties;
    private long totalUnits;
    private long openRequests;
    private long urgentRequests;
    private long activeWorkOrders;
    private BigDecimal currentMonthExpense = BigDecimal.ZERO;

    public long getTotalProperties() {
        return totalProperties;
    }

    public void setTotalProperties(long totalProperties) {
        this.totalProperties = totalProperties;
    }

    public long getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(long totalUnits) {
        this.totalUnits = totalUnits;
    }

    public long getOpenRequests() {
        return openRequests;
    }

    public void setOpenRequests(long openRequests) {
        this.openRequests = openRequests;
    }

    public long getUrgentRequests() {
        return urgentRequests;
    }

    public void setUrgentRequests(long urgentRequests) {
        this.urgentRequests = urgentRequests;
    }

    public long getActiveWorkOrders() {
        return activeWorkOrders;
    }

    public void setActiveWorkOrders(long activeWorkOrders) {
        this.activeWorkOrders = activeWorkOrders;
    }

    public BigDecimal getCurrentMonthExpense() {
        return currentMonthExpense;
    }

    public void setCurrentMonthExpense(BigDecimal currentMonthExpense) {
        this.currentMonthExpense = currentMonthExpense;
    }
}
