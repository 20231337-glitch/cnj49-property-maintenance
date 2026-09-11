package com.cnj49.propertymaintenance.service;

import com.cnj49.propertymaintenance.dto.MaintenanceRequestForm;
import com.cnj49.propertymaintenance.entity.*;
import com.cnj49.propertymaintenance.enums.Priority;
import com.cnj49.propertymaintenance.enums.RequestStatus;
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

/** TC02: tao yeu cau bao tri moi luon o trang thai NEW; validate du lieu dau vao. */
@SpringBootTest
@Transactional
class MaintenanceRequestServiceTest {

    @Autowired
    private MaintenanceRequestService requestService;
    @Autowired
    private TestFixtures fixtures;

    private Property property;
    private MaintenanceCategory category;

    @BeforeEach
    void setUp() {
        property = fixtures.property("PROP-T01");
        category = fixtures.category("Điện test");
    }

    @Test
    void create_savesRequestWithNewStatusAndGeneratedCode() {
        MaintenanceRequestForm form = validForm();

        MaintenanceRequest saved = requestService.create(form);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getStatus()).isEqualTo(RequestStatus.NEW);
        assertThat(saved.getRequestCode()).startsWith("MR-");
    }

    @Test
    void create_rejectsExpectedDateBeforeReportedDate() {
        MaintenanceRequestForm form = validForm();
        form.setReportedDate(LocalDate.of(2026, 6, 10));
        form.setExpectedCompletionDate(LocalDate.of(2026, 6, 5));

        assertThatThrownBy(() -> requestService.create(form))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("BR06");
    }

    @Test
    void create_rejectsNegativeEstimatedCost() {
        MaintenanceRequestForm form = validForm();
        form.setEstimatedCost(BigDecimal.valueOf(-1000));

        assertThatThrownBy(() -> requestService.create(form))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("BR07");
    }

    private MaintenanceRequestForm validForm() {
        MaintenanceRequestForm form = new MaintenanceRequestForm();
        form.setPropertyId(property.getId());
        form.setCategoryId(category.getId());
        form.setTitle("Điều hòa không làm lạnh");
        form.setDescription("Điều hòa phòng 302 không làm lạnh");
        form.setPriority(Priority.HIGH);
        form.setReportedDate(LocalDate.now());
        form.setEstimatedCost(BigDecimal.valueOf(1_500_000));
        return form;
    }
}
