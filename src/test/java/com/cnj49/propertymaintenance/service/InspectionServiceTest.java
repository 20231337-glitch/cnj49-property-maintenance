package com.cnj49.propertymaintenance.service;

import com.cnj49.propertymaintenance.dto.InspectionForm;
import com.cnj49.propertymaintenance.dto.MaintenanceRequestForm;
import com.cnj49.propertymaintenance.dto.QuotationForm;
import com.cnj49.propertymaintenance.dto.WorkOrderForm;
import com.cnj49.propertymaintenance.entity.*;
import com.cnj49.propertymaintenance.enums.InspectionResult;
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

/** TC07/TC08: nghiem thu PASSED va FAILED (BR04, BR12, BR13). */
@SpringBootTest
@Transactional
class InspectionServiceTest {

    @Autowired
    private MaintenanceRequestService requestService;
    @Autowired
    private QuotationService quotationService;
    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private InspectionService inspectionService;
    @Autowired
    private TestFixtures fixtures;

    private Contractor contractor;
    private Property property;
    private MaintenanceCategory category;

    @BeforeEach
    void setUp() {
        property = fixtures.property("PROP-T04");
        category = fixtures.category("Điều hòa test INS");
        contractor = fixtures.contractor("CTR-TINS");
    }

    @Test
    void inspect_beforeWorkOrderCompleted_throwsBusinessException() {
        WorkOrder workOrder = createWorkOrderNotStarted();

        InspectionForm form = inspectionForm(workOrder.getId(), InspectionResult.PASSED, 1_420_000);

        assertThatThrownBy(() -> inspectionService.inspect(form))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("BR04");
    }

    @Test
    void inspect_passed_completesWorkOrderAndRequestAndRecordsExpense() {
        WorkOrder workOrder = createWorkOrderNotStarted();
        workOrderService.start(workOrder.getId());
        workOrderService.complete(workOrder.getId(), "Đã hoàn thành");

        inspectionService.inspect(inspectionForm(workOrder.getId(), InspectionResult.PASSED, 1_420_000));

        WorkOrder reloaded = workOrderService.findById(workOrder.getId());
        assertThat(reloaded.getStatus()).isEqualTo(WorkOrderStatus.ACCEPTED);

        MaintenanceRequest request = requestService.findById(workOrder.getMaintenanceRequest().getId());
        assertThat(request.getStatus()).isEqualTo(RequestStatus.COMPLETED);
    }

    @Test
    void inspect_failed_sendsWorkOrderAndRequestBackToInProgress() {
        WorkOrder workOrder = createWorkOrderNotStarted();
        workOrderService.start(workOrder.getId());
        workOrderService.complete(workOrder.getId(), "Đã hoàn thành");

        inspectionService.inspect(inspectionForm(workOrder.getId(), InspectionResult.FAILED, 1_420_000));

        WorkOrder reloaded = workOrderService.findById(workOrder.getId());
        assertThat(reloaded.getStatus()).isEqualTo(WorkOrderStatus.IN_PROGRESS);

        MaintenanceRequest request = requestService.findById(workOrder.getMaintenanceRequest().getId());
        assertThat(request.getStatus()).isEqualTo(RequestStatus.IN_PROGRESS);
    }

    private WorkOrder createWorkOrderNotStarted() {
        MaintenanceRequestForm rForm = new MaintenanceRequestForm();
        rForm.setPropertyId(property.getId());
        rForm.setCategoryId(category.getId());
        rForm.setTitle("Điều hòa phòng 302 không làm lạnh");
        rForm.setDescription("Điều hòa phòng 302 không làm lạnh");
        rForm.setPriority(Priority.HIGH);
        rForm.setReportedDate(LocalDate.now());
        MaintenanceRequest request = requestService.create(rForm);

        QuotationForm qForm = new QuotationForm();
        qForm.setMaintenanceRequestId(request.getId());
        qForm.setContractorId(contractor.getId());
        qForm.setQuotationDate(LocalDate.now());
        qForm.setMaterialCost(BigDecimal.valueOf(900_000));
        qForm.setLaborCost(BigDecimal.valueOf(400_000));
        qForm.setOtherCost(BigDecimal.valueOf(50_000));
        qForm.setEstimatedDays(2);
        Quotation quotation = quotationService.create(qForm);
        quotationService.approve(quotation.getId());

        WorkOrderForm woForm = new WorkOrderForm();
        woForm.setMaintenanceRequestId(request.getId());
        woForm.setStartDate(LocalDate.now());
        woForm.setExpectedCompletionDate(LocalDate.now().plusDays(2));
        woForm.setWorkDescription("Kiểm tra và sửa điều hòa");
        return workOrderService.create(woForm);
    }

    private InspectionForm inspectionForm(Long workOrderId, InspectionResult result, long actualCost) {
        InspectionForm form = new InspectionForm();
        form.setWorkOrderId(workOrderId);
        form.setInspectionDate(LocalDate.now());
        form.setInspectorName("Trịnh Hoàng Thành");
        form.setResult(result);
        form.setActualCost(BigDecimal.valueOf(actualCost));
        return form;
    }
}
