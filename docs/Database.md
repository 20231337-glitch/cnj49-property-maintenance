# Mo ta Database - CNJ49 Property Maintenance

Database: `cnj49_property_maintenance` (MySQL 8.x, utf8mb4). Schema duoc Hibernate tu tao/cap nhat
qua `spring.jpa.hibernate.ddl-auto=update` khi ung dung khoi dong.

## properties
| Cot | Kieu | Ghi chu |
|---|---|---|
| id | BIGINT PK | |
| code | VARCHAR(30) UNIQUE | tu sinh: PROP-0001 |
| name | VARCHAR(150) | |
| property_type | VARCHAR(40) | enum PropertyType |
| address | VARCHAR(255) | |
| number_of_floors | INT | |
| number_of_units | INT | |
| area | DECIMAL(12,2) | |
| operation_date | DATE | |
| status | VARCHAR(30) | enum PropertyStatus |
| description | VARCHAR(1000) | |
| created_at / updated_at | DATETIME | |

## units
`property_id` (FK -> properties.id), `code`, `name`, `floor_number`, `unit_type`, `area`, `status`, `description`.
Rang buoc unique (property_id, code).

## maintenance_categories
`name` (unique), `description`, `active`.

## maintenance_requests
`request_code` (unique, MR-yyyy-xxxx), `property_id` (FK), `unit_id` (FK, nullable),
`category_id` (FK), `title`, `description`, `priority`, `status`, `reported_date`,
`expected_completion_date`, `actual_completion_date`, `estimated_cost`, `reported_by`, `notes`.

## contractors
`contractor_code` (unique, CTR-0001), `company_name`, `contact_person`, `phone`, `email`,
`address`, `tax_code`, `specialization`, `rating` (0-5), `status`, `notes`.

## quotations
`quotation_code` (unique, QT-yyyy-xxxx), `maintenance_request_id` (FK), `contractor_id` (FK),
`quotation_date`, `material_cost`, `labor_cost`, `other_cost`, `total_amount` (tinh tu 3 cot tren),
`estimated_days`, `description`, `status` (PENDING/APPROVED/REJECTED), `approved_at`.

## work_orders
`work_order_code` (unique, WO-yyyy-xxxx), `maintenance_request_id` (FK), `quotation_id` (FK),
`contractor_id` (FK), `start_date`, `expected_completion_date`, `actual_completion_date`,
`work_description`, `status`, `result`, `warranty_until`.

## inspections
`work_order_id` (FK), `inspection_date`, `inspector_name`, `result` (PASSED/FAILED),
`actual_cost`, `notes`.

## expenses
`expense_code` (unique, EXP-yyyy-xxxx), `property_id` (FK), `work_order_id` (FK, nullable),
`contractor_id` (FK, nullable), `expense_type`, `amount`, `expense_date`, `description`,
`reference_number`, `notes`.

## users
`username` (unique), `password` (BCrypt hash), `full_name`, `email`, `role`, `active`.

## audit_logs
`action`, `entity_type`, `entity_id`, `description`, `username`, `created_at`.
Dung de hien thi timeline lich su xu ly tren trang chi tiet yeu cau bao tri.
