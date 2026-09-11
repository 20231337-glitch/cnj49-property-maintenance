package com.cnj49.propertymaintenance.controller;

import com.cnj49.propertymaintenance.entity.Contractor;
import com.cnj49.propertymaintenance.enums.ContractorStatus;
import com.cnj49.propertymaintenance.enums.Specialization;
import com.cnj49.propertymaintenance.service.ContractorService;
import com.cnj49.propertymaintenance.service.QuotationService;
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

/** Quan ly nha thau: danh sach, chi tiet kem thong ke hop tac (muc 26). */
@Controller
@RequestMapping("/contractors")
public class ContractorController {

    private final ContractorService contractorService;
    private final QuotationService quotationService;
    private final WorkOrderService workOrderService;

    public ContractorController(ContractorService contractorService, QuotationService quotationService,
                                WorkOrderService workOrderService) {
        this.contractorService = contractorService;
        this.quotationService = quotationService;
        this.workOrderService = workOrderService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Specialization specialization,
                       @RequestParam(required = false) ContractorStatus status,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("companyName").ascending());
        Page<Contractor> result = contractorService.search(keyword, specialization, status, pageable);

        model.addAttribute("contractors", result);
        model.addAttribute("keyword", keyword);
        model.addAttribute("specialization", specialization);
        model.addAttribute("status", status);
        model.addAttribute("specializations", Specialization.values());
        model.addAttribute("statuses", ContractorStatus.values());
        return "contractors/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("contractor", new Contractor());
        model.addAttribute("specializations", Specialization.values());
        model.addAttribute("statuses", ContractorStatus.values());
        return "contractors/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("contractor") Contractor contractor,
                         BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("specializations", Specialization.values());
            model.addAttribute("statuses", ContractorStatus.values());
            return "contractors/form";
        }
        Contractor saved = contractorService.create(contractor);
        redirectAttributes.addFlashAttribute("success", "Đã thêm nhà thầu " + saved.getCompanyName());
        return "redirect:/contractors/" + saved.getId();
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Contractor contractor = contractorService.findById(id);
        model.addAttribute("contractor", contractor);
        model.addAttribute("statistics", contractorService.getStatistics(id));
        model.addAttribute("quotations", quotationService.findByContractor(id));
        model.addAttribute("workOrders", workOrderService.findByContractor(id));
        return "contractors/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("contractor", contractorService.findById(id));
        model.addAttribute("specializations", Specialization.values());
        model.addAttribute("statuses", ContractorStatus.values());
        return "contractors/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("contractor") Contractor contractor,
                         BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("specializations", Specialization.values());
            model.addAttribute("statuses", ContractorStatus.values());
            return "contractors/form";
        }
        Contractor saved = contractorService.update(id, contractor);
        redirectAttributes.addFlashAttribute("success", "Đã cập nhật nhà thầu " + saved.getCompanyName());
        return "redirect:/contractors/" + saved.getId();
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        contractorService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Đã xóa nhà thầu");
        return "redirect:/contractors";
    }
}
