package com.cnj49.propertymaintenance.config;

import com.cnj49.propertymaintenance.entity.*;
import com.cnj49.propertymaintenance.enums.*;
import com.cnj49.propertymaintenance.repository.*;
import com.cnj49.propertymaintenance.util.CodeGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Seed du lieu demo khi database rong (idempotent: chi chay neu chua co du lieu).
 * Muc tieu (muc 39): 4 Properties, 25 Units, 10 categories, 10 contractors,
 * 35 maintenance requests, 40 quotations, 15 work orders, 10 inspections, 30 expenses,
 * lien ket hop ly theo dung workflow nghiep vu.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final PropertyRepository propertyRepository;
    private final UnitRepository unitRepository;
    private final MaintenanceCategoryRepository categoryRepository;
    private final ContractorRepository contractorRepository;
    private final MaintenanceRequestRepository requestRepository;
    private final QuotationRepository quotationRepository;
    private final WorkOrderRepository workOrderRepository;
    private final InspectionRepository inspectionRepository;
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CodeGenerator codeGenerator;
    private final PasswordEncoder passwordEncoder;

    private final Random random = new Random(42);

    public DataSeeder(PropertyRepository propertyRepository, UnitRepository unitRepository,
                      MaintenanceCategoryRepository categoryRepository, ContractorRepository contractorRepository,
                      MaintenanceRequestRepository requestRepository, QuotationRepository quotationRepository,
                      WorkOrderRepository workOrderRepository, InspectionRepository inspectionRepository,
                      ExpenseRepository expenseRepository, UserRepository userRepository,
                      CodeGenerator codeGenerator, PasswordEncoder passwordEncoder) {
        this.propertyRepository = propertyRepository;
        this.unitRepository = unitRepository;
        this.categoryRepository = categoryRepository;
        this.contractorRepository = contractorRepository;
        this.requestRepository = requestRepository;
        this.quotationRepository = quotationRepository;
        this.workOrderRepository = workOrderRepository;
        this.inspectionRepository = inspectionRepository;
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.codeGenerator = codeGenerator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        seedUsers();
        if (propertyRepository.count() > 0) {
            return; // Da co du lieu - khong seed lai (idempotent).
        }
        List<MaintenanceCategory> categories = seedCategories();
        List<Contractor> contractors = seedContractors();
        List<Property> properties = seedProperties();
        List<Unit> units = seedUnits(properties);
        seedWorkflow(properties, units, categories, contractors);
    }

    private void seedUsers() {
        if (userRepository.existsByUsername("admin")) {
            return;
        }
        userRepository.save(new User("admin", passwordEncoder.encode("admin123"),
                "Quản trị viên hệ thống", "admin@cnj49.local", Role.ADMIN));
        userRepository.save(new User("manager", passwordEncoder.encode("manager123"),
                "Trần Quản Lý", "manager@cnj49.local", Role.MANAGER));
        userRepository.save(new User("staff", passwordEncoder.encode("staff123"),
                "Nguyễn Nhân Viên", "staff@cnj49.local", Role.STAFF));
    }

    private List<MaintenanceCategory> seedCategories() {
        String[] names = {"Điện", "Nước", "Điều hòa", "Thang máy", "PCCC", "Camera", "Nội thất", "Xây dựng", "Thiết bị", "Vệ sinh"};
        List<MaintenanceCategory> saved = new ArrayList<>();
        for (String name : names) {
            saved.add(categoryRepository.save(new MaintenanceCategory(name, "Hạng mục bảo trì: " + name)));
        }
        return saved;
    }

    private List<Contractor> seedContractors() {
        String[][] data = {
                {"Điện lạnh Hà Nội", "Nguyễn Văn An", "0912345001", "HVAC"},
                {"Điện lạnh Thành Công", "Lê Thị Bình", "0912345002", "HVAC"},
                {"Công ty Điện nước Sài Gòn", "Trần Văn Cường", "0912345003", "PLUMBING"},
                {"Thang máy Thiên Nam", "Phạm Thị Dung", "0912345004", "ELEVATOR"},
                {"PCCC An Toàn", "Hoàng Văn Em", "0912345005", "FIRE_SAFETY"},
                {"Xây dựng Việt Tiến", "Vũ Thị Giang", "0912345006", "CONSTRUCTION"},
                {"Nội thất Hoàng Gia", "Đặng Văn Hùng", "0912345007", "INTERIOR"},
                {"Điện Quang Minh", "Bùi Thị Lan", "0912345008", "ELECTRICAL"},
                {"Thiết bị công nghiệp Đông Á", "Ngô Văn Khoa", "0912345009", "EQUIPMENT"},
                {"Dịch vụ tổng hợp Phú Quý", "Đinh Thị Mai", "0912345010", "MULTI_SERVICE"}
        };
        List<Contractor> saved = new ArrayList<>();
        for (String[] row : data) {
            Contractor c = new Contractor();
            c.setContractorCode(codeGenerator.nextContractorCode());
            c.setCompanyName(row[0]);
            c.setContactPerson(row[1]);
            c.setPhone(row[2]);
            c.setEmail(row[1].toLowerCase().replace(" ", ".") + "@contractor.vn");
            c.setAddress("Hà Nội, Việt Nam");
            c.setTaxCode("MST" + (1000 + saved.size()));
            c.setSpecialization(Specialization.valueOf(row[3]));
            c.setRating(BigDecimal.valueOf(3.5 + random.nextInt(15) / 10.0).setScale(1, java.math.RoundingMode.HALF_UP));
            c.setStatus(ContractorStatus.ACTIVE);
            saved.add(contractorRepository.save(c));
        }
        return saved;
    }

    private List<Property> seedProperties() {
        Object[][] data = {
                {"Chung cư Mini Cầu Giấy", PropertyType.MINI_APARTMENT, "Số 12 Cầu Giấy, Hà Nội", 5, 20, "80"},
                {"Nhà trọ Mỹ Đình", PropertyType.BOARDING_HOUSE, "Ngõ 55 Mỹ Đình, Hà Nội", 3, 15, "45"},
                {"Căn hộ cho thuê Hà Đông", PropertyType.RENTAL_HOUSE, "Đường Quang Trung, Hà Đông, Hà Nội", 4, 12, "60"},
                {"Chung cư Botanica Cầu Giấy", PropertyType.APARTMENT_BUILDING, "Số 89 Dịch Vọng, Cầu Giấy, Hà Nội", 18, 120, "500"}
        };
        List<Property> saved = new ArrayList<>();
        for (Object[] row : data) {
            Property p = new Property();
            p.setCode(codeGenerator.nextPropertyCode());
            p.setName((String) row[0]);
            p.setPropertyType((PropertyType) row[1]);
            p.setAddress((String) row[2]);
            p.setNumberOfFloors((Integer) row[3]);
            p.setNumberOfUnits((Integer) row[4]);
            p.setArea(new BigDecimal((String) row[5]));
            p.setOperationDate(LocalDate.now().minusYears(1 + saved.size()));
            p.setStatus(PropertyStatus.ACTIVE);
            p.setDescription("Bất động sản cho thuê tại " + row[0]);
            saved.add(propertyRepository.save(p));
        }
        return saved;
    }

    private List<Unit> seedUnits(List<Property> properties) {
        List<Unit> saved = new ArrayList<>();
        UnitType[] types = {UnitType.ROOM, UnitType.APARTMENT};
        for (Property property : properties) {
            int perProperty = Math.max(1, 25 / properties.size());
            if (saved.size() + perProperty > 25 && property != properties.get(properties.size() - 1)) {
                perProperty = Math.max(1, (25 - saved.size()) / (properties.size() - properties.indexOf(property)));
            }
            for (int i = 1; i <= perProperty && saved.size() < 25; i++) {
                int floor = 1 + (i % Math.max(1, property.getNumberOfFloors()));
                Unit u = new Unit();
                u.setProperty(property);
                u.setCode(String.format("P%02d%02d", floor, i));
                u.setName("Phòng " + floor + "0" + i);
                u.setFloorNumber(floor);
                u.setUnitType(types[i % types.length]);
                u.setArea(BigDecimal.valueOf(25 + random.nextInt(35)));
                u.setStatus(i % 4 == 0 ? UnitStatus.VACANT : UnitStatus.OCCUPIED);
                saved.add(unitRepository.save(u));
            }
        }
        // Dam bao du 25 unit bang cach them vao BDS lon nhat neu con thieu.
        Property largest = properties.get(properties.size() - 1);
        while (saved.size() < 25) {
            int i = saved.size() + 1;
            Unit u = new Unit();
            u.setProperty(largest);
            u.setCode("EXT" + i);
            u.setName("Phòng EXT" + i);
            u.setFloorNumber(1 + (i % Math.max(1, largest.getNumberOfFloors())));
            u.setUnitType(UnitType.ROOM);
            u.setArea(BigDecimal.valueOf(30));
            u.setStatus(UnitStatus.VACANT);
            saved.add(unitRepository.save(u));
        }
        return saved;
    }

    /**
     * Tao 35 yeu cau bao tri, 40 bao gia, 15 phieu cong viec, 10 nghiem thu, 30 chi phi,
     * lien ket theo dung workflow: NEW -> ... -> COMPLETED/CLOSED, tuan thu BR01-BR13.
     */
    private void seedWorkflow(List<Property> properties, List<Unit> units,
                              List<MaintenanceCategory> categories, List<Contractor> contractors) {
        String[][] issueTemplates = {
                {"Điện", "Mất điện toàn bộ khu vực", "Toàn bộ khu vực bị mất điện đột ngột, cần kiểm tra hệ thống dây điện"},
                {"Điện", "Ổ cắm điện bị cháy", "Ổ cắm điện tại phòng phát hiện có mùi khét, cần thay thế"},
                {"Điện", "Đèn hành lang không sáng", "Hệ thống đèn chiếu sáng hành lang không hoạt động"},
                {"Nước", "Rò rỉ nước tại nhà vệ sinh", "Phát hiện rò rỉ nước dưới sàn nhà vệ sinh"},
                {"Nước", "Vòi nước bị hỏng", "Vòi nước trong bếp bị chảy liên tục không tắt được"},
                {"Nước", "Bồn cầu bị tắc nghẽn", "Bồn cầu bị tắc, nước không thoát được"},
                {"Điều hòa", "Điều hòa phòng không làm lạnh", "Điều hòa hoạt động nhưng không làm lạnh được phòng"},
                {"Điều hòa", "Điều hòa bị chảy nước", "Điều hòa chảy nước xuống sàn khi vận hành"},
                {"Điều hòa", "Điều hòa có tiếng ồn lạ", "Điều hòa phát ra tiếng ồn bất thường khi hoạt động"},
                {"Thang máy", "Thang máy kêu to bất thường", "Thang máy phát ra tiếng động lớn khi di chuyển"},
                {"Thang máy", "Thang máy dừng đột ngột", "Thang máy dừng giữa chừng, cần kiểm tra hệ thống"},
                {"PCCC", "Bình chữa cháy hết hạn", "Bình chữa cháy đã hết hạn sử dụng, cần thay mới"},
                {"PCCC", "Chuông báo cháy kêu liên tục", "Hệ thống báo cháy kêu liên tục không rõ nguyên nhân"},
                {"Camera", "Camera an ninh mất tín hiệu", "Camera khu vực sảnh chính bị mất tín hiệu"},
                {"Camera", "Camera hành lang bị mờ", "Hình ảnh camera hành lang bị mờ, cần vệ sinh ống kính"},
                {"Nội thất", "Cửa gỗ bị mối mọt", "Cửa gỗ phòng bị mối mọt, cần xử lý"},
                {"Nội thất", "Bản lề cửa bị lỏng", "Bản lề cửa ra vào bị lỏng, đóng mở khó khăn"},
                {"Xây dựng", "Tường bị nứt", "Phát hiện vết nứt trên tường cần kiểm tra kết cấu"},
                {"Xây dựng", "Trần nhà bị thấm nước", "Trần nhà xuất hiện vết ố do thấm nước từ tầng trên"},
                {"Thiết bị", "Máy bơm nước bị hỏng", "Máy bơm nước sinh hoạt không hoạt động"},
                {"Thiết bị", "Quạt thông gió không hoạt động", "Quạt thông gió tại tầng hầm bị hỏng"},
                {"Vệ sinh", "Khu vực chung không được dọn dẹp", "Khu vực sảnh chưa được vệ sinh theo lịch"},
                {"Vệ sinh", "Thùng rác đầy tràn", "Thùng rác khu vực chung đầy, cần thu gom"},
                {"Khác", "Bảng chỉ dẫn bị hỏng", "Bảng chỉ dẫn tầng bị bong tróc, khó đọc"}
        };

        Priority[] priorities = Priority.values();
        List<MaintenanceRequest> requests = new ArrayList<>();

        for (int i = 0; i < 35; i++) {
            String[] template = issueTemplates[i % issueTemplates.length];
            Property property = properties.get(i % properties.size());
            List<Unit> propertyUnits = units.stream().filter(u -> u.getProperty().getId().equals(property.getId())).toList();
            MaintenanceCategory category = categories.stream()
                    .filter(c -> c.getName().equals(template[0]))
                    .findFirst().orElse(categories.get(i % categories.size()));

            MaintenanceRequest request = new MaintenanceRequest();
            request.setRequestCode(codeGenerator.nextMaintenanceRequestCode());
            request.setProperty(property);
            if (!propertyUnits.isEmpty() && i % 5 != 0) {
                request.setUnit(propertyUnits.get(i % propertyUnits.size()));
            }
            request.setCategory(category);
            request.setTitle(template[1] + " " + (request.getUnit() != null ? request.getUnit().getName() : property.getName()));
            request.setDescription(template[2]);
            request.setPriority(priorities[i % priorities.length]);
            request.setStatus(RequestStatus.NEW);
            request.setReportedDate(LocalDate.now().minusDays(60 - i));
            request.setExpectedCompletionDate(request.getReportedDate().plusDays(7));
            request.setEstimatedCost(BigDecimal.valueOf(500_000 + random.nextInt(2_000_000)));
            request.setReportedBy("Nhân viên trực tòa nhà");
            requests.add(requestRepository.save(request));
        }

        // ===== Nhom 1: 15 yeu cau "nang cao" - moi yeu cau 2 bao gia (1 duyet + 1 tu choi) =====
        List<WorkOrder> workOrders = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            MaintenanceRequest request = requests.get(i);
            Contractor contractorA = contractors.get(i % contractors.size());
            Contractor contractorB = contractors.get((i + 1) % contractors.size());

            Quotation qA = createQuotation(request, contractorA,
                    600_000 + random.nextInt(1_000_000), 400_000 + random.nextInt(600_000), 100_000,
                    3 + random.nextInt(4), QuotationStatus.APPROVED);
            Quotation qB = createQuotation(request, contractorB,
                    700_000 + random.nextInt(1_200_000), 450_000 + random.nextInt(700_000), 150_000,
                    3 + random.nextInt(5), QuotationStatus.REJECTED);

            request.setStatus(RequestStatus.QUOTATION_APPROVED);
            request.setEstimatedCost(qA.getTotalAmount());
            requestRepository.save(request);

            WorkOrder wo = new WorkOrder();
            wo.setWorkOrderCode(codeGenerator.nextWorkOrderCode());
            wo.setMaintenanceRequest(request);
            wo.setQuotation(qA);
            wo.setContractor(contractorA);
            wo.setStartDate(request.getReportedDate().plusDays(2));
            wo.setExpectedCompletionDate(wo.getStartDate().plusDays(qA.getEstimatedDays()));
            wo.setWorkDescription("Thực hiện xử lý: " + request.getTitle());
            wo.setStatus(WorkOrderStatus.NOT_STARTED);
            workOrders.add(workOrderRepository.save(wo));
        }

        // idx 0-7: hoan thanh + nghiem thu PASSED (idx 0-2 dong luon yeu cau)
        for (int i = 0; i <= 7; i++) {
            WorkOrder wo = advanceToCompleted(workOrders.get(i));
            Inspection inspection = createInspection(wo, InspectionResult.PASSED,
                    wo.getQuotation().getTotalAmount().subtract(BigDecimal.valueOf(30_000 * (i + 1))));
            MaintenanceRequest request = wo.getMaintenanceRequest();
            wo.setStatus(WorkOrderStatus.ACCEPTED);
            workOrderRepository.save(wo);
            request.setStatus(i <= 2 ? RequestStatus.CLOSED : RequestStatus.COMPLETED);
            request.setActualCompletionDate(inspection.getInspectionDate());
            requestRepository.save(request);
            recordExpenseForInspection(wo, inspection);
        }

        // idx 8-9: hoan thanh nhung nghiem thu FAILED -> lam lai
        for (int i = 8; i <= 9; i++) {
            WorkOrder wo = advanceToCompleted(workOrders.get(i));
            createInspection(wo, InspectionResult.FAILED, wo.getQuotation().getTotalAmount());
            wo.setStatus(WorkOrderStatus.IN_PROGRESS);
            wo.setActualCompletionDate(null);
            workOrderRepository.save(wo);
            MaintenanceRequest request = wo.getMaintenanceRequest();
            request.setStatus(RequestStatus.IN_PROGRESS);
            requestRepository.save(request);
        }

        // idx 10: hoan thanh, cho nghiem thu (chua nghiem thu)
        advanceToCompleted(workOrders.get(10));

        // idx 11 va 14: dang thuc hien
        for (int i : new int[]{11, 14}) {
            WorkOrder wo = workOrders.get(i);
            wo.setStatus(WorkOrderStatus.IN_PROGRESS);
            workOrderRepository.save(wo);
            MaintenanceRequest request = wo.getMaintenanceRequest();
            request.setStatus(RequestStatus.IN_PROGRESS);
            requestRepository.save(request);
        }
        // idx 12, 13: chua bat dau (giu nguyen NOT_STARTED, request QUOTATION_APPROVED)

        // ===== Nhom 2: 10 yeu cau dang cho bao gia (1 bao gia PENDING moi yeu cau) =====
        for (int i = 15; i <= 24; i++) {
            MaintenanceRequest request = requests.get(i);
            Contractor contractor = contractors.get(i % contractors.size());
            createQuotation(request, contractor,
                    500_000 + random.nextInt(900_000), 350_000 + random.nextInt(500_000), 80_000,
                    2 + random.nextInt(4), QuotationStatus.PENDING);
            request.setStatus(RequestStatus.WAITING_QUOTATION);
            requestRepository.save(request);
        }

        // ===== Nhom 3: 10 yeu cau moi phat sinh, chua co bao gia =====
        for (int i = 25; i <= 34; i++) {
            MaintenanceRequest request = requests.get(i);
            request.setStatus(i % 2 == 0 ? RequestStatus.NEW : RequestStatus.UNDER_REVIEW);
            requestRepository.save(request);
        }

        // ===== 22 chi phi van hanh doc lap (dien, nuoc, ve sinh, an ninh...) de du 30 chi phi =====
        ExpenseType[] operatingTypes = {ExpenseType.UTILITY, ExpenseType.CLEANING, ExpenseType.SECURITY, ExpenseType.OTHER};
        String[] operatingDescriptions = {
                "Tiền điện khu vực chung tháng", "Tiền nước sinh hoạt tháng", "Chi phí vệ sinh định kỳ",
                "Chi phí bảo vệ an ninh tháng", "Chi phí thu gom rác thải", "Chi phí bảo trì thang máy định kỳ"
        };
        for (int i = 0; i < 22; i++) {
            Property property = properties.get(i % properties.size());
            Expense expense = new Expense();
            expense.setExpenseCode(codeGenerator.nextExpenseCode());
            expense.setProperty(property);
            expense.setExpenseType(operatingTypes[i % operatingTypes.length]);
            expense.setAmount(BigDecimal.valueOf(300_000 + random.nextInt(3_000_000)));
            expense.setExpenseDate(LocalDate.now().minusDays(random.nextInt(150)));
            expense.setDescription(operatingDescriptions[i % operatingDescriptions.length] + " - " + property.getName());
            expense.setReferenceNumber("HD" + (2000 + i));
            expenseRepository.save(expense);
        }
    }

    private Quotation createQuotation(MaintenanceRequest request, Contractor contractor,
                                      long materialCost, long laborCost, long otherCost,
                                      int estimatedDays, QuotationStatus status) {
        Quotation q = new Quotation();
        q.setQuotationCode(codeGenerator.nextQuotationCode());
        q.setMaintenanceRequest(request);
        q.setContractor(contractor);
        q.setQuotationDate(request.getReportedDate().plusDays(1));
        q.setMaterialCost(BigDecimal.valueOf(materialCost));
        q.setLaborCost(BigDecimal.valueOf(laborCost));
        q.setOtherCost(BigDecimal.valueOf(otherCost));
        q.recalculateTotal();
        q.setEstimatedDays(estimatedDays);
        q.setDescription("Báo giá thực hiện: " + request.getTitle());
        q.setStatus(status);
        if (status == QuotationStatus.APPROVED) {
            q.setApprovedAt(LocalDateTime.now());
        }
        return quotationRepository.save(q);
    }

    /** Dua WorkOrder qua NOT_STARTED -> IN_PROGRESS -> COMPLETED. */
    private WorkOrder advanceToCompleted(WorkOrder wo) {
        wo.setStatus(WorkOrderStatus.COMPLETED);
        wo.setActualCompletionDate(wo.getStartDate().plusDays(Math.max(1, wo.getQuotation().getEstimatedDays())));
        wo.setResult("Đã hoàn thành xử lý theo yêu cầu.");
        return workOrderRepository.save(wo);
    }

    private Inspection createInspection(WorkOrder wo, InspectionResult result, BigDecimal actualCost) {
        Inspection inspection = new Inspection();
        inspection.setWorkOrder(wo);
        inspection.setInspectionDate(wo.getActualCompletionDate() != null
                ? wo.getActualCompletionDate().plusDays(1) : LocalDate.now());
        inspection.setInspectorName("Trịnh Hoàng Thành");
        inspection.setResult(result);
        inspection.setActualCost(actualCost);
        inspection.setNotes(result == InspectionResult.PASSED ? "Công việc đạt yêu cầu." : "Chưa đạt, yêu cầu làm lại.");
        return inspectionRepository.save(inspection);
    }

    /** Mo phong lai hanh vi tu dong ghi chi phi khi nghiem thu PASSED (giong InspectionServiceImpl). */
    private void recordExpenseForInspection(WorkOrder wo, Inspection inspection) {
        Expense expense = new Expense();
        expense.setExpenseCode(codeGenerator.nextExpenseCode());
        expense.setProperty(wo.getMaintenanceRequest().getProperty());
        expense.setWorkOrder(wo);
        expense.setContractor(wo.getContractor());
        expense.setExpenseType(ExpenseType.MAINTENANCE);
        expense.setAmount(inspection.getActualCost());
        expense.setExpenseDate(inspection.getInspectionDate());
        expense.setDescription("Chi phí bảo trì theo phiếu " + wo.getWorkOrderCode()
                + " - " + wo.getMaintenanceRequest().getTitle());
        expense.setReferenceNumber(wo.getWorkOrderCode());
        expenseRepository.save(expense);
    }
}
