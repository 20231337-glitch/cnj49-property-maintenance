package com.cnj49.propertymaintenance.controller;

import com.cnj49.propertymaintenance.dto.UnitForm;
import com.cnj49.propertymaintenance.entity.Unit;
import com.cnj49.propertymaintenance.enums.UnitStatus;
import com.cnj49.propertymaintenance.enums.UnitType;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Quan ly can/phong/khu vuc, loc theo bat dong san/tang/trang thai (muc 24). */
@Controller
@RequestMapping("/units")
public class UnitController {

    private final UnitService unitService;
    private final PropertyService propertyService;

    public UnitController(UnitService unitService, PropertyService propertyService) {
        this.unitService = unitService;
        this.propertyService = propertyService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Long propertyId,
                       @RequestParam(required = false) Integer floorNumber,
                       @RequestParam(required = false) UnitType unitType,
                       @RequestParam(required = false) UnitStatus status,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("floorNumber").ascending());
        Page<Unit> result = unitService.search(keyword, propertyId, floorNumber, unitType, status, pageable);

        model.addAttribute("units", result);
        model.addAttribute("keyword", keyword);
        model.addAttribute("propertyId", propertyId);
        model.addAttribute("floorNumber", floorNumber);
        model.addAttribute("unitType", unitType);
        model.addAttribute("status", status);
        model.addAttribute("properties", propertyService.findAll());
        model.addAttribute("floors", unitService.findDistinctFloors());
        model.addAttribute("unitTypes", UnitType.values());
        model.addAttribute("statuses", UnitStatus.values());
        return "units/list";
    }

    /** Dung cho dropdown can/phong phu thuoc bat dong san tren cac form khac (AJAX). */
    @GetMapping("/by-property/{propertyId}")
    @ResponseBody
    public List<Map<String, Object>> byProperty(@PathVariable Long propertyId) {
        return unitService.findByProperty(propertyId).stream()
                .map(u -> {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("id", u.getId());
                    item.put("name", u.getName());
                    return item;
                })
                .toList();
    }

    @GetMapping("/create")
    public String createForm(@RequestParam(required = false) Long propertyId, Model model) {
        UnitForm form = new UnitForm();
        form.setPropertyId(propertyId);
        model.addAttribute("unitForm", form);
        addFormAttributes(model);
        return "units/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("unitForm") UnitForm form,
                         BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addFormAttributes(model);
            return "units/form";
        }
        Unit saved = unitService.save(form);
        redirectAttributes.addFlashAttribute("success", "Đã thêm căn/phòng " + saved.getName());
        return "redirect:/units/" + saved.getId();
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("unit", unitService.findById(id));
        return "units/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Unit unit = unitService.findById(id);
        model.addAttribute("unitForm", unitService.toForm(unit));
        addFormAttributes(model);
        return "units/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("unitForm") UnitForm form,
                         BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addFormAttributes(model);
            return "units/form";
        }
        form.setId(id);
        Unit saved = unitService.save(form);
        redirectAttributes.addFlashAttribute("success", "Đã cập nhật căn/phòng " + saved.getName());
        return "redirect:/units/" + saved.getId();
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        unitService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Đã xóa căn/phòng");
        return "redirect:/units";
    }

    private void addFormAttributes(Model model) {
        model.addAttribute("properties", propertyService.findAll());
        model.addAttribute("unitTypes", UnitType.values());
        model.addAttribute("statuses", UnitStatus.values());
    }
}
