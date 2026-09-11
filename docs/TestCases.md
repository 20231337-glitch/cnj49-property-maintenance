# Test Cases - CNJ49 Property Maintenance

Cac test case duoc tu dong hoa trong `src/test/java` (JUnit 5 + H2 in-memory), chay bang `mvn test`.

## TC01 - Tao bat dong san moi
- Buoc: goi `PropertyService.create()` voi du lieu hop le.
- Ky vong: Property duoc luu, tu sinh ma dang `PROP-xxxx`.

## TC02 - Tao yeu cau bao tri
- File: `MaintenanceRequestServiceTest.create_savesRequestWithNewStatusAndGeneratedCode`
- Ky vong: `status = NEW`, ma dang `MR-yyyy-xxxx`.

## TC02b - Validate du lieu yeu cau bao tri
- File: `create_rejectsExpectedDateBeforeReportedDate`, `create_rejectsNegativeEstimatedCost`
- Ky vong: nem `BusinessException` voi thong bao chua "BR06" / "BR07".

## TC03 - Them 3 bao gia
- File: `QuotationServiceTest.addingThreeQuotations_allLinkToSameRequest`
- Ky vong: ca 3 Quotation lien ket cung MaintenanceRequest.

## TC04 - Duyet bao gia va chong duyet trung
- File: `QuotationServiceTest.approve_setsApprovedAndRejectsOthers`,
  `approve_secondQuotation_throwsBusinessException`
- Ky vong: bao gia duoc duyet -> APPROVED, cac bao gia con lai -> REJECTED (BR03);
  duyet them mot bao gia thu hai cho cung yeu cau nem `BusinessException` chua "BR02".

## TC05 - Tao WorkOrder tu bao gia da duyet
- File: `WorkOrderServiceTest.create_start_complete_followsFullLifecycle`
- Ky vong: WorkOrder duoc tao dung Contractor cua Quotation da duyet (BR09).

## TC05b - Chan tao WorkOrder khi chua duyet bao gia
- File: `WorkOrderServiceTest.create_withoutApprovedQuotation_throwsBusinessException`
- Ky vong: nem `BusinessException` chua "BR01".

## TC06 - Hoan thanh WorkOrder
- File: `WorkOrderServiceTest.create_start_complete_followsFullLifecycle`
- Ky vong: sau `complete()`, MaintenanceRequest chuyen `WAITING_INSPECTION` (BR11).

## TC07 - Nghiem thu PASSED
- File: `InspectionServiceTest.inspect_passed_completesWorkOrderAndRequestAndRecordsExpense`
- Ky vong: `WorkOrder = ACCEPTED`, `MaintenanceRequest = COMPLETED`, chi phi thuc te duoc tu dong
  ghi nhan vao bang `expenses` (BR12).

## TC08 - Nghiem thu FAILED
- File: `InspectionServiceTest.inspect_failed_sendsWorkOrderAndRequestBackToInProgress`
- Ky vong: `WorkOrder = IN_PROGRESS`, `MaintenanceRequest = IN_PROGRESS` (BR13).

## TC09 - Chan nghiem thu WorkOrder chua hoan thanh
- File: `InspectionServiceTest.inspect_beforeWorkOrderCompleted_throwsBusinessException`
- Ky vong: nem `BusinessException` chua "BR04".

## TC10 - Chi phi khong duoc am
- File: `ExpenseServiceTest.create_negativeAmount_throwsBusinessException`
- Ky vong: nem `BusinessException` chua "BR07".
