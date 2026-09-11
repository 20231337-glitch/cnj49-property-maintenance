package com.cnj49.propertymaintenance.controller;

import com.cnj49.propertymaintenance.dto.QuotationForm;
import com.cnj49.propertymaintenance.entity.Quotation;
import com.cnj49.propertymaintenance.service.ContractorService;
import com.cnj49.propertymaintenance.service.QuotationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Quan ly bao gia gan tren tab "Bao gia" cua trang chi tiet yeu cau bao tri (muc 27).
 * approve() la noi thuc thi BR02 + BR03.
 */
@Controller
@RequestMapping("/maintenance/{requestId}/quotations")
public class QuotationController {

    private final QuotationService quotationService;
    private final ContractorService contractorService;

    public QuotationController(QuotationService quotationService, ContractorService contractorService) {
        this.quotationService = quotationService;
        this.contractorService = contractorService;
    }

    @GetMapping("/create")
    public String createForm(@PathVariable Long requestId, Model model) {
        QuotationForm form = new QuotationForm();
        form.setMaintenanceRequestId(requestId);
        model.addAttribute("quotationForm", form);
        model.addAttribute("requestId", requestId);
        model.addAttribute("contractors", contractorService.findActive());
        return "quotations/form";
    }

    @PostMapping
    public String create(@PathVariable Long requestId,
                         @Valid @ModelAttribute("quotationForm") QuotationForm form,
                         BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttributes) {
        form.setMaintenanceRequestId(requestId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("requestId", requestId);
            model.addAttribute("contractors", contractorService.findActive());
            return "quotations/form";
        }
        Quotation saved = quotationService.create(form);
        redirectAttributes.addFlashAttribute("success", "Đã thêm báo giá " + saved.getQuotationCode());
        return "redirect:/maintenance/" + requestId;
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long requestId, @PathVariable Long id, Model model) {
        Quotation quotation = quotationService.findById(id);
        model.addAttribute("quotationForm", quotationService.toForm(quotation));
        model.addAttribute("requestId", requestId);
        model.addAttribute("contractors", contractorService.findActive());
        return "quotations/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long requestId, @PathVariable Long id,
                         @Valid @ModelAttribute("quotationForm") QuotationForm form,
                         BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttributes) {
        form.setMaintenanceRequestId(requestId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("requestId", requestId);
            model.addAttribute("contractors", contractorService.findActive());
            return "quotations/form";
        }
        Quotation saved = quotationService.update(id, form);
        redirectAttributes.addFlashAttribute("success", "Đã cập nhật báo giá " + saved.getQuotationCode());
        return "redirect:/maintenance/" + requestId;
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long requestId, @PathVariable Long id,
                          RedirectAttributes redirectAttributes) {
        Quotation saved = quotationService.approve(id);
        redirectAttributes.addFlashAttribute("success",
                "Đã duyệt báo giá " + saved.getQuotationCode() + " của " + saved.getContractor().getCompanyName());
        return "redirect:/maintenance/" + requestId;
    }

    @PostMapping("/{id}/reject")
    public String reject(@PathVariable Long requestId, @PathVariable Long id,
                         RedirectAttributes redirectAttributes) {
        Quotation saved = quotationService.reject(id);
        redirectAttributes.addFlashAttribute("success", "Đã từ chối báo giá " + saved.getQuotationCode());
        return "redirect:/maintenance/" + requestId;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long requestId, @PathVariable Long id,
                         RedirectAttributes redirectAttributes) {
        quotationService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Đã xóa báo giá");
        return "redirect:/maintenance/" + requestId;
    }
}
