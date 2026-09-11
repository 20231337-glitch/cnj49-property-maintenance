package com.cnj49.propertymaintenance.controller;

import com.cnj49.propertymaintenance.service.DashboardService;
import com.cnj49.propertymaintenance.service.MaintenanceRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** Trang tong quan he thong: 6 the thong ke + 4 bieu do lay tu database that (muc 22, 37). */
@Controller
public class DashboardController {

    private final DashboardService dashboardService;
    private final MaintenanceRequestService requestService;

    public DashboardController(DashboardService dashboardService, MaintenanceRequestService requestService) {
        this.dashboardService = dashboardService;
        this.requestService = requestService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("stats", dashboardService.getStats());
        model.addAttribute("expensesByMonth", dashboardService.expensesByMonth(12));
        model.addAttribute("requestsByStatus", dashboardService.requestsByStatus());
        model.addAttribute("expensesByProperty", dashboardService.expensesByProperty());
        model.addAttribute("requestsByCategory", dashboardService.requestsByCategory());
        model.addAttribute("recentRequests", requestService.findRecent(8));
        return "dashboard/index";
    }
}
