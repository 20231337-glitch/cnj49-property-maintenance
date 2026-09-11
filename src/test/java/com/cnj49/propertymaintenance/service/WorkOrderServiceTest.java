package com.cnj49.propertymaintenance.service;

import com.cnj49.propertymaintenance.dto.MaintenanceRequestForm;
import com.cnj49.propertymaintenance.dto.QuotationForm;
import com.cnj49.propertymaintenance.dto.WorkOrderForm;
import com.cnj49.propertymaintenance.entity.*;
import com.cnj49.propertymaintenance.enums.Priority;
import com.cnj49.propertymaintenance.enums.RequestStatus;
import com.cnj49.propertymaintenance.enums.WorkOrderStatus;
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

/** TC05/TC06: khong tao WorkOrder khi chua duyet bao gia (BR01); start; complete (BR10/BR11). */
@SpringBootTest
@Transactional
class WorkOrderServiceTest {

    @Autowired
    private MaintenanceRequestService requestService;
    @Autowired
    private QuotationService quotationService;
    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private TestFixtures fixtures;

    private MaintenanceRequest request;
    private Contractor contractor;

    @BeforeEach
    void setUp() {
        Property property = fixtures.property("PROP-T03");
        MaintenanceCategory category = fixtures.category("Điện test WO");
        contractor = fixtures.contractor("CTR-TWO");

        MaintenanceRequestForm form = new MaintenanceRequestForm();
        form.setPropertyId(property.getId());
        form.setCategoryId(category.getId());
        form.setTitle("Mất điện toàn bộ tầng 3");
        form.setDescription("Mất điện toàn bộ tầng 3");
        form.setPriority(Priority.URGENT);
        form.setReportedDate(LocalDate.now());
        request = requestService.create(form);
    }

    @Test
    void create_withoutApprovedQuotation_throwsBusinessException() {
        WorkOrderForm form = workOrderForm();

        assertThatThrownBy(() -> workOrderService.create(form))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("BR01");
    }

    @Test
    void create_start_complete_followsFullLifecycle() {
        Quotation approved = approveQuotation();

        WorkOrder workOrder = workOrderService.create(workOrderForm());
        assertThat(workOrder.getStatus()).isEqualTo(WorkOrderStatus.NOT_STARTED);
        assertThat(workOrder.getContractor().getId()).isEqualTo(approved.getContractor().getId());

        workOrderService.start(workOrder.getId());
        assertThat(requestService.findById(request.getId()).getStatus()).isEqualTo(RequestStatus.IN_PROGRESS);

        WorkOrder completed = workOrderService.complete(workOrder.getId(), "Đã sửa xong");
        assertThat(completed.getStatus()).isEqualTo(WorkOrderStatus.COMPLETED);
        assertThat(requestService.findById(request.getId()).getStatus()).isEqualTo(RequestStatus.WAITING_INSPECTION);
    }

    private Quotation approveQuotation() {
        QuotationForm qForm = new QuotationForm();
        qForm.setMaintenanceRequestId(request.getId());
        qForm.setContractorId(contractor.getId());
        qForm.setQuotationDate(LocalDate.now());
        qForm.setMaterialCost(BigDecimal.valueOf(1_000_000));
        qForm.setLaborCost(BigDecimal.valueOf(300_000));
        qForm.setOtherCost(BigDecimal.valueOf(50_000));
        qForm.setEstimatedDays(2);
        Quotation quotation = quotationService.create(qForm);
        return quotationService.approve(quotation.getId());
    }

    private WorkOrderForm workOrderForm() {
        WorkOrderForm form = new WorkOrderForm();
        form.setMaintenanceRequestId(request.getId());
        form.setStartDate(LocalDate.now());
        form.setExpectedCompletionDate(LocalDate.now().plusDays(2));
        form.setWorkDescription("Kiểm tra và khắc phục sự cố mất điện");
        return form;
    }
}
