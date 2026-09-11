package com.cnj49.propertymaintenance.controller;

import com.cnj49.propertymaintenance.dto.WorkOrderForm;
import com.cnj49.propertymaintenance.entity.WorkOrder;
import com.cnj49.propertymaintenance.enums.WorkOrderStatus;
import com.cnj49.propertymaintenance.service.ContractorService;
import com.cnj49.propertymaintenance.service.MaintenanceRequestService;
import com.cnj49.propertymaintenance.service.WorkOrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Quan ly phieu cong viec (muc 28).
 * create() thuc thi BR01/BR06/BR09; cac action start/pause/resume/complete/cancel
 * dieu khien vong doi va dong bo trang thai MaintenanceRequest (BR10, BR11).
 */
@Controller
@RequestMapping("/workorders")
public class WorkOrderController {

    private final WorkOrderService workOrderService;
    private final MaintenanceRequestService requestService;
    private final ContractorService contractorService;

    public WorkOrderController(WorkOrderService workOrderService, MaintenanceRequestService requestService,
                               ContractorService contractorService) {
        this.workOrderService = workOrderService;
        this.requestService = requestService;
        this.contractorService = contractorService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Long contractorId,
                       @RequestParam(required = false) WorkOrderStatus status,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
        Page<WorkOrder> result = workOrderService.search(keyword, contractorId, status, pageable);

        model.addAttribute("workOrders", result);
        model.addAttribute("keyword", keyword);
        model.addAttribute("contractorId", contractorId);
        model.addAttribute("status", status);
        model.addAttribute("contractors", contractorService.findAll());
        model.addAttribute("statuses", WorkOrderStatus.values());
        return "workorders/list";
    }

    @GetMapping("/create")
    public String createForm(@RequestParam Long requestId, Model model) {
        WorkOrderForm form = new WorkOrderForm();
        form.setMaintenanceRequestId(requestId);
        model.addAttribute("workOrderForm", form);
        model.addAttribute("request", requestService.findById(requestId));
        return "workorders/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("workOrderForm") WorkOrderForm form,
                         BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("request", requestService.findById(form.getMaintenanceRequestId()));
            return "workorders/form";
        }
        WorkOrder saved = workOrderService.create(form);
        redirectAttributes.addFlashAttribute("success", "Đã tạo phiếu công việc " + saved.getWorkOrderCode());
        return "redirect:/workorders/" + saved.getId();
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("workOrder", workOrderService.findById(id));
        return "workorders/detail";
    }

    @PostMapping("/{id}/start")
    public String start(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        workOrderService.start(id);
        redirectAttributes.addFlashAttribute("success", "Đã bắt đầu thực hiện công việc");
        return "redirect:/workorders/" + id;
    }

    @PostMapping("/{id}/pause")
    public String pause(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        workOrderService.pause(id);
        redirectAttributes.addFlashAttribute("success", "Đã tạm dừng công việc");
        return "redirect:/workorders/" + id;
    }

    @PostMapping("/{id}/resume")
    public String resume(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        workOrderService.resume(id);
        redirectAttributes.addFlashAttribute("success", "Đã tiếp tục công việc");
        return "redirect:/workorders/" + id;
    }

    @PostMapping("/{id}/complete")
    public String complete(@PathVariable Long id, @RequestParam(required = false) String result,
                           RedirectAttributes redirectAttributes) {
        workOrderService.complete(id, result);
        redirectAttributes.addFlashAttribute("success", "Đã hoàn thành công việc, chờ nghiệm thu");
        return "redirect:/workorders/" + id;
    }

    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        workOrderService.cancel(id);
        redirectAttributes.addFlashAttribute("success", "Đã hủy phiếu công việc");
        return "redirect:/workorders/" + id;
    }
}
