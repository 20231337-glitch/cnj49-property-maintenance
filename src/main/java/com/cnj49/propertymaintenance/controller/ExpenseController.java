package com.cnj49.propertymaintenance.controller;

import com.cnj49.propertymaintenance.dto.ExpenseForm;
import com.cnj49.propertymaintenance.entity.Expense;
import com.cnj49.propertymaintenance.enums.ExpenseType;
import com.cnj49.propertymaintenance.service.ContractorService;
import com.cnj49.propertymaintenance.service.ExpenseService;
import com.cnj49.propertymaintenance.service.PropertyService;
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

/** Quan ly chi phi van hanh (muc 30). */
@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final PropertyService propertyService;
    private final ContractorService contractorService;

    public ExpenseController(ExpenseService expenseService, PropertyService propertyService,
                             ContractorService contractorService) {
        this.expenseService = expenseService;
        this.propertyService = propertyService;
        this.contractorService = contractorService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Long propertyId,
                       @RequestParam(required = false) Long contractorId,
                       @RequestParam(required = false) ExpenseType expenseType,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("expenseDate").descending());
        Page<Expense> result = expenseService.search(keyword, propertyId, contractorId, expenseType,
                fromDate, toDate, pageable);

        model.addAttribute("expenses", result);
        model.addAttribute("summary", expenseService.summarize(keyword, propertyId, contractorId, expenseType,
                fromDate, toDate));
        model.addAttribute("keyword", keyword);
        model.addAttribute("propertyId", propertyId);
        model.addAttribute("contractorId", contractorId);
        model.addAttribute("expenseType", expenseType);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("properties", propertyService.findAll());
        model.addAttribute("contractors", contractorService.findAll());
        model.addAttribute("expenseTypes", ExpenseType.values());
        return "expenses/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("expenseForm", new ExpenseForm());
        addFormAttributes(model);
        return "expenses/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("expenseForm") ExpenseForm form,
                         BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addFormAttributes(model);
            return "expenses/form";
        }
        Expense saved = expenseService.create(form);
        redirectAttributes.addFlashAttribute("success", "Đã ghi nhận chi phí " + saved.getExpenseCode());
        return "redirect:/expenses";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("expense", expenseService.findById(id));
        return "expenses/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("expenseForm", expenseService.toForm(expenseService.findById(id)));
        model.addAttribute("expenseId", id);
        addFormAttributes(model);
        return "expenses/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("expenseForm") ExpenseForm form,
                         BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("expenseId", id);
            addFormAttributes(model);
            return "expenses/form";
        }
        Expense saved = expenseService.update(id, form);
        redirectAttributes.addFlashAttribute("success", "Đã cập nhật chi phí " + saved.getExpenseCode());
        return "redirect:/expenses";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        expenseService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Đã xóa chi phí");
        return "redirect:/expenses";
    }

    private void addFormAttributes(Model model) {
        model.addAttribute("properties", propertyService.findAll());
        model.addAttribute("contractors", contractorService.findAll());
        model.addAttribute("expenseTypes", ExpenseType.values());
    }
}
