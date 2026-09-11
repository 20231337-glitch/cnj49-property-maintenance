package com.cnj49.propertymaintenance.controller;

import com.cnj49.propertymaintenance.dto.InspectionForm;
import com.cnj49.propertymaintenance.entity.Inspection;
import com.cnj49.propertymaintenance.enums.InspectionResult;
import com.cnj49.propertymaintenance.service.InspectionService;
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
 * Quan ly nghiem thu cong viec (muc 29).
 * inspect() la noi thuc thi BR04, BR12, BR13.
 */
@Controller
@RequestMapping("/inspections")
public class InspectionController {

    private final InspectionService inspectionService;
    private final WorkOrderService workOrderService;

    public InspectionController(InspectionService inspectionService, WorkOrderService workOrderService) {
        this.inspectionService = inspectionService;
        this.workOrderService = workOrderService;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("inspectionDate").descending());
        Page<Inspection> result = inspectionService.findAll(pageable);
        model.addAttribute("inspections", result);
        model.addAttribute("awaitingWorkOrders", workOrderService.findAwaitingInspection());
        return "inspections/list";
    }

    @GetMapping("/create")
    public String createForm(@RequestParam Long workOrderId, Model model) {
        InspectionForm form = new InspectionForm();
        form.setWorkOrderId(workOrderId);
        model.addAttribute("inspectionForm", form);
        model.addAttribute("workOrder", workOrderService.findById(workOrderId));
        model.addAttribute("results", InspectionResult.values());
        return "inspections/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("inspectionForm") InspectionForm form,
                         BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("workOrder", workOrderService.findById(form.getWorkOrderId()));
            model.addAttribute("results", InspectionResult.values());
            return "inspections/form";
        }
        Inspection saved = inspectionService.inspect(form);
        String message = saved.getResult() == InspectionResult.PASSED
                ? "Nghiệm thu ĐẠT. Yêu cầu bảo trì đã hoàn thành."
                : "Nghiệm thu KHÔNG ĐẠT. Công việc chuyển về trạng thái đang thực hiện.";
        redirectAttributes.addFlashAttribute("success", message);
        return "redirect:/workorders/" + saved.getWorkOrder().getId();
    }
}
