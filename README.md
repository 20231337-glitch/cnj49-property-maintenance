# CNJ49 Property Maintenance Management

## Thành viên

| Thành viên | MSSV | Vai trò |
|---|---:|---|
| Trịnh Hoàng Thành | 20231337 | Trưởng nhóm |
| Bùi Huy Hùng | 20231645 | Thành viên |
| Trần Quốc Đạt | 20231621 | Thành viên |
| Lê Văn Duy | 20231904 | Thành viên |

## Mô tả đề tài

**CNJ49** – Xây dựng ứng dụng web quản lý bảo trì, nhà thầu và chi phí vận hành bất động sản cho thuê.

Hệ thống quản lý xuyên suốt quy trình: Bất động sản → khu vực/căn/phòng → phát sinh sự cố → yêu cầu bảo trì →
nhà thầu → báo giá → phê duyệt → phiếu công việc → sửa chữa → nghiệm thu → ghi nhận chi phí → báo cáo.

## Công nghệ

- Java 21, Spring Boot 3, Spring MVC, Spring Data JPA / Hibernate, Bean Validation, Spring Security
- Thymeleaf, Bootstrap 5, Bootstrap Icons, Chart.js
- MySQL 8.x, Maven

## Chức năng

1. Quản lý bất động sản (CRUD, tìm kiếm, lọc, phân trang)
2. Quản lý căn/phòng/khu vực
3. Quản lý hạng mục bảo trì
4. Quản lý yêu cầu bảo trì (workflow đầy đủ NEW → ... → CLOSED)
5. Quản lý nhà thầu
6. Quản lý báo giá + so sánh + phê duyệt (BR02, BR03)
7. Quản lý phiếu công việc (BR01, BR06, BR09, BR10, BR11)
8. Nghiệm thu PASSED/FAILED (BR04, BR12, BR13)
9. Quản lý chi phí vận hành
10. Dashboard thống kê với 6 thẻ số liệu + 4 biểu đồ (dữ liệu thật từ DB)
11. 7 báo cáo có bộ lọc theo ngày/bất động sản/nhà thầu
12. Lịch sử hoạt động (audit log / timeline)
13. Đăng nhập, phân quyền 3 vai trò: ADMIN, MANAGER, STAFF

## Database

Tên database: `cnj49_property_maintenance` (MySQL 8.x, tự tạo khi khởi động nếu chưa có).

Các bảng chính: `users`, `properties`, `units`, `maintenance_categories`, `maintenance_requests`,
`contractors`, `quotations`, `work_orders`, `inspections`, `expenses`, `audit_logs`.

Quan hệ chính:
```
Property 1--N Unit
Property 1--N MaintenanceRequest
MaintenanceRequest 1--N Quotation --N--1 Contractor
MaintenanceRequest 1--N WorkOrder --N--1 Contractor
WorkOrder 1--N Inspection
WorkOrder 1--N Expense
Property 1--N Expense
```

## Yêu cầu môi trường

- JDK 21+
- Maven 3.9+ (hoặc dùng `mvnw` nếu có)
- MySQL Server 8.x đang chạy tại `localhost:3306`

## Cách chạy

1. Tạo biến môi trường mật khẩu MySQL (nếu có đặt password cho user root):
   ```
   set DB_PASSWORD=your_password      (Windows cmd)
   $env:DB_PASSWORD="your_password"   (PowerShell)
   ```
   Nếu MySQL root không có mật khẩu, có thể bỏ qua bước này.

2. Build và chạy:
   ```
   mvn clean package
   mvn spring-boot:run
   ```
   hoặc chạy trực tiếp jar:
   ```
   java -jar target/cnj49-property-maintenance.jar
   ```

3. Mở trình duyệt tại: http://localhost:8080

Database và bảng sẽ được tự động tạo (`spring.jpa.hibernate.ddl-auto=update`), dữ liệu demo sẽ được
seed tự động ở lần chạy đầu tiên (idempotent - không seed lại nếu đã có dữ liệu).

## Tài khoản demo

| Tài khoản | Mật khẩu | Vai trò |
|---|---|---|
| admin | admin123 | ADMIN |
| manager | manager123 | MANAGER |
| staff | staff123 | STAFF |

## Cấu trúc project

```
src/main/java/com/cnj49/propertymaintenance/
  config/       - Cấu hình Security, MVC, Data Seeder
  controller/   - Spring MVC Controllers
  dto/          - Form objects và DTO cho hiển thị
  entity/       - JPA Entities
  enums/        - Enum trạng thái nghiệp vụ
  exception/    - Exception tùy chỉnh + Global Exception Handler
  repository/   - Spring Data JPA Repositories
  service/      - Service interfaces
  service/impl/ - Service implementations (business rules BR01-BR15)
  util/         - Sinh mã tự động (CodeGenerator)

src/main/resources/
  templates/    - Thymeleaf templates theo module
  static/       - CSS, JS
  application.properties

src/test/java/  - Unit/integration test cho các nghiệp vụ chính (H2 in-memory)
```

## Workflow chính

```
Phát hiện sự cố → Tạo yêu cầu bảo trì (NEW)
→ Gửi yêu cầu báo giá (WAITING_QUOTATION)
→ Nhà thầu gửi báo giá (Quotation PENDING)
→ So sánh & Phê duyệt một báo giá (QUOTATION_APPROVED, các báo giá khác tự động REJECTED)
→ Tạo phiếu công việc (WorkOrder NOT_STARTED)
→ Bắt đầu (IN_PROGRESS) → Hoàn thành (COMPLETED, request WAITING_INSPECTION)
→ Nghiệm thu:
    PASSED → WorkOrder ACCEPTED, Request COMPLETED, tự động ghi nhận chi phí thực tế
    FAILED → WorkOrder & Request quay lại IN_PROGRESS (làm lại)
→ Đóng yêu cầu (CLOSED)
```

## Business Rules (BR01-BR15)

Toàn bộ được triển khai trong lớp Service (không đặt trong Controller), bao gồm:
- BR01: Không tạo WorkOrder khi chưa có Quotation APPROVED
- BR02/BR03: Chỉ một Quotation APPROVED tại một thời điểm, các báo giá còn lại tự động REJECTED
- BR04: Không nghiệm thu WorkOrder chưa COMPLETED
- BR05: Không đóng MaintenanceRequest nếu chưa nghiệm thu đạt
- BR06: Ngày hoàn thành không được trước ngày bắt đầu
- BR07: Số tiền không được âm
- BR08: Unit bắt buộc thuộc một Property tồn tại
- BR09: WorkOrder dùng đúng Contractor của Quotation đã duyệt
- BR10-BR13: Đồng bộ trạng thái WorkOrder ↔ MaintenanceRequest
- BR14/BR15: Chặn xóa Property/Contractor đang được tham chiếu

## Chạy test

```
mvn clean test
```

Test bao phủ: tạo yêu cầu bảo trì + validate, duyệt báo giá + chống duyệt trùng, điều kiện tạo
phiếu công việc + vòng đời start/complete, nghiệm thu PASSED/FAILED, validate chi phí không âm.
