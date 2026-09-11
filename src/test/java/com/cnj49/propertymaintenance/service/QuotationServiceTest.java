package com.cnj49.propertymaintenance.service;

import com.cnj49.propertymaintenance.dto.MaintenanceRequestForm;
import com.cnj49.propertymaintenance.dto.QuotationForm;
import com.cnj49.propertymaintenance.entity.*;
import com.cnj49.propertymaintenance.enums.Priority;
import com.cnj49.propertymaintenance.enums.QuotationStatus;
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

/** TC03/TC04: them nhieu bao gia, duyet mot bao gia va ngan duyet bao gia thu hai (BR02/BR03). */
@SpringBootTest
@Transactional
class QuotationServiceTest {

    @Autowired
    private MaintenanceRequestService requestService;
    @Autowired
    private QuotationService quotationService;
    @Autowired
    private TestFixtures fixtures;

    private MaintenanceRequest request;
    private Contractor contractorA;
    private Contractor contractorB;
    private Contractor contractorC;

    @BeforeEach
    void setUp() {
        Property property = fixtures.property("PROP-T02");
        MaintenanceCategory category = fixtures.category("Điều hòa test");
        contractorA = fixtures.contractor("CTR-TA");
        contractorB = fixtures.contractor("CTR-TB");
        contractorC = fixtures.contractor("CTR-TC");

        MaintenanceRequestForm form = new MaintenanceRequestForm();
        form.setPropertyId(property.getId());
        form.setCategoryId(category.getId());
        form.setTitle("Điều hòa phòng 302 không làm lạnh");
        form.setDescription("Điều hòa phòng 302 không làm lạnh");
        form.setPriority(Priority.HIGH);
        form.setReportedDate(LocalDate.now());
        request = requestService.create(form);
    }

    @Test
    void addingThreeQuotations_allLinkToSameRequest() {
        quote(contractorA, 1_700_000);
        quote(contractorB, 1_350_000);
        quote(contractorC, 1_900_000);

        assertThat(quotationService.findByRequest(request.getId())).hasSize(3);
    }

    @Test
    void approve_setsApprovedAndRejectsOthers() {
        Quotation qA = quote(contractorA, 1_700_000);
        Quotation qB = quote(contractorB, 1_350_000);
        Quotation qC = quote(contractorC, 1_900_000);

        quotationService.approve(qB.getId());

        assertThat(quotationService.findById(qB.getId()).getStatus()).isEqualTo(QuotationStatus.APPROVED);
        assertThat(quotationService.findById(qA.getId()).getStatus()).isEqualTo(QuotationStatus.REJECTED);
        assertThat(quotationService.findById(qC.getId()).getStatus()).isEqualTo(QuotationStatus.REJECTED);
        assertThat(requestService.findById(request.getId()).getStatus()).isEqualTo(RequestStatus.QUOTATION_APPROVED);
    }

    @Test
    void approve_secondQuotation_throwsBusinessException() {
        Quotation qA = quote(contractorA, 1_700_000);
        quotationService.approve(qA.getId());

        // Bao gia moi them sau khi da co mot bao gia APPROVED (khong bi tu dong REJECTED boi BR03).
        Quotation qC = quote(contractorC, 1_900_000);

        assertThatThrownBy(() -> quotationService.approve(qC.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("BR02");
    }

    private Quotation quote(Contractor contractor, long total) {
        QuotationForm form = new QuotationForm();
        form.setMaintenanceRequestId(request.getId());
        form.setContractorId(contractor.getId());
        form.setQuotationDate(LocalDate.now());
        form.setMaterialCost(BigDecimal.valueOf(total - 200_000));
        form.setLaborCost(BigDecimal.valueOf(150_000));
        form.setOtherCost(BigDecimal.valueOf(50_000));
        form.setEstimatedDays(2);
        return quotationService.create(form);
    }
}
