package com.cnj49.propertymaintenance.controller;

import com.cnj49.propertymaintenance.service.ContractorService;
import com.cnj49.propertymaintenance.service.PropertyService;
import com.cnj49.propertymaintenance.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

/** Bay bao cao theo muc 31, ho tro loc fromDate/toDate/property/contractor. */
@Controller
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;
    private final PropertyService propertyService;
    private final ContractorService contractorService;

    public ReportController(ReportService reportService, PropertyService propertyService,
                            ContractorService contractorService) {
        this.reportService = reportService;
        this.propertyService = propertyService;
        this.contractorService = contractorService;
    }

    @GetMapping
    public String reports(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                          @RequestParam(required = false) Long propertyId,
                          @RequestParam(required = false) Long contractorId,
                          Model model) {
        // Mac dinh xem 6 thang gan nhat neu nguoi dung chua chon.
        LocalDate effectiveTo = toDate != null ? toDate : LocalDate.now();
        LocalDate effectiveFrom = fromDate != null ? fromDate : effectiveTo.minusMonths(6);

        model.addAttribute("fromDate", effectiveFrom);
        model.addAttribute("toDate", effectiveTo);
        model.addAttribute("propertyId", propertyId);
        model.addAttribute("contractorId", contractorId);
        model.addAttribute("properties", propertyService.findAll());
        model.addAttribute("contractors", contractorService.findAll());

        model.addAttribute("totalExpense", reportService.totalExpense(effectiveFrom, effectiveTo, propertyId));

        // Bao cao 1
        model.addAttribute("expenseByMonth", reportService.expenseByMonth(effectiveFrom, effectiveTo));
        // Bao cao 2
        model.addAttribute("expenseByProperty", reportService.expenseByProperty(effectiveFrom, effectiveTo, propertyId));
        // Bao cao 3
        model.addAttribute("expenseByType", reportService.expenseByType(effectiveFrom, effectiveTo, propertyId));
        // Bao cao 4
        model.addAttribute("requestsByCategory", reportService.requestsByCategory(effectiveFrom, effectiveTo));
        // Bao cao 5
        model.addAttribute("topContractors", reportService.topContractors(effectiveFrom, effectiveTo, contractorId));
        // Bao cao 6
        model.addAttribute("requestsByStatus", reportService.requestsByStatus(effectiveFrom, effectiveTo));
        // Bao cao 7
        model.addAttribute("overdueRequests", reportService.overdueRequests());

        return "reports/index";
    }
}
