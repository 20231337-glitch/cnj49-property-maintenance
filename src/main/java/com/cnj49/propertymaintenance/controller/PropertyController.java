package com.cnj49.propertymaintenance.controller;

import com.cnj49.propertymaintenance.entity.Property;
import com.cnj49.propertymaintenance.enums.PropertyStatus;
import com.cnj49.propertymaintenance.enums.PropertyType;
import com.cnj49.propertymaintenance.service.ExpenseService;
import com.cnj49.propertymaintenance.service.MaintenanceRequestService;
import com.cnj49.propertymaintenance.service.PropertyService;
import com.cnj49.propertymaintenance.service.UnitService;
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

/** Quan ly bat dong san: danh sach + tim kiem/loc/phan trang, them, sua, xem chi tiet, xoa (muc 23). */
@Controller
@RequestMapping("/properties")
public class PropertyController {

    private final PropertyService propertyService;
    private final UnitService unitService;
    private final MaintenanceRequestService requestService;
    private final ExpenseService expenseService;

    public PropertyController(PropertyService propertyService, UnitService unitService,
                              MaintenanceRequestService requestService, ExpenseService expenseService) {
        this.propertyService = propertyService;
        this.unitService = unitService;
        this.requestService = requestService;
        this.expenseService = expenseService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) PropertyType propertyType,
                       @RequestParam(required = false) PropertyStatus status,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("name").ascending());
        Page<Property> result = propertyService.search(keyword, propertyType, status, pageable);

        model.addAttribute("properties", result);
        model.addAttribute("keyword", keyword);
        model.addAttribute("propertyType", propertyType);
        model.addAttribute("status", status);
        model.addAttribute("propertyTypes", PropertyType.values());
        model.addAttribute("statuses", PropertyStatus.values());
        return "properties/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("property", new Property());
        model.addAttribute("propertyTypes", PropertyType.values());
        model.addAttribute("statuses", PropertyStatus.values());
        return "properties/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("property") Property property,
                         BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("propertyTypes", PropertyType.values());
            model.addAttribute("statuses", PropertyStatus.values());
            return "properties/form";
        }
        Property saved = propertyService.create(property);
        redirectAttributes.addFlashAttribute("success", "Đã thêm bất động sản " + saved.getName());
        return "redirect:/properties/" + saved.getId();
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Property property = propertyService.findById(id);
        model.addAttribute("property", property);
        model.addAttribute("units", unitService.findByProperty(id));
        model.addAttribute("requests", requestService.findByProperty(id));
        model.addAttribute("totalExpense", expenseService.sumByProperty(id));
        return "properties/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("property", propertyService.findById(id));
        model.addAttribute("propertyTypes", PropertyType.values());
        model.addAttribute("statuses", PropertyStatus.values());
        return "properties/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("property") Property property,
                         BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("propertyTypes", PropertyType.values());
            model.addAttribute("statuses", PropertyStatus.values());
            return "properties/form";
        }
        Property saved = propertyService.update(id, property);
        redirectAttributes.addFlashAttribute("success", "Đã cập nhật bất động sản " + saved.getName());
        return "redirect:/properties/" + saved.getId();
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        propertyService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Đã xóa bất động sản");
        return "redirect:/properties";
    }
}
