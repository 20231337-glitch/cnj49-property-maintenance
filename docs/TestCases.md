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

## TC14 - Du bao gia 5 nam theo ty le tang gia (Bao cao 8)
- File: `ReportServiceTest.propertyPriceProjection_appliesAnnualRateCompoundedOverFiveYears`
- Ky vong: gia 100.000.000, tang 10%/nam -> sau 5 nam 161.051.000, tang 1,61 lan.

## TC15 - Bo qua bat dong san chua khai bao gia
- File: `ReportServiceTest.propertyPriceProjection_skipsPropertiesWithoutBasePrice`
- Ky vong: bat dong san khong co `basePrice` khong xuat hien trong bao cao du bao.

## TC16 - Bieu do tong gia tri danh muc 6 moc
- File: `ReportServiceTest.portfolioValueByYear_returnsSixPoints_currentPlusFiveYears`
- Ky vong: nhan "Hien tai", "Nam 1" ... "Nam 5".

## TC17 - Trang bao cao hien thi Bao cao 8
- File: `ReportControllerSmokeTest.reportsPage_rendersPriceProjectionSection`
- Ky vong: GET `/reports` tra ve HTTP 200, co muc "Du bao tang gia bat dong san".

## TC18 - Form sua bat dong san hien dung ngay van hanh
- File: `PropertyControllerTest.editForm_rendersOperationDateInIsoFormatForDateInput`
- Ky vong: o `operationDate` co `value="yyyy-MM-dd"` de trinh duyet hien thi dung ngay.

## TC19 - Sua bat dong san luu dung ngay va gia
- File: `PropertyControllerTest.update_savesOperationDateAndPriceFieldsFromForm`
- Ky vong: sau khi luu, `operationDate`, `basePrice`, `annualIncreaseRate` dung voi gia tri nhap.

## TC20 - STAFF bi chan cac thao tac quan ly
- File: `RoleAuthorizationTest.staff_isDeniedManagementActions` (25 duong dan)
- Ky vong: duyet/tu choi bao gia, tao/huy phieu, nghiem thu, chi phi, sua danh muc, xoa -> HTTP 403.

## TC21 - STAFF tao va theo doi yeu cau
- File: `RoleAuthorizationTest.staff_canCreateAndFollowRequests` (14 duong dan)
- Ky vong: xem du lieu, tao/sua yeu cau, nhap bao gia, cap nhat tien do phieu -> khong bi chan.

## TC22 - MANAGER duyet va quan ly danh muc
- File: `RoleAuthorizationTest.manager_canApproveInspectAndManageMasterData` (11 duong dan)
- Ky vong: khong bi chan.

## TC23 - MANAGER khong duoc xoa
- File: `RoleAuthorizationTest.manager_cannotDelete` (7 duong dan)
- Ky vong: HTTP 403.

## TC24 - ADMIN duoc xoa
- File: `RoleAuthorizationTest.admin_canDelete` (4 duong dan)
- Ky vong: khong bi chan.

## TC25 - Trang 403
- File: `RoleAuthorizationTest.accessDeniedPage_rendersForbiddenMessage`
- Ky vong: `/errors/403` hien thong bao "Khong co quyen truy cap".
