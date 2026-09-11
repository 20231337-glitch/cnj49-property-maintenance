package com.cnj49.propertymaintenance.service;

import com.cnj49.propertymaintenance.dto.ExpenseForm;
import com.cnj49.propertymaintenance.dto.ExpenseSummary;
import com.cnj49.propertymaintenance.entity.Expense;
import com.cnj49.propertymaintenance.enums.ExpenseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Quan ly chi phi van hanh. */
public interface ExpenseService {

    Page<Expense> search(String keyword, Long propertyId, Long contractorId, ExpenseType type,
                         LocalDate fromDate, LocalDate toDate, Pageable pageable);

    ExpenseSummary summarize(String keyword, Long propertyId, Long contractorId, ExpenseType type,
                             LocalDate fromDate, LocalDate toDate);

    Expense findById(Long id);

    ExpenseForm toForm(Expense expense);

    /** BR07: so tien khong duoc am. */
    Expense create(ExpenseForm form);

    Expense update(Long id, ExpenseForm form);

    void delete(Long id);

    BigDecimal sumByProperty(Long propertyId);

    BigDecimal sumCurrentMonth();
}
