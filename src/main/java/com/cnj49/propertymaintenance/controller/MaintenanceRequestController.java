package com.cnj49.propertymaintenance.controller;

import com.cnj49.propertymaintenance.dto.MaintenanceRequestForm;
import com.cnj49.propertymaintenance.entity.MaintenanceRequest;
import com.cnj49.propertymaintenance.enums.Priority;
import com.cnj49.propertymaintenance.enums.RequestStatus;
import com.cnj49.propertymaintenance.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

/** Quan ly yeu cau bao tri - man hinh trung tam the hien toan bo vong doi (muc 25). */
@Controller
@RequestMapping("/maintenance")
public class MaintenanceRequestController {

    private final MaintenanceRequestService requestService;
    private final PropertyService propertyService;
    private final UnitService unitService;
    private final MaintenanceCategoryService categoryService;
    private final QuotationService quotationService;
    private final WorkOrderService workOrderService;
    private final AuditLogService auditLogService;

    public MaintenanceRequestController(MaintenanceRequestService requestService,
                                        PropertyService propertyService,
                                        UnitService unitService,
                                        MaintenanceCategoryService categoryService,
                                        QuotationService quotationService,
                                        WorkOrderService workOrderService,
                                        AuditLogService auditLogService) {
        this.requestService = requestService;
        this.propertyService = propertyService;
        this.unitService = unitService;
        this.categoryService = categoryService;
        this.quotationService = quotationService;
        this.workOrderService = workOrderService;
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Long propertyId,
                       @RequestParam(required = false) Long categoryId,
                       @RequestParam(required = false) Priority priority,
                       @RequestParam(required = false) RequestStatus status,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("reportedDate").descending());
        Page<MaintenanceRequest> result = requestService.search(keyword, propertyId, categoryId,
                priority, status, fromDate, toDate, pageable);

        model.addAttribute("requests", result);
        model.addAttribute("keyword", keyword);
        model.addAttribute("propertyId", propertyId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("priority", priority);
        model.addAttribute("status", status);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("properties", propertyService.findAll());
        model.addAttribute("categories", categoryService.findActive());
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("statuses", RequestStatus.values());
        return "maintenance/list";
    }

    @GetMapping("/create")
    public String createForm(@RequestParam(required = false) Long propertyId, Model model) {
        MaintenanceRequestForm form = new MaintenanceRequestForm();
        form.setPropertyId(propertyId);
        model.addAttribute("requestForm", form);
        addFormAttributes(model, propertyId);
        return "maintenance/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("requestForm") MaintenanceRequestForm form,
                         BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addFormAttributes(model, form.getPropertyId());
            return "maintenance/form";
        }
        MaintenanceRequest saved = requestService.create(form);
        redirectAttributes.addFlashAttribute("success", "Đã tạo yêu cầu " + saved.getRequestCode());
        return "redirect:/maintenance/" + saved.getId();
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        MaintenanceRequest request = requestService.findById(id);
        model.addAttribute("request", request);
        model.addAttribute("quotations", quotationService.findByRequest(id));
        model.addAttribute("workOrders", workOrderService.findByRequest(id));
        model.addAttribute("timeline", auditLogService.findTimeline("MaintenanceRequest", id));
        return "maintenance/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        MaintenanceRequest request = requestService.findById(id);
        MaintenanceRequestForm form = requestService.toForm(request);
        model.addAttribute("requestForm", form);
        addFormAttributes(model, form.getPropertyId());
        return "maintenance/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("requestForm") MaintenanceRequestForm form,
                         BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addFormAttributes(model, form.getPropertyId());
            return "maintenance/form";
        }
        MaintenanceRequest saved = requestService.update(id, form);
        redirectAttributes.addFlashAttribute("success", "Đã cập nhật yêu cầu " + saved.getRequestCode());
        return "redirect:/maintenance/" + saved.getId();
    }

    @PostMapping("/{id}/close")
    public String close(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        MaintenanceRequest saved = requestService.close(id);
        redirectAttributes.addFlashAttribute("success", "Đã đóng yêu cầu " + saved.getRequestCode());
        return "redirect:/maintenance/" + id;
    }

    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        MaintenanceRequest saved = requestService.cancel(id);
        redirectAttributes.addFlashAttribute("success", "Đã hủy yêu cầu " + saved.getRequestCode());
        return "redirect:/maintenance/" + id;
    }

    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable Long id, @RequestParam RequestStatus newStatus,
                               RedirectAttributes redirectAttributes) {
        requestService.changeStatus(id, newStatus);
        redirectAttributes.addFlashAttribute("success", "Đã chuyển trạng thái sang " + newStatus.getLabel());
        return "redirect:/maintenance/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        requestService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Đã xóa yêu cầu bảo trì");
        return "redirect:/maintenance";
    }

    private void addFormAttributes(Model model, Long propertyId) {
        model.addAttribute("properties", propertyService.findAll());
        model.addAttribute("units", propertyId != null ? unitService.findByProperty(propertyId) : java.util.List.of());
        model.addAttribute("categories", categoryService.findActive());
        model.addAttribute("priorities", Priority.values());
    }
}
