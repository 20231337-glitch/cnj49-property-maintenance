package com.cnj49.propertymaintenance.service;

import com.cnj49.propertymaintenance.dto.ExpenseForm;
import com.cnj49.propertymaintenance.entity.Expense;
import com.cnj49.propertymaintenance.entity.Property;
import com.cnj49.propertymaintenance.enums.ExpenseType;
import com.cnj49.propertymaintenance.exception.BusinessException;
import com.cnj49.propertymaintenance.support.TestFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** BR07: so tien chi phi khong duoc am. */
@SpringBootTest
@Transactional
class ExpenseServiceTest {

    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private TestFixtures fixtures;

    private Property property;

    @BeforeEach
    void setUp() {
        property = fixtures.property("PROP-T05");
    }

    @Test
    void create_negativeAmount_throwsBusinessException() {
        ExpenseForm form = expenseForm(BigDecimal.valueOf(-500_000));

        assertThatThrownBy(() -> expenseService.create(form))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("BR07");
    }

    @Test
    void create_validAmount_savesExpenseWithGeneratedCode() {
        Expense saved = expenseService.create(expenseForm(BigDecimal.valueOf(1_200_000)));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getExpenseCode()).startsWith("EXP-");
        assertThat(saved.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(1_200_000));
    }

    private ExpenseForm expenseForm(BigDecimal amount) {
        ExpenseForm form = new ExpenseForm();
        form.setPropertyId(property.getId());
        form.setExpenseType(ExpenseType.UTILITY);
        form.setAmount(amount);
        form.setExpenseDate(LocalDate.now());
        form.setDescription("Tiền điện tháng test");
        return form;
    }
}
