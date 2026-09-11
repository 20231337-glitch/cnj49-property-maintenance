package com.cnj49.propertymaintenance.controller;

import com.cnj49.propertymaintenance.entity.MaintenanceCategory;
import com.cnj49.propertymaintenance.service.MaintenanceCategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Quan ly danh muc hang muc bao tri (Dien, Nuoc, Dieu hoa...). */
@Controller
@RequestMapping("/categories")
public class MaintenanceCategoryController {

    private final MaintenanceCategoryService categoryService;

    public MaintenanceCategoryController(MaintenanceCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "categories/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("category", new MaintenanceCategory());
        return "categories/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("category") MaintenanceCategory category,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "categories/form";
        }
        categoryService.save(category);
        redirectAttributes.addFlashAttribute("success", "Đã thêm hạng mục " + category.getName());
        return "redirect:/categories";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoryService.findById(id));
        return "categories/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("category") MaintenanceCategory category,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "categories/form";
        }
        category.setId(id);
        categoryService.save(category);
        redirectAttributes.addFlashAttribute("success", "Đã cập nhật hạng mục " + category.getName());
        return "redirect:/categories";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        categoryService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Đã xóa hạng mục");
        return "redirect:/categories";
    }
}
