# Nội dung báo cáo BTL – CNJ49 Property Maintenance Management

> Tài liệu này soạn theo đúng cấu trúc mục lục của `File_template_BTL_2026.docx`.
> Copy từng phần vào đúng heading tương ứng trong file Word (giữ style Heading có sẵn
> của template để Mục lục tự động cập nhật đúng số trang).

---

## TRANG BÌA (điền vào các chỗ …)

- Chủ đề 1: **CNJ49**
- Đề tài 2: **Xây dựng ứng dụng web quản lý bảo trì, nhà thầu và chi phí vận hành bất động sản cho thuê**
- LỚP TÍN CHỈ: Công nghệ Java-1-1-26(N06)
- Giảng viên hướng dẫn: ThS. Trần Nguyên Hoàng
- Danh sách sinh viên thực hiện – Nhóm …:

| TT | Mã sinh viên | Sinh viên thực hiện | Lớp hành chính |
|---|---|---|---|
| 1 | 20231337 | Trịnh Hoàng Thành (Trưởng nhóm) | *(điền lớp hành chính)* |
| 2 | 20231645 | Bùi Huy Hùng | *(điền lớp hành chính)* |
| 3 | 20231621 | Trần Quốc Đạt | *(điền lớp hành chính)* |
| 4 | 20231904 | Lê Văn Duy | *(điền lớp hành chính)* |

Số điện thoại (dùng cho form báo cáo tiến độ):

| Thành viên | Số điện thoại |
|---|---|
| Trịnh Hoàng Thành | 0355636882 |
| Bùi Huy Hùng | *(điền SĐT)* |
| Trần Quốc Đạt | *(điền SĐT)* |
| Lê Văn Duy | *(điền SĐT)* |

- Bắc Ninh – 2026

---

## PHÁT BIỂU BÀI TOÁN (dùng cho ô "Phát biểu bài toán" của form báo cáo tiến độ)

Các bất động sản cho thuê (chung cư mini, nhà trọ, căn hộ dịch vụ) hiện đang được quản lý bảo trì
thủ công qua sổ sách hoặc Excel, dẫn tới nhiều bất cập: sự cố báo trễ, không theo dõi được tiến độ
xử lý, khó so sánh và kiểm soát báo giá giữa nhiều nhà thầu, dễ phát sinh thanh toán trùng hoặc sai
lệch chi phí, và không có số liệu tổng hợp để chủ đầu tư ra quyết định. Đề tài xây dựng một ứng
dụng web quản lý xuyên suốt vòng đời một sự cố bảo trì: từ khi phát hiện sự cố → tạo yêu cầu bảo
trì → mời và so sánh báo giá của nhà thầu → phê duyệt báo giá → lập phiếu công việc → nhà thầu
thi công → nghiệm thu (đạt/không đạt) → tự động ghi nhận chi phí thực tế → đóng yêu cầu, đồng
thời cung cấp dashboard thống kê và các báo cáo có bộ lọc để hỗ trợ ra quyết định. Hệ thống áp
dụng phân quyền theo 3 vai trò (ADMIN/MANAGER/STAFF) và chuẩn hoá 15 quy tắc nghiệp vụ
(BR01–BR15) nhằm đảm bảo tính nhất quán trạng thái dữ liệu trong suốt quy trình.

---

## MỤC LỤC / DANH MỤC TỪ VIẾT TẮT / DANH MỤC BẢNG BIỂU

Giữ nguyên các trường TOC tự động của template (F9 để cập nhật số trang sau khi dán nội dung).

Danh mục từ viết tắt gợi ý:

| STT | Chữ viết tắt | Giải thích |
|---|---|---|
| 1 | BTL | Bài tập lớn |
| 2 | BR | Business Rule (Quy tắc nghiệp vụ) |
| 3 | CRUD | Create – Read – Update – Delete |
| 4 | JPA | Java Persistence API |
| 5 | MVC | Model – View – Controller |
| 6 | ERD | Entity Relationship Diagram |

---

## Lời mở đầu

Trình bày ngắn gọn: lý do chọn đề tài (thực trạng quản lý bảo trì bất động sản cho thuê thủ công,
thiếu công cụ hỗ trợ), mục tiêu môn học Công nghệ Java (vận dụng Spring Boot, Spring Data JPA,
Thymeleaf, Spring Security, MySQL vào một bài toán quản lý thực tế), và bố cục báo cáo gồm 3
chương theo đúng mục lục.

---

## Chương 1. Cơ sở lý thuyết

### 1.1. Giới thiệu về đề tài

- **Giới thiệu**: nhắc lại phát biểu bài toán ở trên (BĐS cho thuê, quy trình bảo trì – nhà thầu –
  chi phí vận hành, workflow NEW → … → CLOSED).
- **Kế hoạch làm đề tài**: chia theo tuần/sprint, ví dụ: Tuần 1 phân tích yêu cầu + thiết kế CSDL;
  Tuần 2-3 xây dựng entity/repository/service theo business rule; Tuần 4 xây controller + giao diện
  Thymeleaf; Tuần 5 viết unit test, hoàn thiện dashboard/báo cáo; Tuần 6 kiểm thử tổng thể, viết
  báo cáo. *(nhóm điều chỉnh lại theo tiến độ thực tế)*.
- **Thành viên nhóm** (Nhóm …, GVHD ThS. Trần Nguyên Hoàng):
  - Trịnh Hoàng Thành – 20231337 – Trưởng nhóm
  - Bùi Huy Hùng – 20231645 – Thành viên
  - Trần Quốc Đạt – 20231621 – Thành viên
  - Lê Văn Duy – 20231904 – Thành viên
- **Phân công công việc**: *(nhóm tự điền theo thực tế phân công của từng người — mục CV
  thành viên trong form báo cáo cần khớp với phần này, ví dụ: Thành – kiến trúc hệ thống, module
  MaintenanceRequest/WorkOrder, security; Hùng – module Property/Unit/Contractor; Đạt – module
  Quotation/Inspection/Expense; Duy – Dashboard, Report, giao diện Thymeleaf, viết test)*.

### 1.2. Giải thuật, công cụ

- Thuật toán sinh mã nghiệp vụ tự động dạng `PREFIX-xxxx` (PROP-0001, MR-2026-0001, QT-2026-0001,
  WO-2026-0001, EXP-2026-0001, CTR-0001) dựa trên `MAX(sequence) + 1` theo từng loại thực thể
  (`CodeGenerator`).
- Thuật toán đồng bộ trạng thái hữu hạn (finite-state) giữa 3 thực thể liên quan
  `MaintenanceRequest` ↔ `Quotation` ↔ `WorkOrder` ↔ `Inspection`, đảm bảo bởi 15 quy tắc nghiệp
  vụ BR01–BR15 cài đặt tại tầng Service.
- Công cụ: IntelliJ IDEA / VS Code, Maven 3.9, MySQL Workbench, Postman (test API/luồng nghiệp
  vụ), Git/GitHub (quản lý mã nguồn), draw.io/Figma (vẽ ERD, wireframe).

### 1.3. Các công nghệ sử dụng

| Thành phần | Công nghệ |
|---|---|
| Ngôn ngữ | Java 21 |
| Backend framework | Spring Boot 3.3.5, Spring MVC |
| Truy xuất dữ liệu | Spring Data JPA / Hibernate 6 |
| Kiểm tra dữ liệu | Bean Validation (Jakarta Validation) |
| Bảo mật | Spring Security (form login, mã hoá mật khẩu BCrypt) |
| View layer | Thymeleaf + thymeleaf-extras-springsecurity6 |
| Giao diện | Bootstrap 5, Bootstrap Icons, Chart.js (biểu đồ dashboard) |
| Cơ sở dữ liệu | MySQL 8.x |
| Build | Maven |
| Kiểm thử | JUnit 5, Spring Boot Test, H2 in-memory (test độc lập với MySQL) |

### 1.4. Kết chương

Tóm tắt: chương 1 đã trình bày bối cảnh bài toán, kế hoạch triển khai và nền tảng công nghệ được
lựa chọn; chương 2 sẽ đi sâu vào phân tích yêu cầu, thiết kế kiến trúc và cơ sở dữ liệu.

---

## Chương 2. Thiết kế và xây dựng chương trình

### 2.1. Phân tích yêu cầu bài toán

Tác nhân (actor) và chức năng chính (chi tiết đầy đủ tại `docs/UseCase.md`):

- **ADMIN**: toàn quyền hệ thống.
- **MANAGER**: quản lý BĐS, bảo trì, nhà thầu, báo giá, nghiệm thu, chi phí, báo cáo.
- **STAFF**: ghi nhận sự cố, tạo yêu cầu bảo trì, cập nhật thông tin, theo dõi tiến độ.

15 nhóm chức năng chính: đăng nhập/đăng xuất; CRUD bất động sản (tìm kiếm/lọc/phân trang); CRUD
căn/phòng; CRUD hạng mục bảo trì; tạo/cập nhật/huỷ/đóng yêu cầu bảo trì; thêm và so sánh báo giá
nhà thầu; duyệt/từ chối báo giá; tạo phiếu công việc từ báo giá đã duyệt; bắt đầu/tạm dừng/tiếp
tục/hoàn thành/huỷ phiếu công việc; nghiệm thu PASSED/FAILED; ghi nhận chi phí vận hành (thủ công
hoặc tự động khi nghiệm thu đạt); dashboard thống kê; 7 báo cáo có bộ lọc.

**Kịch bản chính (use case trung tâm) – Xử lý yêu cầu bảo trì từ đầu đến cuối:**

1. STAFF phát hiện sự cố, tạo Yêu cầu bảo trì (NEW).
2. MANAGER xem xét, chuyển UNDER_REVIEW → WAITING_QUOTATION.
3. Nhiều nhà thầu gửi báo giá (Quotation PENDING).
4. MANAGER so sánh, duyệt một báo giá (APPROVED); các báo giá còn lại tự động REJECTED (BR03);
   yêu cầu chuyển QUOTATION_APPROVED (BR02).
5. Hệ thống tạo Phiếu công việc từ báo giá đã duyệt, nhà thầu lấy đúng từ báo giá (BR09).
6. Nhà thầu thực hiện: WorkOrder NOT_STARTED → IN_PROGRESS → COMPLETED; yêu cầu đồng bộ sang
   WAITING_INSPECTION (BR10, BR11).
7. Nghiệm thu: PASSED → WorkOrder ACCEPTED, yêu cầu COMPLETED, chi phí thực tế tự động ghi nhận
   (BR12); FAILED → WorkOrder và yêu cầu quay lại IN_PROGRESS để làm lại (BR13).
8. Sau COMPLETED, MANAGER đóng yêu cầu (CLOSED) (BR05).
9. Chi phí và báo cáo cập nhật theo thời gian thực trên Dashboard và trang Báo cáo.

### 2.2. Kiến trúc hệ thống

Kiến trúc phân lớp (layered architecture) chuẩn Spring MVC:

```
Controller (Spring MVC)  →  Service (business rules BR01-BR15)  →  Repository (Spring Data JPA)  →  MySQL
        ↓                                                                    ↑
   Thymeleaf View  ←───────────────── DTO / Form ──────────────────── Entity (JPA)
```

- **entity/**: 11 JPA entity (Property, Unit, MaintenanceCategory, MaintenanceRequest, Contractor,
  Quotation, WorkOrder, Inspection, Expense, User, AuditLog).
- **repository/**: interface Spring Data JPA kế thừa `JpaRepository`, bổ sung các query tìm
  kiếm/lọc/phân trang (`@Query` JPQL) cho từng module.
- **service / service.impl/**: toàn bộ 15 business rule (BR01–BR15) được cài đặt ở đây, tách biệt
  khỏi Controller để dễ kiểm thử độc lập (unit test không cần khởi động web layer).
- **controller/**: 11 Spring MVC controller, chuẩn hoá theo REST-ish path (`/properties`,
  `/maintenance/{id}/quotations`, `/workorders`, …), dùng `RedirectAttributes` cho flash message.
  Ví dụ 2 lớp kế thừa/mở rộng rõ nét trong hệ thống: `BusinessException`/`ResourceNotFoundException`
  kế thừa `RuntimeException` và được `GlobalExceptionHandler` (`@ControllerAdvice`) xử lý tập
  trung; các Service impl đều implement interface Service tương ứng (đa hình khi cần thay đổi cài
  đặt).
- **dto/**: Form object cho input (có Bean Validation) và DTO hiển thị (ChartSeries, DashboardStats,
  ExpenseSummary, ContractorStatistics…) tách khỏi entity để tránh lộ cấu trúc DB ra view.
- **config/**: `SecurityConfig` (xác thực/phân quyền), `WebMvcConfig`, `DataSeeder` (seed dữ liệu
  demo idempotent), `CustomUserDetailsService`.

*(Hình 2.1 – Mô hình lớp của hệ thống: vẽ sơ đồ lớp rút gọn Controller → Service → Repository →
Entity cho 1-2 module tiêu biểu, ví dụ MaintenanceRequest và WorkOrder, bằng draw.io/Figma rồi
chèn ảnh vào đây.)*

### 2.3. Thiết kế giao diện

33 giao diện Thymeleaf, tổ chức theo module trong `src/main/resources/templates/`: auth, dashboard,
properties, units, categories, contractors, maintenance, quotations, workorders, inspections,
expenses, reports, errors, và các fragment dùng chung (`fragments/sidebar.html`,
`fragments/topbar.html`, `fragments/pagination.html`, `fragments/badges.html`).

*(Vẽ wireframe bằng Figma/draw.io cho tối thiểu 5 màn hình tiêu biểu rồi chèn ảnh, ví dụ: Dashboard,
Danh sách yêu cầu bảo trì, Chi tiết yêu cầu bảo trì (kèm timeline + báo giá), Form tạo phiếu công
việc, Trang báo cáo.)*

Bảng 2.1 – Mô tả form "Yêu cầu bảo trì" (ví dụ, nhóm bổ sung các form còn lại theo mẫu này):

| STT | Tên đối tượng | Kiểu | Ý nghĩa | Ghi chú |
|---|---|---|---|---|
| 1 | frmMaintenanceList | Trang danh sách | Hiển thị danh sách yêu cầu bảo trì, tìm kiếm/lọc/phân trang | maintenance/list.html |
| 2 | frmMaintenanceForm | Form | Tạo/sửa yêu cầu bảo trì | maintenance/form.html |
| 3 | frmMaintenanceDetail | Trang chi tiết | Xem chi tiết + timeline xử lý + danh sách báo giá | maintenance/detail.html |
| 4 | btnChangeStatus | Button | Chuyển trạng thái yêu cầu | POST /maintenance/{id}/status |

### 2.3. Thiết kế cơ sở dữ liệu

Sơ đồ liên kết thực thể (chi tiết tại `docs/ERD.md`):

```
PROPERTY (1) ----< (N) UNIT
PROPERTY (1) ----< (N) MAINTENANCE_REQUEST
PROPERTY (1) ----< (N) EXPENSE
MAINTENANCE_CATEGORY (1) ----< (N) MAINTENANCE_REQUEST
UNIT (1) ----< (N) MAINTENANCE_REQUEST   [tuỳ chọn]
MAINTENANCE_REQUEST (1) ----< (N) QUOTATION >---- (N) CONTRACTOR
MAINTENANCE_REQUEST (1) ----< (N) WORK_ORDER >---- (N) CONTRACTOR
QUOTATION (1) ---- (1) WORK_ORDER
WORK_ORDER (1) ----< (N) INSPECTION
WORK_ORDER (1) ----< (N) EXPENSE
CONTRACTOR (1) ----< (N) EXPENSE   [tuỳ chọn]
```

*(Hình 2.2 – Mô hình liên kết thực thể: chèn ảnh ERD, có thể vẽ lại từ sơ đồ trên bằng draw.io.
Hình 2.3 – Mô hình vật lý CSDL: chèn ảnh sơ đồ các bảng + khoá chính/khoá ngoại, có thể export từ
MySQL Workbench sau khi import file `database/cnj49_property_maintenance.sql`.)*

11 bảng chính: `properties`, `units`, `maintenance_categories`, `maintenance_requests`,
`contractors`, `quotations`, `work_orders`, `inspections`, `expenses`, `users`, `audit_logs` – mô
tả chi tiết từng cột tại `docs/Database.md`. File CSDL đầy đủ (schema + dữ liệu mẫu, cú pháp MySQL
chuẩn) nằm tại `database/cnj49_property_maintenance.sql`, xuất trực tiếp từ ứng dụng thật (không
viết tay) để đảm bảo khớp 100% với entity JPA.

Ghi chú thiết kế quan trọng: không dùng `CascadeType.ALL` tuỳ tiện — xoá Property/Contractor bị
chặn ở tầng Service (BR14/BR15) nếu còn dữ liệu liên quan, tránh lỗi khoá ngoại và xoá nhầm dữ
liệu lịch sử; trạng thái `WorkOrder`/`MaintenanceRequest` được đồng bộ từ Service chứ không dựa
vào trigger database, giúp logic dễ kiểm thử và dễ giải thích khi bảo vệ đồ án.

### 2.4. Tổ chức dự án, mô tả file mã nguồn

```
src/main/java/com/cnj49/propertymaintenance/
  config/       - Cấu hình Security, MVC, Data Seeder
  controller/   - 11 Spring MVC Controller
  dto/          - Form object và DTO hiển thị
  entity/       - 11 JPA Entity
  enums/        - 12 enum trạng thái/loại nghiệp vụ
  exception/    - Exception tuỳ chỉnh + Global Exception Handler
  repository/   - 11 Spring Data JPA Repository
  service/      - Service interface
  service/impl/ - Service implementation (business rule BR01-BR15)
  util/         - CodeGenerator (sinh mã tự động)

src/main/resources/
  templates/    - 33 Thymeleaf template theo module
  static/       - CSS, JS
  application.properties

src/test/java/  - 5 test class (13 test case) cho các nghiệp vụ chính, dùng H2 in-memory
database/       - File SQL export (schema + seed data), sẵn sàng import vào MySQL
docs/           - Database.md, ERD.md, UseCase.md, TestCases.md
```

Repository GitHub: `https://github.com/20231337-glitch/cnj49-property-maintenance`

### 2.5. Kết chương

Chương 2 đã trình bày phân tích yêu cầu, kiến trúc phân lớp, thiết kế giao diện và cơ sở dữ liệu
của hệ thống. Chương 3 trình bày quá trình phát triển từng tầng, kết quả demo và kế hoạch kiểm
thử.

---

## Chương 3. Phát triển hệ thống và kiểm thử

### 3.1. Tầng giao diện (Presentation layer)

Các Form/trang Thymeleaf và quan hệ giữa chúng: trang danh sách (list) → trang chi tiết (detail)
→ form tạo/sửa (form); ví dụ luồng `maintenance/list.html` → `maintenance/detail.html` (xem timeline,
thêm báo giá, đổi trạng thái) → `quotations/form.html` (thêm báo giá) → quay lại `detail.html`.
Các fragment dùng chung (`sidebar`, `topbar`, `pagination`) được `th:replace`/`th:insert` vào mọi
trang để đảm bảo giao diện nhất quán và phân quyền hiển thị menu theo vai trò đăng nhập
(`sec:authorize` của thymeleaf-extras-springsecurity6).

### 3.2. Tầng nghiệp vụ (Service layer)

Các class xử lý nghiệp vụ chính, thống kê, báo cáo: `MaintenanceRequestServiceImpl`,
`QuotationServiceImpl`, `WorkOrderServiceImpl`, `InspectionServiceImpl`, `ExpenseServiceImpl`,
`DashboardServiceImpl` (6 thẻ số liệu + 4 biểu đồ), `ReportServiceImpl` (7 báo cáo có bộ lọc theo
ngày/BĐS/nhà thầu), `AuditLogServiceImpl` (ghi nhật ký thao tác cho timeline).

**Code đặc trưng** – ví dụ business rule BR02/BR03 (chỉ 1 báo giá được duyệt, các báo giá còn lại
tự động từ chối), trích từ `QuotationServiceImpl.approve()`:

```java
if (quotationRepository.existsByMaintenanceRequestIdAndStatus(
        request.getId(), QuotationStatus.APPROVED)) {
    throw new BusinessException(
        "Yêu cầu này đã có một báo giá được duyệt. Không thể duyệt thêm báo giá thứ hai (BR02).");
}
quotation.setStatus(QuotationStatus.APPROVED);
// BR03: các báo giá PENDING còn lại của cùng yêu cầu tự động chuyển REJECTED
```

### 3.3. Tầng dữ liệu (Persistence layer)

Các lớp thực thể (entity) ánh xạ 11 bảng CSDL qua JPA/Hibernate (`@Entity`, `@ManyToOne`,
`@OneToMany`, `@Enumerated`, Bean Validation `@NotNull`/`@DecimalMin`…), thao tác qua Spring Data
JPA repository. Việc sinh mã nghiệp vụ (`CodeGenerator`) và seed dữ liệu demo (`DataSeeder`, chạy
một lần khi CSDL rỗng) cũng thuộc tầng này.

### 3.4/3.5. Kết quả đạt được

Demo từng chức năng (chèn ảnh chụp màn hình thực tế sau khi chạy `mvn spring-boot:run`, đăng nhập
bằng 1 trong 3 tài khoản demo admin/manager/staff):

1. Đăng nhập, phân quyền hiển thị menu theo vai trò.
2. CRUD Bất động sản/Căn phòng/Hạng mục bảo trì với tìm kiếm, lọc, phân trang.
3. Tạo yêu cầu bảo trì, theo dõi timeline xử lý.
4. Thêm nhiều báo giá, so sánh, duyệt một báo giá.
5. Tạo phiếu công việc, cập nhật tiến độ thi công.
6. Nghiệm thu PASSED/FAILED, tự động ghi nhận chi phí khi đạt.
7. Dashboard 6 thẻ số liệu + 4 biểu đồ Chart.js (dữ liệu thật từ DB).
8. 7 báo cáo có bộ lọc theo ngày/bất động sản/nhà thầu.

### 3.5. Kiểm thử (đổi tiêu đề mục để không trùng số 3.5 với mục trên)

Kịch bản test tự động hoá bằng JUnit 5 + H2 in-memory (độc lập với MySQL), chạy `mvn clean test`:
5 lớp test, 13 test case, **13/13 PASS**. Chi tiết từng test case tại `docs/TestCases.md`, gồm:
TC01 tạo bất động sản; TC02/TC02b tạo yêu cầu bảo trì + validate (BR06/BR07); TC03 thêm nhiều báo
giá; TC04 duyệt báo giá + chống duyệt trùng (BR02/BR03); TC05/TC05b tạo WorkOrder từ báo giá đã
duyệt + chặn khi chưa duyệt (BR01/BR09); TC06 hoàn thành WorkOrder (BR11); TC07 nghiệm thu PASSED
(tự động ghi chi phí).

### 3.6. Kết chương

Chương 3 đã trình bày việc triển khai từng tầng của hệ thống, kết quả demo các chức năng chính và
kết quả kiểm thử tự động — toàn bộ 13 test case đều đạt, xác nhận các quy tắc nghiệp vụ cốt lõi
(BR01–BR13) hoạt động đúng như thiết kế.

---

## Kết luận

### Kết quả thu được

- Xây dựng hoàn chỉnh ứng dụng web quản lý bảo trì – nhà thầu – chi phí vận hành BĐS cho thuê theo
  đúng 15 chức năng và 15 business rule đã đề ra, workflow đầy đủ từ phát hiện sự cố đến đóng yêu
  cầu và ghi nhận chi phí.
- 11 entity, 11 repository, 12 service + 12 service impl, 11 controller, 33 giao diện Thymeleaf.
- 13/13 unit/integration test PASS (JUnit 5 + H2), đảm bảo các business rule cốt lõi hoạt động
  đúng.
- Dashboard thống kê thời gian thực + 7 báo cáo có bộ lọc phục vụ ra quyết định quản lý.
- Đăng nhập, seed dữ liệu demo tự động, mã nguồn quản lý bằng Git/GitHub.

### Hạn chế và hướng phát triển của đề tài

Hạn chế hiện tại (ghi nhận qua tự rà soát code trước khi bảo vệ):

- Phân quyền mới dừng ở mức xác thực đăng nhập, chưa chặn theo vai trò (ADMIN/MANAGER/STAFF) ở
  từng endpoint nhạy cảm (duyệt báo giá, xoá dữ liệu) — hướng phát triển: bổ sung `@PreAuthorize`
  theo vai trò cho từng thao tác.
- Một số luồng chuyển trạng thái đặc biệt (huỷ phiếu công việc, đổi trạng thái thủ công) còn thiếu
  kiểm tra chặt chẽ theo đúng state machine của quy trình — hướng phát triển: chuẩn hoá bảng
  chuyển trạng thái hợp lệ (transition table) cho `RequestStatus`/`WorkOrderStatus`/`QuotationStatus`.
- Một số trang danh sách chưa tối ưu truy vấn (N+1 query) khi dữ liệu lớn — hướng phát triển: bổ
  sung `JOIN FETCH`/`@EntityGraph` cho các quan hệ LAZY thường xuyên hiển thị.
- Sinh mã nghiệp vụ hiện dựa trên `MAX + 1`, có thể trùng khi có nhiều người dùng thao tác đồng
  thời — hướng phát triển: dùng DB sequence hoặc cơ chế khoá phù hợp.
- Chưa có API/ứng dụng di động cho nhà thầu tự cập nhật tiến độ, chưa có thông báo (email/SMS) khi
  đổi trạng thái yêu cầu — hướng phát triển mở rộng trong tương lai.

---

## Danh mục sách tham khảo

1. Spring Boot Reference Documentation – https://docs.spring.io/spring-boot/ [truy cập tháng
   09/2026].
2. Spring Data JPA Reference Documentation – https://docs.spring.io/spring-data/jpa/reference/.
3. Thymeleaf Documentation – https://www.thymeleaf.org/documentation.html.
4. Spring Security Reference – https://docs.spring.io/spring-security/reference/.
5. *(bổ sung giáo trình/tài liệu học phần Công nghệ Java do giảng viên cung cấp)*.
