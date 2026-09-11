package com.cnj49.propertymaintenance.service;

import com.cnj49.propertymaintenance.dto.ChartSeries;
import com.cnj49.propertymaintenance.dto.DashboardStats;

/** Tong hop so lieu cho Dashboard. Tat ca deu truy van tu database. */
public interface DashboardService {

    DashboardStats getStats();

    /** Chart 1: chi phi 12 thang gan nhat. */
    ChartSeries expensesByMonth(int months);

    /** Chart 2: yeu cau bao tri theo trang thai. */
    ChartSeries requestsByStatus();

    /** Chart 3: chi phi theo bat dong san. */
    ChartSeries expensesByProperty();

    /** Chart 4: so su co theo hang muc. */
    ChartSeries requestsByCategory();
}
