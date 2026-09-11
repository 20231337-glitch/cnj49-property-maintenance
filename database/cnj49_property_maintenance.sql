-- =====================================================================
-- CNJ49 - Quan ly bao tri, nha thau va chi phi van hanh bat dong san cho thue
-- Schema + du lieu mau, xuat tu ung dung that (Hibernate ddl-auto + DataSeeder)
-- Tao database truoc khi chay file nay:
--   CREATE DATABASE IF NOT EXISTS cnj49_property_maintenance
--     DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_unicode_ci;
--   USE cnj49_property_maintenance;
-- =====================================================================
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE `audit_logs`(
    `created_at` TIMESTAMP(6) NOT NULL,
    `entity_id` BIGINT,
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `entity_type` VARCHAR(50) NOT NULL,
    `username` VARCHAR(50),
    `description` VARCHAR(500),
    `action` ENUM('APPROVE_QUOTATION', 'CANCEL_MAINTENANCE_REQUEST', 'CANCEL_WORK_ORDER', 'CLOSE_MAINTENANCE_REQUEST', 'COMPLETE_WORK_ORDER', 'CREATE_EXPENSE', 'CREATE_MAINTENANCE_REQUEST', 'CREATE_QUOTATION', 'CREATE_WORK_ORDER', 'INSPECTION_FAILED', 'INSPECTION_PASSED', 'PAUSE_WORK_ORDER', 'REJECT_QUOTATION', 'RESUME_WORK_ORDER', 'START_WORK_ORDER', 'UPDATE_MAINTENANCE_REQUEST') NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE TABLE `contractors`(
    `rating` DECIMAL(3, 1) NOT NULL,
    `created_at` TIMESTAMP(6) NOT NULL,
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `updated_at` TIMESTAMP(6),
    `phone` VARCHAR(20) NOT NULL,
    `contractor_code` VARCHAR(30) NOT NULL,
    `tax_code` VARCHAR(30),
    `contact_person` VARCHAR(100),
    `email` VARCHAR(120),
    `company_name` VARCHAR(150) NOT NULL,
    `notes` VARCHAR(1000),
    `address` VARCHAR(255),
    `specialization` ENUM('CONSTRUCTION', 'ELECTRICAL', 'ELEVATOR', 'EQUIPMENT', 'FIRE_SAFETY', 'HVAC', 'INTERIOR', 'MULTI_SERVICE', 'PLUMBING') NOT NULL,
    `status` ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
INSERT INTO `contractors` VALUES
(4.0, '2026-09-12 04:39:20.009724', 1, '2026-09-12 04:39:20.009724', '0912345001', 'CTR-0001', 'MST1000', 'Nguyễn Văn An', 'nguyễn.văn.an@contractor.vn', 'Điện lạnh Hà Nội', NULL, 'Hà Nội, Việt Nam', 'HVAC', 'ACTIVE'),
(3.8, '2026-09-12 04:39:20.032724', 2, '2026-09-12 04:39:20.032724', '0912345002', 'CTR-0002', 'MST1001', 'Lê Thị Bình', 'lê.thị.bình@contractor.vn', 'Điện lạnh Thành Công', NULL, 'Hà Nội, Việt Nam', 'HVAC', 'ACTIVE'),
(3.8, '2026-09-12 04:39:20.046727', 3, '2026-09-12 04:39:20.046727', '0912345003', 'CTR-0003', 'MST1002', 'Trần Văn Cường', 'trần.văn.cường@contractor.vn', 'Công ty Điện nước Sài Gòn', NULL, 'Hà Nội, Việt Nam', 'PLUMBING', 'ACTIVE'),
(4.9, '2026-09-12 04:39:20.07458', 4, '2026-09-12 04:39:20.07458', '0912345004', 'CTR-0004', 'MST1003', 'Phạm Thị Dung', 'phạm.thị.dung@contractor.vn', 'Thang máy Thiên Nam', NULL, 'Hà Nội, Việt Nam', 'ELEVATOR', 'ACTIVE'),
(3.5, '2026-09-12 04:39:20.084645', 5, '2026-09-12 04:39:20.084645', '0912345005', 'CTR-0005', 'MST1004', 'Hoàng Văn Em', 'hoàng.văn.em@contractor.vn', 'PCCC An Toàn', NULL, 'Hà Nội, Việt Nam', 'FIRE_SAFETY', 'ACTIVE'),
(4.5, '2026-09-12 04:39:20.099769', 6, '2026-09-12 04:39:20.099769', '0912345006', 'CTR-0006', 'MST1005', 'Vũ Thị Giang', 'vũ.thị.giang@contractor.vn', 'Xây dựng Việt Tiến', NULL, 'Hà Nội, Việt Nam', 'CONSTRUCTION', 'ACTIVE'),
(4.0, '2026-09-12 04:39:20.11176', 7, '2026-09-12 04:39:20.11176', '0912345007', 'CTR-0007', 'MST1006', 'Đặng Văn Hùng', 'đặng.văn.hùng@contractor.vn', 'Nội thất Hoàng Gia', NULL, 'Hà Nội, Việt Nam', 'INTERIOR', 'ACTIVE'),
(4.3, '2026-09-12 04:39:20.124757', 8, '2026-09-12 04:39:20.124757', '0912345008', 'CTR-0008', 'MST1007', 'Bùi Thị Lan', 'bùi.thị.lan@contractor.vn', 'Điện Quang Minh', NULL, 'Hà Nội, Việt Nam', 'ELECTRICAL', 'ACTIVE'),
(3.9, '2026-09-12 04:39:20.137759', 9, '2026-09-12 04:39:20.137759', '0912345009', 'CTR-0009', 'MST1008', 'Ngô Văn Khoa', 'ngô.văn.khoa@contractor.vn', 'Thiết bị công nghiệp Đông Á', NULL, 'Hà Nội, Việt Nam', 'EQUIPMENT', 'ACTIVE'),
(4.3, '2026-09-12 04:39:20.149939', 10, '2026-09-12 04:39:20.149939', '0912345010', 'CTR-0010', 'MST1009', 'Đinh Thị Mai', 'đinh.thị.mai@contractor.vn', 'Dịch vụ tổng hợp Phú Quý', NULL, 'Hà Nội, Việt Nam', 'MULTI_SERVICE', 'ACTIVE');
CREATE TABLE `expenses`(
    `amount` DECIMAL(15, 2) NOT NULL,
    `expense_date` DATE NOT NULL,
    `contractor_id` BIGINT,
    `created_at` TIMESTAMP(6) NOT NULL,
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `property_id` BIGINT NOT NULL,
    `updated_at` TIMESTAMP(6),
    `work_order_id` BIGINT,
    `expense_code` VARCHAR(30) NOT NULL,
    `reference_number` VARCHAR(60),
    `description` VARCHAR(500) NOT NULL,
    `notes` VARCHAR(1000),
    `expense_type` ENUM('CLEANING', 'LABOR', 'MAINTENANCE', 'MATERIAL', 'OTHER', 'REPAIR', 'SECURITY', 'UTILITY') NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
INSERT INTO `expenses` VALUES
(1821889.00, '2026-07-20', 1, '2026-09-12 04:39:22.010481', 1, 1, '2026-09-12 04:39:22.010481', 1, 'EXP-2026-0001', 'WO-2026-0001', 'Chi phí bảo trì theo phiếu WO-2026-0001 - Mất điện toàn bộ khu vực Chung cư Mini Cầu Giấy', NULL, 'MAINTENANCE'),
(1794719.00, '2026-07-23', 2, '2026-09-12 04:39:22.039868', 2, 2, '2026-09-12 04:39:22.039868', 2, 'EXP-2026-0002', 'WO-2026-0002', 'Chi phí bảo trì theo phiếu WO-2026-0002 - Ổ cắm điện bị cháy Phòng 302', NULL, 'MAINTENANCE'),
(1883997.00, '2026-07-22', 3, '2026-09-12 04:39:22.066948', 3, 3, '2026-09-12 04:39:22.066948', 3, 'EXP-2026-0003', 'WO-2026-0003', 'Chi phí bảo trì theo phiếu WO-2026-0003 - Đèn hành lang không sáng Phòng 403', NULL, 'MAINTENANCE'),
(1817142.00, '2026-07-26', 4, '2026-09-12 04:39:22.089172', 4, 4, '2026-09-12 04:39:22.089172', 4, 'EXP-2026-0004', 'WO-2026-0004', 'Chi phí bảo trì theo phiếu WO-2026-0004 - Rò rỉ nước tại nhà vệ sinh Phòng 504', NULL, 'MAINTENANCE'),
(1850054.00, '2026-07-27', 5, '2026-09-12 04:39:22.115988', 5, 1, '2026-09-12 04:39:22.115988', 5, 'EXP-2026-0005', 'WO-2026-0005', 'Chi phí bảo trì theo phiếu WO-2026-0005 - Vòi nước bị hỏng Phòng 105', NULL, 'MAINTENANCE'),
(1523198.00, '2026-07-25', 6, '2026-09-12 04:39:22.13758', 6, 2, '2026-09-12 04:39:22.13758', 6, 'EXP-2026-0006', 'WO-2026-0006', 'Chi phí bảo trì theo phiếu WO-2026-0006 - Bồn cầu bị tắc nghẽn Nhà trọ Mỹ Đình', NULL, 'MAINTENANCE'),
(1394620.00, '2026-07-26', 7, '2026-09-12 04:39:22.157589', 7, 3, '2026-09-12 04:39:22.157589', 7, 'EXP-2026-0007', 'WO-2026-0007', 'Chi phí bảo trì theo phiếu WO-2026-0007 - Điều hòa phòng không làm lạnh Phòng 201', NULL, 'MAINTENANCE'),
(1375701.00, '2026-07-27', 8, '2026-09-12 04:39:22.184726', 8, 4, '2026-09-12 04:39:22.184726', 8, 'EXP-2026-0008', 'WO-2026-0008', 'Chi phí bảo trì theo phiếu WO-2026-0008 - Điều hòa bị chảy nước Phòng 201', NULL, 'MAINTENANCE'),
(1220331.00, '2026-06-06', NULL, '2026-09-12 04:39:22.437268', 9, 1, '2026-09-12 04:39:22.437268', NULL, 'EXP-2026-0009', 'HD2000', 'Tiền điện khu vực chung tháng - Chung cư Mini Cầu Giấy', NULL, 'UTILITY'),
(2212293.00, '2026-05-19', NULL, '2026-09-12 04:39:22.462269', 10, 2, '2026-09-12 04:39:22.462269', NULL, 'EXP-2026-0010', 'HD2001', 'Tiền nước sinh hoạt tháng - Nhà trọ Mỹ Đình', NULL, 'CLEANING'),
(3187978.00, '2026-07-08', NULL, '2026-09-12 04:39:22.477269', 11, 3, '2026-09-12 04:39:22.477269', NULL, 'EXP-2026-0011', 'HD2002', 'Chi phí vệ sinh định kỳ - Căn hộ cho thuê Hà Đông', NULL, 'SECURITY'),
(2869379.00, '2026-06-15', NULL, '2026-09-12 04:39:22.492269', 12, 4, '2026-09-12 04:39:22.492269', NULL, 'EXP-2026-0012', 'HD2003', 'Chi phí bảo vệ an ninh tháng - Chung cư Botanica Cầu Giấy', NULL, 'OTHER'),
(2779943.00, '2026-07-31', NULL, '2026-09-12 04:39:22.516276', 13, 1, '2026-09-12 04:39:22.516276', NULL, 'EXP-2026-0013', 'HD2004', 'Chi phí thu gom rác thải - Chung cư Mini Cầu Giấy', NULL, 'UTILITY'),
(2563374.00, '2026-09-03', NULL, '2026-09-12 04:39:22.547267', 14, 2, '2026-09-12 04:39:22.547267', NULL, 'EXP-2026-0014', 'HD2005', 'Chi phí bảo trì thang máy định kỳ - Nhà trọ Mỹ Đình', NULL, 'CLEANING');
INSERT INTO `expenses` VALUES
(2976668.00, '2026-06-26', NULL, '2026-09-12 04:39:22.572281', 15, 3, '2026-09-12 04:39:22.572281', NULL, 'EXP-2026-0015', 'HD2006', 'Tiền điện khu vực chung tháng - Căn hộ cho thuê Hà Đông', NULL, 'SECURITY'),
(1227188.00, '2026-05-16', NULL, '2026-09-12 04:39:22.581308', 16, 4, '2026-09-12 04:39:22.581308', NULL, 'EXP-2026-0016', 'HD2007', 'Tiền nước sinh hoạt tháng - Chung cư Botanica Cầu Giấy', NULL, 'OTHER'),
(962377.00, '2026-07-02', NULL, '2026-09-12 04:39:22.601361', 17, 1, '2026-09-12 04:39:22.601361', NULL, 'EXP-2026-0017', 'HD2008', 'Chi phí vệ sinh định kỳ - Chung cư Mini Cầu Giấy', NULL, 'UTILITY'),
(1125991.00, '2026-09-08', NULL, '2026-09-12 04:39:22.617268', 18, 2, '2026-09-12 04:39:22.617268', NULL, 'EXP-2026-0018', 'HD2009', 'Chi phí bảo vệ an ninh tháng - Nhà trọ Mỹ Đình', NULL, 'CLEANING'),
(1526780.00, '2026-09-12', NULL, '2026-09-12 04:39:22.630318', 19, 3, '2026-09-12 04:39:22.630318', NULL, 'EXP-2026-0019', 'HD2010', 'Chi phí thu gom rác thải - Căn hộ cho thuê Hà Đông', NULL, 'SECURITY'),
(973693.00, '2026-08-05', NULL, '2026-09-12 04:39:22.644308', 20, 4, '2026-09-12 04:39:22.644308', NULL, 'EXP-2026-0020', 'HD2011', 'Chi phí bảo trì thang máy định kỳ - Chung cư Botanica Cầu Giấy', NULL, 'OTHER'),
(3181948.00, '2026-06-10', NULL, '2026-09-12 04:39:22.658292', 21, 1, '2026-09-12 04:39:22.658292', NULL, 'EXP-2026-0021', 'HD2012', 'Tiền điện khu vực chung tháng - Chung cư Mini Cầu Giấy', NULL, 'UTILITY'),
(2582305.00, '2026-07-15', NULL, '2026-09-12 04:39:22.672405', 22, 2, '2026-09-12 04:39:22.672405', NULL, 'EXP-2026-0022', 'HD2013', 'Tiền nước sinh hoạt tháng - Nhà trọ Mỹ Đình', NULL, 'CLEANING'),
(2943731.00, '2026-06-21', NULL, '2026-09-12 04:39:22.698414', 23, 3, '2026-09-12 04:39:22.698414', NULL, 'EXP-2026-0023', 'HD2014', 'Chi phí vệ sinh định kỳ - Căn hộ cho thuê Hà Đông', NULL, 'SECURITY'),
(830323.00, '2026-05-10', NULL, '2026-09-12 04:39:22.714416', 24, 4, '2026-09-12 04:39:22.714416', NULL, 'EXP-2026-0024', 'HD2015', 'Chi phí bảo vệ an ninh tháng - Chung cư Botanica Cầu Giấy', NULL, 'OTHER'),
(2824181.00, '2026-07-13', NULL, '2026-09-12 04:39:22.728414', 25, 1, '2026-09-12 04:39:22.728414', NULL, 'EXP-2026-0025', 'HD2016', 'Chi phí thu gom rác thải - Chung cư Mini Cầu Giấy', NULL, 'UTILITY'),
(1487843.00, '2026-05-12', NULL, '2026-09-12 04:39:22.742407', 26, 2, '2026-09-12 04:39:22.742407', NULL, 'EXP-2026-0026', 'HD2017', 'Chi phí bảo trì thang máy định kỳ - Nhà trọ Mỹ Đình', NULL, 'CLEANING'),
(1861553.00, '2026-08-27', NULL, '2026-09-12 04:39:22.749417', 27, 3, '2026-09-12 04:39:22.749417', NULL, 'EXP-2026-0027', 'HD2018', 'Tiền điện khu vực chung tháng - Căn hộ cho thuê Hà Đông', NULL, 'SECURITY'),
(699493.00, '2026-05-11', NULL, '2026-09-12 04:39:22.756414', 28, 4, '2026-09-12 04:39:22.756414', NULL, 'EXP-2026-0028', 'HD2019', 'Tiền nước sinh hoạt tháng - Chung cư Botanica Cầu Giấy', NULL, 'OTHER'),
(895987.00, '2026-05-30', NULL, '2026-09-12 04:39:22.77642', 29, 1, '2026-09-12 04:39:22.77642', NULL, 'EXP-2026-0029', 'HD2020', 'Chi phí vệ sinh định kỳ - Chung cư Mini Cầu Giấy', NULL, 'UTILITY'),
(1846417.00, '2026-05-17', NULL, '2026-09-12 04:39:22.790408', 30, 2, '2026-09-12 04:39:22.790408', NULL, 'EXP-2026-0030', 'HD2021', 'Chi phí bảo vệ an ninh tháng - Nhà trọ Mỹ Đình', NULL, 'CLEANING');
CREATE TABLE `inspections`(
    `actual_cost` DECIMAL(15, 2) NOT NULL,
    `inspection_date` DATE NOT NULL,
    `created_at` TIMESTAMP(6) NOT NULL,
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `work_order_id` BIGINT NOT NULL,
    `inspector_name` VARCHAR(100) NOT NULL,
    `notes` VARCHAR(1000),
    `result` ENUM('FAILED', 'PASSED') NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
INSERT INTO `inspections` VALUES
(1821889.00, '2026-07-20', '2026-09-12 04:39:21.978355', 1, 1, 'Trịnh Hoàng Thành', 'Công việc đạt yêu cầu.', 'PASSED'),
(1794719.00, '2026-07-23', '2026-09-12 04:39:22.017477', 2, 2, 'Trịnh Hoàng Thành', 'Công việc đạt yêu cầu.', 'PASSED'),
(1883997.00, '2026-07-22', '2026-09-12 04:39:22.054957', 3, 3, 'Trịnh Hoàng Thành', 'Công việc đạt yêu cầu.', 'PASSED'),
(1817142.00, '2026-07-26', '2026-09-12 04:39:22.07475', 4, 4, 'Trịnh Hoàng Thành', 'Công việc đạt yêu cầu.', 'PASSED'),
(1850054.00, '2026-07-27', '2026-09-12 04:39:22.096523', 5, 5, 'Trịnh Hoàng Thành', 'Công việc đạt yêu cầu.', 'PASSED'),
(1523198.00, '2026-07-25', '2026-09-12 04:39:22.116988', 6, 6, 'Trịnh Hoàng Thành', 'Công việc đạt yêu cầu.', 'PASSED'),
(1394620.00, '2026-07-26', '2026-09-12 04:39:22.143803', 7, 7, 'Trịnh Hoàng Thành', 'Công việc đạt yêu cầu.', 'PASSED'),
(1375701.00, '2026-07-27', '2026-09-12 04:39:22.165624', 8, 8, 'Trịnh Hoàng Thành', 'Công việc đạt yêu cầu.', 'PASSED'),
(2188401.00, '2026-07-28', '2026-09-12 04:39:22.191721', 9, 9, 'Trịnh Hoàng Thành', 'Chưa đạt, yêu cầu làm lại.', 'FAILED'),
(1257975.00, '2026-07-29', '2026-09-12 04:39:22.199731', 10, 10, 'Trịnh Hoàng Thành', 'Chưa đạt, yêu cầu làm lại.', 'FAILED');
CREATE TABLE `maintenance_categories`(
    `active` BOOLEAN NOT NULL,
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `description` VARCHAR(500),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
INSERT INTO `maintenance_categories` VALUES
(TRUE, 1, 'Điện', 'Hạng mục bảo trì: Điện'),
(TRUE, 2, 'Nước', 'Hạng mục bảo trì: Nước'),
(TRUE, 3, 'Điều hòa', 'Hạng mục bảo trì: Điều hòa'),
(TRUE, 4, 'Thang máy', 'Hạng mục bảo trì: Thang máy'),
(TRUE, 5, 'PCCC', 'Hạng mục bảo trì: PCCC'),
(TRUE, 6, 'Camera', 'Hạng mục bảo trì: Camera'),
(TRUE, 7, 'Nội thất', 'Hạng mục bảo trì: Nội thất'),
(TRUE, 8, 'Xây dựng', 'Hạng mục bảo trì: Xây dựng'),
(TRUE, 9, 'Thiết bị', 'Hạng mục bảo trì: Thiết bị'),
(TRUE, 10, 'Vệ sinh', 'Hạng mục bảo trì: Vệ sinh');
CREATE TABLE `maintenance_requests`(
    `actual_completion_date` DATE,
    `estimated_cost` DECIMAL(15, 2),
    `expected_completion_date` DATE,
    `reported_date` DATE NOT NULL,
    `category_id` BIGINT NOT NULL,
    `created_at` TIMESTAMP(6) NOT NULL,
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `property_id` BIGINT NOT NULL,
    `unit_id` BIGINT,
    `updated_at` TIMESTAMP(6),
    `request_code` VARCHAR(30) NOT NULL,
    `reported_by` VARCHAR(100),
    `title` VARCHAR(200) NOT NULL,
    `notes` VARCHAR(1000),
    `description` VARCHAR(2000) NOT NULL,
    `priority` ENUM('HIGH', 'LOW', 'MEDIUM', 'URGENT') NOT NULL,
    `status` ENUM('CANCELLED', 'CLOSED', 'COMPLETED', 'IN_PROGRESS', 'NEW', 'QUOTATION_APPROVED', 'UNDER_REVIEW', 'WAITING_INSPECTION', 'WAITING_QUOTATION') NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
INSERT INTO `maintenance_requests` VALUES
('2026-07-20', 1851889.00, '2026-07-21', '2026-07-14', 1, '2026-09-12 04:39:20.418862', 1, 1, NULL, '2026-09-12 04:39:22.797405', 'MR-2026-0001', 'Nhân viên trực tòa nhà', 'Mất điện toàn bộ khu vực Chung cư Mini Cầu Giấy', NULL, 'Toàn bộ khu vực bị mất điện đột ngột, cần kiểm tra hệ thống dây điện', 'LOW', 'CLOSED'),
('2026-07-23', 1854719.00, '2026-07-22', '2026-07-15', 1, '2026-09-12 04:39:20.446811', 2, 2, 8, '2026-09-12 04:39:22.797405', 'MR-2026-0002', 'Nhân viên trực tòa nhà', 'Ổ cắm điện bị cháy Phòng 302', NULL, 'Ổ cắm điện tại phòng phát hiện có mùi khét, cần thay thế', 'MEDIUM', 'CLOSED'),
('2026-07-22', 1973997.00, '2026-07-23', '2026-07-16', 1, '2026-09-12 04:39:20.465808', 3, 3, 15, '2026-09-12 04:39:22.797405', 'MR-2026-0003', 'Nhân viên trực tòa nhà', 'Đèn hành lang không sáng Phòng 403', NULL, 'Hệ thống đèn chiếu sáng hành lang không hoạt động', 'HIGH', 'CLOSED'),
('2026-07-26', 1937142.00, '2026-07-24', '2026-07-17', 2, '2026-09-12 04:39:20.494812', 4, 4, 22, '2026-09-12 04:39:22.797405', 'MR-2026-0004', 'Nhân viên trực tòa nhà', 'Rò rỉ nước tại nhà vệ sinh Phòng 504', NULL, 'Phát hiện rò rỉ nước dưới sàn nhà vệ sinh', 'URGENT', 'COMPLETED'),
('2026-07-27', 2000054.00, '2026-07-25', '2026-07-18', 2, '2026-09-12 04:39:20.509808', 5, 1, 5, '2026-09-12 04:39:22.797405', 'MR-2026-0005', 'Nhân viên trực tòa nhà', 'Vòi nước bị hỏng Phòng 105', NULL, 'Vòi nước trong bếp bị chảy liên tục không tắt được', 'LOW', 'COMPLETED'),
('2026-07-25', 1703198.00, '2026-07-26', '2026-07-19', 2, '2026-09-12 04:39:20.518808', 6, 2, NULL, '2026-09-12 04:39:22.797405', 'MR-2026-0006', 'Nhân viên trực tòa nhà', 'Bồn cầu bị tắc nghẽn Nhà trọ Mỹ Đình', NULL, 'Bồn cầu bị tắc, nước không thoát được', 'MEDIUM', 'COMPLETED'),
('2026-07-26', 1604620.00, '2026-07-27', '2026-07-20', 3, '2026-09-12 04:39:20.528811', 7, 3, 13, '2026-09-12 04:39:22.797405', 'MR-2026-0007', 'Nhân viên trực tòa nhà', 'Điều hòa phòng không làm lạnh Phòng 201', NULL, 'Điều hòa hoạt động nhưng không làm lạnh được phòng', 'HIGH', 'COMPLETED'),
('2026-07-27', 1615701.00, '2026-07-28', '2026-07-21', 3, '2026-09-12 04:39:20.547814', 8, 4, 19, '2026-09-12 04:39:22.797405', 'MR-2026-0008', 'Nhân viên trực tòa nhà', 'Điều hòa bị chảy nước Phòng 201', NULL, 'Điều hòa chảy nước xuống sàn khi vận hành', 'URGENT', 'COMPLETED'),
(NULL, 2188401.00, '2026-07-29', '2026-07-22', 3, '2026-09-12 04:39:20.560818', 9, 1, 3, '2026-09-12 04:39:22.797405', 'MR-2026-0009', 'Nhân viên trực tòa nhà', 'Điều hòa có tiếng ồn lạ Phòng 403', NULL, 'Điều hòa phát ra tiếng ồn bất thường khi hoạt động', 'LOW', 'IN_PROGRESS'),
(NULL, 1257975.00, '2026-07-30', '2026-07-23', 4, '2026-09-12 04:39:20.570347', 10, 2, 10, '2026-09-12 04:39:22.797405', 'MR-2026-0010', 'Nhân viên trực tòa nhà', 'Thang máy kêu to bất thường Phòng 204', NULL, 'Thang máy phát ra tiếng động lớn khi di chuyển', 'MEDIUM', 'IN_PROGRESS');
INSERT INTO `maintenance_requests` VALUES
(NULL, 2191754.00, '2026-07-31', '2026-07-24', 4, '2026-09-12 04:39:20.580336', 11, 3, NULL, '2026-09-12 04:39:22.797405', 'MR-2026-0011', 'Nhân viên trực tòa nhà', 'Thang máy dừng đột ngột Căn hộ cho thuê Hà Đông', NULL, 'Thang máy dừng giữa chừng, cần kiểm tra hệ thống', 'HIGH', 'QUOTATION_APPROVED'),
(NULL, 1649693.00, '2026-08-01', '2026-07-25', 5, '2026-09-12 04:39:20.589741', 12, 4, 23, '2026-09-12 04:39:22.797405', 'MR-2026-0012', 'Nhân viên trực tòa nhà', 'Bình chữa cháy hết hạn Phòng 605', NULL, 'Bình chữa cháy đã hết hạn sử dụng, cần thay mới', 'URGENT', 'IN_PROGRESS'),
(NULL, 1591748.00, '2026-08-02', '2026-07-26', 5, '2026-09-12 04:39:20.621743', 13, 1, 1, '2026-09-12 04:39:22.797405', 'MR-2026-0013', 'Nhân viên trực tòa nhà', 'Chuông báo cháy kêu liên tục Phòng 201', NULL, 'Hệ thống báo cháy kêu liên tục không rõ nguyên nhân', 'LOW', 'QUOTATION_APPROVED'),
(NULL, 1893540.00, '2026-08-03', '2026-07-27', 6, '2026-09-12 04:39:20.636743', 14, 2, 8, '2026-09-12 04:39:22.797405', 'MR-2026-0014', 'Nhân viên trực tòa nhà', 'Camera an ninh mất tín hiệu Phòng 302', NULL, 'Camera khu vực sảnh chính bị mất tín hiệu', 'MEDIUM', 'QUOTATION_APPROVED'),
(NULL, 1986236.00, '2026-08-04', '2026-07-28', 6, '2026-09-12 04:39:20.660743', 15, 3, 15, '2026-09-12 04:39:22.797405', 'MR-2026-0015', 'Nhân viên trực tòa nhà', 'Camera hành lang bị mờ Phòng 403', NULL, 'Hình ảnh camera hành lang bị mờ, cần vệ sinh ống kính', 'HIGH', 'IN_PROGRESS'),
(NULL, 2248443.00, '2026-08-05', '2026-07-29', 7, '2026-09-12 04:39:20.677757', 16, 4, NULL, '2026-09-12 04:39:22.797916', 'MR-2026-0016', 'Nhân viên trực tòa nhà', 'Cửa gỗ bị mối mọt Chung cư Botanica Cầu Giấy', NULL, 'Cửa gỗ phòng bị mối mọt, cần xử lý', 'URGENT', 'WAITING_QUOTATION'),
(NULL, 1127193.00, '2026-08-06', '2026-07-30', 7, '2026-09-12 04:39:20.68575', 17, 1, 5, '2026-09-12 04:39:22.797937', 'MR-2026-0017', 'Nhân viên trực tòa nhà', 'Bản lề cửa bị lỏng Phòng 105', NULL, 'Bản lề cửa ra vào bị lỏng, đóng mở khó khăn', 'LOW', 'WAITING_QUOTATION'),
(NULL, 2024440.00, '2026-08-07', '2026-07-31', 8, '2026-09-12 04:39:20.707683', 18, 2, 12, '2026-09-12 04:39:22.797937', 'MR-2026-0018', 'Nhân viên trực tòa nhà', 'Tường bị nứt Phòng 106', NULL, 'Phát hiện vết nứt trên tường cần kiểm tra kết cấu', 'MEDIUM', 'WAITING_QUOTATION'),
(NULL, 705558.00, '2026-08-08', '2026-08-01', 8, '2026-09-12 04:39:20.719681', 19, 3, 13, '2026-09-12 04:39:22.797937', 'MR-2026-0019', 'Nhân viên trực tòa nhà', 'Trần nhà bị thấm nước Phòng 201', NULL, 'Trần nhà xuất hiện vết ố do thấm nước từ tầng trên', 'HIGH', 'WAITING_QUOTATION'),
(NULL, 2264756.00, '2026-08-09', '2026-08-02', 9, '2026-09-12 04:39:20.731585', 20, 4, 24, '2026-09-12 04:39:22.797937', 'MR-2026-0020', 'Nhân viên trực tòa nhà', 'Máy bơm nước bị hỏng Phòng 706', NULL, 'Máy bơm nước sinh hoạt không hoạt động', 'URGENT', 'WAITING_QUOTATION');
INSERT INTO `maintenance_requests` VALUES
(NULL, 1477160.00, '2026-08-10', '2026-08-03', 9, '2026-09-12 04:39:20.741575', 21, 1, NULL, '2026-09-12 04:39:22.797937', 'MR-2026-0021', 'Nhân viên trực tòa nhà', 'Quạt thông gió không hoạt động Chung cư Mini Cầu Giấy', NULL, 'Quạt thông gió tại tầng hầm bị hỏng', 'LOW', 'WAITING_QUOTATION'),
(NULL, 1064410.00, '2026-08-11', '2026-08-04', 10, '2026-09-12 04:39:20.755495', 22, 2, 10, '2026-09-12 04:39:22.797937', 'MR-2026-0022', 'Nhân viên trực tòa nhà', 'Khu vực chung không được dọn dẹp Phòng 204', NULL, 'Khu vực sảnh chưa được vệ sinh theo lịch', 'MEDIUM', 'WAITING_QUOTATION'),
(NULL, 2225713.00, '2026-08-12', '2026-08-05', 10, '2026-09-12 04:39:20.771572', 23, 3, 17, '2026-09-12 04:39:22.797937', 'MR-2026-0023', 'Nhân viên trực tòa nhà', 'Thùng rác đầy tràn Phòng 205', NULL, 'Thùng rác khu vực chung đầy, cần thu gom', 'HIGH', 'WAITING_QUOTATION'),
(NULL, 1704059.00, '2026-08-13', '2026-08-06', 4, '2026-09-12 04:39:20.79115', 24, 4, 21, '2026-09-12 04:39:22.797937', 'MR-2026-0024', 'Nhân viên trực tòa nhà', 'Bảng chỉ dẫn bị hỏng Phòng 403', NULL, 'Bảng chỉ dẫn tầng bị bong tróc, khó đọc', 'URGENT', 'WAITING_QUOTATION'),
(NULL, 528261.00, '2026-08-14', '2026-08-07', 1, '2026-09-12 04:39:20.807149', 25, 1, 1, '2026-09-12 04:39:22.797937', 'MR-2026-0025', 'Nhân viên trực tòa nhà', 'Mất điện toàn bộ khu vực Phòng 201', NULL, 'Toàn bộ khu vực bị mất điện đột ngột, cần kiểm tra hệ thống dây điện', 'LOW', 'WAITING_QUOTATION'),
(NULL, 1126003.00, '2026-08-15', '2026-08-08', 1, '2026-09-12 04:39:20.823248', 26, 2, NULL, '2026-09-12 04:39:22.797937', 'MR-2026-0026', 'Nhân viên trực tòa nhà', 'Ổ cắm điện bị cháy Nhà trọ Mỹ Đình', NULL, 'Ổ cắm điện tại phòng phát hiện có mùi khét, cần thay thế', 'MEDIUM', 'UNDER_REVIEW'),
(NULL, 525897.00, '2026-08-16', '2026-08-09', 1, '2026-09-12 04:39:20.841247', 27, 3, 15, '2026-09-12 04:39:20.841247', 'MR-2026-0027', 'Nhân viên trực tòa nhà', 'Đèn hành lang không sáng Phòng 403', NULL, 'Hệ thống đèn chiếu sáng hành lang không hoạt động', 'HIGH', 'NEW'),
(NULL, 2222819.00, '2026-08-17', '2026-08-10', 2, '2026-09-12 04:39:20.881217', 28, 4, 25, '2026-09-12 04:39:22.797937', 'MR-2026-0028', 'Nhân viên trực tòa nhà', 'Rò rỉ nước tại nhà vệ sinh Phòng EXT25', NULL, 'Phát hiện rò rỉ nước dưới sàn nhà vệ sinh', 'URGENT', 'UNDER_REVIEW'),
(NULL, 827552.00, '2026-08-18', '2026-08-11', 2, '2026-09-12 04:39:20.901214', 29, 1, 5, '2026-09-12 04:39:20.901214', 'MR-2026-0029', 'Nhân viên trực tòa nhà', 'Vòi nước bị hỏng Phòng 105', NULL, 'Vòi nước trong bếp bị chảy liên tục không tắt được', 'LOW', 'NEW'),
(NULL, 1926601.00, '2026-08-19', '2026-08-12', 2, '2026-09-12 04:39:20.920642', 30, 2, 12, '2026-09-12 04:39:22.797937', 'MR-2026-0030', 'Nhân viên trực tòa nhà', 'Bồn cầu bị tắc nghẽn Phòng 106', NULL, 'Bồn cầu bị tắc, nước không thoát được', 'MEDIUM', 'UNDER_REVIEW');
INSERT INTO `maintenance_requests` VALUES
(NULL, 2309094.00, '2026-08-20', '2026-08-13', 3, '2026-09-12 04:39:20.944055', 31, 3, NULL, '2026-09-12 04:39:20.944055', 'MR-2026-0031', 'Nhân viên trực tòa nhà', 'Điều hòa phòng không làm lạnh Căn hộ cho thuê Hà Đông', NULL, 'Điều hòa hoạt động nhưng không làm lạnh được phòng', 'HIGH', 'NEW'),
(NULL, 2150794.00, '2026-08-21', '2026-08-14', 3, '2026-09-12 04:39:20.951052', 32, 4, 22, '2026-09-12 04:39:22.797937', 'MR-2026-0032', 'Nhân viên trực tòa nhà', 'Điều hòa bị chảy nước Phòng 504', NULL, 'Điều hòa chảy nước xuống sàn khi vận hành', 'URGENT', 'UNDER_REVIEW'),
(NULL, 2492798.00, '2026-08-22', '2026-08-15', 3, '2026-09-12 04:39:20.959111', 33, 1, 3, '2026-09-12 04:39:20.959111', 'MR-2026-0033', 'Nhân viên trực tòa nhà', 'Điều hòa có tiếng ồn lạ Phòng 403', NULL, 'Điều hòa phát ra tiếng ồn bất thường khi hoạt động', 'LOW', 'NEW'),
(NULL, 1829719.00, '2026-08-23', '2026-08-16', 4, '2026-09-12 04:39:20.976433', 34, 2, 10, '2026-09-12 04:39:22.797937', 'MR-2026-0034', 'Nhân viên trực tòa nhà', 'Thang máy kêu to bất thường Phòng 204', NULL, 'Thang máy phát ra tiếng động lớn khi di chuyển', 'MEDIUM', 'UNDER_REVIEW'),
(NULL, 1683775.00, '2026-08-24', '2026-08-17', 4, '2026-09-12 04:39:20.98613', 35, 3, 17, '2026-09-12 04:39:20.98613', 'MR-2026-0035', 'Nhân viên trực tòa nhà', 'Thang máy dừng đột ngột Phòng 205', NULL, 'Thang máy dừng giữa chừng, cần kiểm tra hệ thống', 'HIGH', 'NEW');
CREATE TABLE `properties`(
    `area` DECIMAL(12, 2) NOT NULL,
    `number_of_floors` INTEGER NOT NULL,
    `number_of_units` INTEGER NOT NULL,
    `operation_date` DATE,
    `created_at` TIMESTAMP(6) NOT NULL,
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `updated_at` TIMESTAMP(6),
    `code` VARCHAR(30) NOT NULL,
    `name` VARCHAR(150) NOT NULL,
    `description` VARCHAR(1000),
    `address` VARCHAR(255) NOT NULL,
    `property_type` ENUM('APARTMENT_BUILDING', 'BOARDING_HOUSE', 'MINI_APARTMENT', 'OFFICE', 'OTHER', 'RENTAL_HOUSE') NOT NULL,
    `status` ENUM('ACTIVE', 'INACTIVE', 'UNDER_MAINTENANCE') NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
INSERT INTO `properties` VALUES
(80.00, 5, 20, '2025-09-12', '2026-09-12 04:39:20.169943', 1, '2026-09-12 04:39:20.169943', 'PROP-0001', 'Chung cư Mini Cầu Giấy', 'Bất động sản cho thuê tại Chung cư Mini Cầu Giấy', 'Số 12 Cầu Giấy, Hà Nội', 'MINI_APARTMENT', 'ACTIVE'),
(45.00, 3, 15, '2024-09-12', '2026-09-12 04:39:20.210939', 2, '2026-09-12 04:39:20.210939', 'PROP-0002', 'Nhà trọ Mỹ Đình', 'Bất động sản cho thuê tại Nhà trọ Mỹ Đình', 'Ngõ 55 Mỹ Đình, Hà Nội', 'BOARDING_HOUSE', 'ACTIVE'),
(60.00, 4, 12, '2023-09-12', '2026-09-12 04:39:20.218933', 3, '2026-09-12 04:39:20.218933', 'PROP-0003', 'Căn hộ cho thuê Hà Đông', 'Bất động sản cho thuê tại Căn hộ cho thuê Hà Đông', 'Đường Quang Trung, Hà Đông, Hà Nội', 'RENTAL_HOUSE', 'ACTIVE'),
(500.00, 18, 120, '2022-09-12', '2026-09-12 04:39:20.234936', 4, '2026-09-12 04:39:20.234936', 'PROP-0004', 'Chung cư Botanica Cầu Giấy', 'Bất động sản cho thuê tại Chung cư Botanica Cầu Giấy', 'Số 89 Dịch Vọng, Cầu Giấy, Hà Nội', 'APARTMENT_BUILDING', 'ACTIVE');
CREATE TABLE `quotations`(
    `estimated_days` INTEGER NOT NULL,
    `labor_cost` DECIMAL(15, 2) NOT NULL,
    `material_cost` DECIMAL(15, 2) NOT NULL,
    `other_cost` DECIMAL(15, 2) NOT NULL,
    `quotation_date` DATE NOT NULL,
    `total_amount` DECIMAL(15, 2) NOT NULL,
    `approved_at` TIMESTAMP(6),
    `contractor_id` BIGINT NOT NULL,
    `created_at` TIMESTAMP(6) NOT NULL,
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `maintenance_request_id` BIGINT NOT NULL,
    `updated_at` TIMESTAMP(6),
    `quotation_code` VARCHAR(30) NOT NULL,
    `description` VARCHAR(1000),
    `status` ENUM('APPROVED', 'PENDING', 'REJECTED') NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
INSERT INTO `quotations` VALUES
(3, 482390.00, 1269499.00, 100000.00, '2026-07-15', 1851889.00, '2026-09-12 04:39:21.004556', 1, '2026-09-12 04:39:21.005557', 1, 1, '2026-09-12 04:39:21.005557', 'QT-2026-0001', 'Báo giá thực hiện: Mất điện toàn bộ khu vực Chung cư Mini Cầu Giấy', 'APPROVED'),
(6, 760429.00, 1541453.00, 150000.00, '2026-07-15', 2451882.00, NULL, 2, '2026-09-12 04:39:21.018492', 2, 1, '2026-09-12 04:39:21.018492', 'QT-2026-0002', 'Báo giá thực hiện: Mất điện toàn bộ khu vực Chung cư Mini Cầu Giấy', 'REJECTED'),
(5, 880986.00, 873733.00, 100000.00, '2026-07-16', 1854719.00, '2026-09-12 04:39:21.074512', 2, '2026-09-12 04:39:21.074512', 3, 2, '2026-09-12 04:39:21.074512', 'QT-2026-0003', 'Báo giá thực hiện: Ổ cắm điện bị cháy Phòng 302', 'APPROVED'),
(3, 662093.00, 1790625.00, 150000.00, '2026-07-16', 2602718.00, NULL, 3, '2026-09-12 04:39:21.082214', 4, 2, '2026-09-12 04:39:21.082214', 'QT-2026-0004', 'Báo giá thực hiện: Ổ cắm điện bị cháy Phòng 302', 'REJECTED'),
(3, 461403.00, 1412594.00, 100000.00, '2026-07-17', 1973997.00, '2026-09-12 04:39:21.116983', 3, '2026-09-12 04:39:21.116983', 5, 3, '2026-09-12 04:39:21.116983', 'QT-2026-0005', 'Báo giá thực hiện: Đèn hành lang không sáng Phòng 403', 'APPROVED'),
(7, 662593.00, 1037807.00, 150000.00, '2026-07-17', 1850400.00, NULL, 4, '2026-09-12 04:39:21.12598', 6, 3, '2026-09-12 04:39:21.12598', 'QT-2026-0006', 'Báo giá thực hiện: Đèn hành lang không sáng Phòng 403', 'REJECTED'),
(6, 700398.00, 1136744.00, 100000.00, '2026-07-18', 1937142.00, '2026-09-12 04:39:21.145008', 4, '2026-09-12 04:39:21.145008', 7, 4, '2026-09-12 04:39:21.145008', 'QT-2026-0007', 'Báo giá thực hiện: Rò rỉ nước tại nhà vệ sinh Phòng 504', 'APPROVED'),
(6, 1127696.00, 823747.00, 150000.00, '2026-07-18', 2101443.00, NULL, 5, '2026-09-12 04:39:21.154083', 8, 4, '2026-09-12 04:39:21.154083', 'QT-2026-0008', 'Báo giá thực hiện: Rò rỉ nước tại nhà vệ sinh Phòng 504', 'REJECTED'),
(6, 786759.00, 1113295.00, 100000.00, '2026-07-19', 2000054.00, '2026-09-12 04:39:21.192321', 5, '2026-09-12 04:39:21.192321', 9, 5, '2026-09-12 04:39:21.192321', 'QT-2026-0009', 'Báo giá thực hiện: Vòi nước bị hỏng Phòng 105', 'APPROVED'),
(4, 893976.00, 948229.00, 150000.00, '2026-07-19', 1992205.00, NULL, 6, '2026-09-12 04:39:21.212324', 10, 5, '2026-09-12 04:39:21.212324', 'QT-2026-0010', 'Báo giá thực hiện: Vòi nước bị hỏng Phòng 105', 'REJECTED'),
(3, 829870.00, 773328.00, 100000.00, '2026-07-20', 1703198.00, '2026-09-12 04:39:21.241332', 6, '2026-09-12 04:39:21.241332', 11, 6, '2026-09-12 04:39:21.241332', 'QT-2026-0011', 'Báo giá thực hiện: Bồn cầu bị tắc nghẽn Nhà trọ Mỹ Đình', 'APPROVED'),
(6, 660029.00, 1377532.00, 150000.00, '2026-07-20', 2187561.00, NULL, 7, '2026-09-12 04:39:21.261841', 12, 6, '2026-09-12 04:39:21.261841', 'QT-2026-0012', 'Báo giá thực hiện: Bồn cầu bị tắc nghẽn Nhà trọ Mỹ Đình', 'REJECTED'),
(3, 762709.00, 741911.00, 100000.00, '2026-07-21', 1604620.00, '2026-09-12 04:39:21.291839', 7, '2026-09-12 04:39:21.292883', 13, 7, '2026-09-12 04:39:21.292883', 'QT-2026-0013', 'Báo giá thực hiện: Điều hòa phòng không làm lạnh Phòng 201', 'APPROVED');
INSERT INTO `quotations` VALUES
(4, 902522.00, 1845275.00, 150000.00, '2026-07-21', 2897797.00, NULL, 8, '2026-09-12 04:39:21.30384', 14, 7, '2026-09-12 04:39:21.30384', 'QT-2026-0014', 'Báo giá thực hiện: Điều hòa phòng không làm lạnh Phòng 201', 'REJECTED'),
(3, 607597.00, 908104.00, 100000.00, '2026-07-22', 1615701.00, '2026-09-12 04:39:21.329848', 8, '2026-09-12 04:39:21.330838', 15, 8, '2026-09-12 04:39:21.330838', 'QT-2026-0015', 'Báo giá thực hiện: Điều hòa bị chảy nước Phòng 201', 'APPROVED'),
(4, 562670.00, 1794031.00, 150000.00, '2026-07-22', 2506701.00, NULL, 9, '2026-09-12 04:39:21.351839', 16, 8, '2026-09-12 04:39:21.351839', 'QT-2026-0016', 'Báo giá thực hiện: Điều hòa bị chảy nước Phòng 201', 'REJECTED'),
(3, 930606.00, 1157795.00, 100000.00, '2026-07-23', 2188401.00, '2026-09-12 04:39:21.389882', 9, '2026-09-12 04:39:21.389882', 17, 9, '2026-09-12 04:39:21.389882', 'QT-2026-0017', 'Báo giá thực hiện: Điều hòa có tiếng ồn lạ Phòng 403', 'APPROVED'),
(6, 561228.00, 1630246.00, 150000.00, '2026-07-23', 2341474.00, NULL, 10, '2026-09-12 04:39:21.422512', 18, 9, '2026-09-12 04:39:21.422512', 'QT-2026-0018', 'Báo giá thực hiện: Điều hòa có tiếng ồn lạ Phòng 403', 'REJECTED'),
(3, 422652.00, 735323.00, 100000.00, '2026-07-24', 1257975.00, '2026-09-12 04:39:21.461514', 10, '2026-09-12 04:39:21.461514', 19, 10, '2026-09-12 04:39:21.461514', 'QT-2026-0019', 'Báo giá thực hiện: Thang máy kêu to bất thường Phòng 204', 'APPROVED'),
(6, 967923.00, 1882322.00, 150000.00, '2026-07-24', 3000245.00, NULL, 1, '2026-09-12 04:39:21.483301', 20, 10, '2026-09-12 04:39:21.483301', 'QT-2026-0020', 'Báo giá thực hiện: Thang máy kêu to bất thường Phòng 204', 'REJECTED'),
(5, 742352.00, 1349402.00, 100000.00, '2026-07-25', 2191754.00, '2026-09-12 04:39:21.519303', 1, '2026-09-12 04:39:21.519303', 21, 11, '2026-09-12 04:39:21.519303', 'QT-2026-0021', 'Báo giá thực hiện: Thang máy dừng đột ngột Căn hộ cho thuê Hà Đông', 'APPROVED'),
(5, 817932.00, 1726238.00, 150000.00, '2026-07-25', 2694170.00, NULL, 2, '2026-09-12 04:39:21.542308', 22, 11, '2026-09-12 04:39:21.542308', 'QT-2026-0022', 'Báo giá thực hiện: Thang máy dừng đột ngột Căn hộ cho thuê Hà Đông', 'REJECTED'),
(5, 490525.00, 1059168.00, 100000.00, '2026-07-26', 1649693.00, '2026-09-12 04:39:21.575302', 2, '2026-09-12 04:39:21.576314', 23, 12, '2026-09-12 04:39:21.576314', 'QT-2026-0023', 'Báo giá thực hiện: Bình chữa cháy hết hạn Phòng 605', 'APPROVED'),
(6, 706433.00, 1836627.00, 150000.00, '2026-07-26', 2693060.00, NULL, 3, '2026-09-12 04:39:21.649155', 24, 12, '2026-09-12 04:39:21.649155', 'QT-2026-0024', 'Báo giá thực hiện: Bình chữa cháy hết hạn Phòng 605', 'REJECTED'),
(5, 856721.00, 635027.00, 100000.00, '2026-07-27', 1591748.00, '2026-09-12 04:39:21.811141', 3, '2026-09-12 04:39:21.811141', 25, 13, '2026-09-12 04:39:21.811141', 'QT-2026-0025', 'Báo giá thực hiện: Chuông báo cháy kêu liên tục Phòng 201', 'APPROVED'),
(6, 1014803.00, 1049904.00, 150000.00, '2026-07-27', 2214707.00, NULL, 4, '2026-09-12 04:39:21.830141', 26, 13, '2026-09-12 04:39:21.830141', 'QT-2026-0026', 'Báo giá thực hiện: Chuông báo cháy kêu liên tục Phòng 201', 'REJECTED');
INSERT INTO `quotations` VALUES
(4, 716447.00, 1077093.00, 100000.00, '2026-07-28', 1893540.00, '2026-09-12 04:39:21.880174', 4, '2026-09-12 04:39:21.880174', 27, 14, '2026-09-12 04:39:21.880174', 'QT-2026-0027', 'Báo giá thực hiện: Camera an ninh mất tín hiệu Phòng 302', 'APPROVED'),
(4, 728035.00, 1001630.00, 150000.00, '2026-07-28', 1879665.00, NULL, 5, '2026-09-12 04:39:21.89417', 28, 14, '2026-09-12 04:39:21.89417', 'QT-2026-0028', 'Báo giá thực hiện: Camera an ninh mất tín hiệu Phòng 302', 'REJECTED'),
(6, 745481.00, 1140755.00, 100000.00, '2026-07-29', 1986236.00, '2026-09-12 04:39:21.943729', 5, '2026-09-12 04:39:21.943729', 29, 15, '2026-09-12 04:39:21.943729', 'QT-2026-0029', 'Báo giá thực hiện: Camera hành lang bị mờ Phòng 403', 'APPROVED'),
(5, 491151.00, 1272600.00, 150000.00, '2026-07-29', 1913751.00, NULL, 6, '2026-09-12 04:39:21.95773', 30, 15, '2026-09-12 04:39:21.95773', 'QT-2026-0030', 'Báo giá thực hiện: Camera hành lang bị mờ Phòng 403', 'REJECTED'),
(3, 792284.00, 629441.00, 80000.00, '2026-07-30', 1501725.00, NULL, 6, '2026-09-12 04:39:22.212547', 31, 16, '2026-09-12 04:39:22.212547', 'QT-2026-0031', 'Báo giá thực hiện: Cửa gỗ bị mối mọt Chung cư Botanica Cầu Giấy', 'PENDING'),
(5, 580610.00, 1292740.00, 80000.00, '2026-07-31', 1953350.00, NULL, 7, '2026-09-12 04:39:22.263416', 32, 17, '2026-09-12 04:39:22.263416', 'QT-2026-0032', 'Báo giá thực hiện: Bản lề cửa bị lỏng Phòng 105', 'PENDING'),
(3, 389030.00, 1228070.00, 80000.00, '2026-08-01', 1697100.00, NULL, 8, '2026-09-12 04:39:22.277', 33, 18, '2026-09-12 04:39:22.277', 'QT-2026-0033', 'Báo giá thực hiện: Tường bị nứt Phòng 106', 'PENDING'),
(3, 536291.00, 959029.00, 80000.00, '2026-08-02', 1575320.00, NULL, 9, '2026-09-12 04:39:22.303976', 34, 19, '2026-09-12 04:39:22.303976', 'QT-2026-0034', 'Báo giá thực hiện: Trần nhà bị thấm nước Phòng 201', 'PENDING'),
(2, 378741.00, 1334434.00, 80000.00, '2026-08-03', 1793175.00, NULL, 10, '2026-09-12 04:39:22.317977', 35, 20, '2026-09-12 04:39:22.317977', 'QT-2026-0035', 'Báo giá thực hiện: Máy bơm nước bị hỏng Phòng 706', 'PENDING'),
(3, 641881.00, 507006.00, 80000.00, '2026-08-04', 1228887.00, NULL, 1, '2026-09-12 04:39:22.337041', 36, 21, '2026-09-12 04:39:22.337041', 'QT-2026-0036', 'Báo giá thực hiện: Quạt thông gió không hoạt động Chung cư Mini Cầu Giấy', 'PENDING'),
(5, 510929.00, 596996.00, 80000.00, '2026-08-05', 1187925.00, NULL, 2, '2026-09-12 04:39:22.349037', 37, 22, '2026-09-12 04:39:22.349037', 'QT-2026-0037', 'Báo giá thực hiện: Khu vực chung không được dọn dẹp Phòng 204', 'PENDING'),
(3, 603589.00, 1374046.00, 80000.00, '2026-08-06', 2057635.00, NULL, 3, '2026-09-12 04:39:22.367116', 38, 23, '2026-09-12 04:39:22.367116', 'QT-2026-0038', 'Báo giá thực hiện: Thùng rác đầy tràn Phòng 205', 'PENDING'),
(3, 476774.00, 642999.00, 80000.00, '2026-08-07', 1199773.00, NULL, 4, '2026-09-12 04:39:22.380282', 39, 24, '2026-09-12 04:39:22.380282', 'QT-2026-0039', 'Báo giá thực hiện: Bảng chỉ dẫn bị hỏng Phòng 403', 'PENDING'),
(2, 431745.00, 504536.00, 80000.00, '2026-08-08', 1016281.00, NULL, 5, '2026-09-12 04:39:22.395273', 40, 25, '2026-09-12 04:39:22.395273', 'QT-2026-0040', 'Báo giá thực hiện: Mất điện toàn bộ khu vực Phòng 201', 'PENDING');
CREATE TABLE `units`(
    `area` DECIMAL(12, 2) NOT NULL,
    `floor_number` INTEGER NOT NULL,
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `property_id` BIGINT NOT NULL,
    `code` VARCHAR(30) NOT NULL,
    `name` VARCHAR(150) NOT NULL,
    `description` VARCHAR(1000),
    `status` ENUM('OCCUPIED', 'UNAVAILABLE', 'UNDER_MAINTENANCE', 'VACANT') NOT NULL,
    `unit_type` ENUM('APARTMENT', 'COMMON_AREA', 'OTHER', 'PARKING', 'ROOM', 'SHOP', 'TECHNICAL_ROOM') NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
INSERT INTO `units` VALUES
(42.00, 2, 1, 1, 'P0201', 'Phòng 201', NULL, 'OCCUPIED', 'APARTMENT'),
(57.00, 3, 2, 1, 'P0302', 'Phòng 302', NULL, 'OCCUPIED', 'ROOM'),
(46.00, 4, 3, 1, 'P0403', 'Phòng 403', NULL, 'OCCUPIED', 'APARTMENT'),
(32.00, 5, 4, 1, 'P0504', 'Phòng 504', NULL, 'VACANT', 'ROOM'),
(26.00, 1, 5, 1, 'P0105', 'Phòng 105', NULL, 'OCCUPIED', 'APARTMENT'),
(42.00, 2, 6, 1, 'P0206', 'Phòng 206', NULL, 'OCCUPIED', 'ROOM'),
(46.00, 2, 7, 2, 'P0201', 'Phòng 201', NULL, 'OCCUPIED', 'APARTMENT'),
(30.00, 3, 8, 2, 'P0302', 'Phòng 302', NULL, 'OCCUPIED', 'ROOM'),
(53.00, 1, 9, 2, 'P0103', 'Phòng 103', NULL, 'OCCUPIED', 'APARTMENT'),
(34.00, 2, 10, 2, 'P0204', 'Phòng 204', NULL, 'VACANT', 'ROOM'),
(30.00, 3, 11, 2, 'P0305', 'Phòng 305', NULL, 'OCCUPIED', 'APARTMENT'),
(33.00, 1, 12, 2, 'P0106', 'Phòng 106', NULL, 'OCCUPIED', 'ROOM'),
(26.00, 2, 13, 3, 'P0201', 'Phòng 201', NULL, 'OCCUPIED', 'APARTMENT'),
(48.00, 3, 14, 3, 'P0302', 'Phòng 302', NULL, 'OCCUPIED', 'ROOM'),
(53.00, 4, 15, 3, 'P0403', 'Phòng 403', NULL, 'OCCUPIED', 'APARTMENT'),
(41.00, 1, 16, 3, 'P0104', 'Phòng 104', NULL, 'VACANT', 'ROOM'),
(25.00, 2, 17, 3, 'P0205', 'Phòng 205', NULL, 'OCCUPIED', 'APARTMENT'),
(28.00, 3, 18, 3, 'P0306', 'Phòng 306', NULL, 'OCCUPIED', 'ROOM'),
(57.00, 2, 19, 4, 'P0201', 'Phòng 201', NULL, 'OCCUPIED', 'APARTMENT'),
(51.00, 3, 20, 4, 'P0302', 'Phòng 302', NULL, 'OCCUPIED', 'ROOM'),
(30.00, 4, 21, 4, 'P0403', 'Phòng 403', NULL, 'OCCUPIED', 'APARTMENT'),
(31.00, 5, 22, 4, 'P0504', 'Phòng 504', NULL, 'VACANT', 'ROOM'),
(55.00, 6, 23, 4, 'P0605', 'Phòng 605', NULL, 'OCCUPIED', 'APARTMENT'),
(35.00, 7, 24, 4, 'P0706', 'Phòng 706', NULL, 'OCCUPIED', 'ROOM'),
(30.00, 8, 25, 4, 'EXT25', 'Phòng EXT25', NULL, 'VACANT', 'ROOM');
CREATE TABLE `users`(
    `active` BOOLEAN NOT NULL,
    `created_at` TIMESTAMP(6) NOT NULL,
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `updated_at` TIMESTAMP(6),
    `username` VARCHAR(50) NOT NULL,
    `full_name` VARCHAR(100) NOT NULL,
    `password` VARCHAR(100) NOT NULL,
    `email` VARCHAR(120),
    `role` ENUM('ADMIN', 'MANAGER', 'STAFF') NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
INSERT INTO `users` VALUES
(TRUE, '2026-09-12 04:39:19.387314', 1, '2026-09-12 04:39:19.387314', 'admin', 'Quản trị viên hệ thống', '$2a$10$B2Ydl3s6PhT9nHpdmX003OGXELA2y0lpo.lAivA1bzgnijrbQyF5i', 'admin@cnj49.local', 'ADMIN'),
(TRUE, '2026-09-12 04:39:19.737834', 2, '2026-09-12 04:39:19.737834', 'manager', 'Trần Quản Lý', '$2a$10$4U7INQBDmY/IVOxxL01ududT.z51j.V.Mh7QQGELKq5ZUYVATYsXu', 'manager@cnj49.local', 'MANAGER'),
(TRUE, '2026-09-12 04:39:19.899345', 3, '2026-09-12 04:39:19.899345', 'staff', 'Nguyễn Nhân Viên', '$2a$10$sv0eQsFH6BeuY5VONfMzY.6YF.9G5ZDUete/fODngU9nVKxAmc0FK', 'staff@cnj49.local', 'STAFF');
CREATE TABLE `work_orders`(
    `actual_completion_date` DATE,
    `expected_completion_date` DATE NOT NULL,
    `start_date` DATE NOT NULL,
    `warranty_until` DATE,
    `contractor_id` BIGINT NOT NULL,
    `created_at` TIMESTAMP(6) NOT NULL,
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `maintenance_request_id` BIGINT NOT NULL,
    `quotation_id` BIGINT NOT NULL,
    `updated_at` TIMESTAMP(6),
    `work_order_code` VARCHAR(30) NOT NULL,
    `result` VARCHAR(2000),
    `work_description` VARCHAR(2000) NOT NULL,
    `status` ENUM('ACCEPTED', 'CANCELLED', 'COMPLETED', 'IN_PROGRESS', 'NOT_STARTED', 'PAUSED') NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
INSERT INTO `work_orders` VALUES
('2026-07-19', '2026-07-19', '2026-07-16', NULL, 1, '2026-09-12 04:39:21.061487', 1, 1, 1, '2026-09-12 04:39:22.797937', 'WO-2026-0001', 'Đã hoàn thành xử lý theo yêu cầu.', 'Thực hiện xử lý: Mất điện toàn bộ khu vực Chung cư Mini Cầu Giấy', 'ACCEPTED'),
('2026-07-22', '2026-07-22', '2026-07-17', NULL, 2, '2026-09-12 04:39:21.107744', 2, 2, 3, '2026-09-12 04:39:22.797937', 'WO-2026-0002', 'Đã hoàn thành xử lý theo yêu cầu.', 'Thực hiện xử lý: Ổ cắm điện bị cháy Phòng 302', 'ACCEPTED'),
('2026-07-21', '2026-07-21', '2026-07-18', NULL, 3, '2026-09-12 04:39:21.138013', 3, 3, 5, '2026-09-12 04:39:22.797937', 'WO-2026-0003', 'Đã hoàn thành xử lý theo yêu cầu.', 'Thực hiện xử lý: Đèn hành lang không sáng Phòng 403', 'ACCEPTED'),
('2026-07-25', '2026-07-25', '2026-07-19', NULL, 4, '2026-09-12 04:39:21.172102', 4, 4, 7, '2026-09-12 04:39:22.797937', 'WO-2026-0004', 'Đã hoàn thành xử lý theo yêu cầu.', 'Thực hiện xử lý: Rò rỉ nước tại nhà vệ sinh Phòng 504', 'ACCEPTED'),
('2026-07-26', '2026-07-26', '2026-07-20', NULL, 5, '2026-09-12 04:39:21.233323', 5, 5, 9, '2026-09-12 04:39:22.797937', 'WO-2026-0005', 'Đã hoàn thành xử lý theo yêu cầu.', 'Thực hiện xử lý: Vòi nước bị hỏng Phòng 105', 'ACCEPTED'),
('2026-07-24', '2026-07-24', '2026-07-21', NULL, 6, '2026-09-12 04:39:21.274832', 6, 6, 11, '2026-09-12 04:39:22.797937', 'WO-2026-0006', 'Đã hoàn thành xử lý theo yêu cầu.', 'Thực hiện xử lý: Bồn cầu bị tắc nghẽn Nhà trọ Mỹ Đình', 'ACCEPTED'),
('2026-07-25', '2026-07-25', '2026-07-22', NULL, 7, '2026-09-12 04:39:21.316843', 7, 7, 13, '2026-09-12 04:39:22.797937', 'WO-2026-0007', 'Đã hoàn thành xử lý theo yêu cầu.', 'Thực hiện xử lý: Điều hòa phòng không làm lạnh Phòng 201', 'ACCEPTED'),
('2026-07-26', '2026-07-26', '2026-07-23', NULL, 8, '2026-09-12 04:39:21.366834', 8, 8, 15, '2026-09-12 04:39:22.797937', 'WO-2026-0008', 'Đã hoàn thành xử lý theo yêu cầu.', 'Thực hiện xử lý: Điều hòa bị chảy nước Phòng 201', 'ACCEPTED'),
(NULL, '2026-07-27', '2026-07-24', NULL, 9, '2026-09-12 04:39:21.450503', 9, 9, 17, '2026-09-12 04:39:22.797937', 'WO-2026-0009', 'Đã hoàn thành xử lý theo yêu cầu.', 'Thực hiện xử lý: Điều hòa có tiếng ồn lạ Phòng 403', 'IN_PROGRESS'),
(NULL, '2026-07-28', '2026-07-25', NULL, 10, '2026-09-12 04:39:21.5083', 10, 10, 19, '2026-09-12 04:39:22.797937', 'WO-2026-0010', 'Đã hoàn thành xử lý theo yêu cầu.', 'Thực hiện xử lý: Thang máy kêu to bất thường Phòng 204', 'IN_PROGRESS'),
('2026-07-31', '2026-07-31', '2026-07-26', NULL, 1, '2026-09-12 04:39:21.5543', 11, 11, 21, '2026-09-12 04:39:22.797937', 'WO-2026-0011', 'Đã hoàn thành xử lý theo yêu cầu.', 'Thực hiện xử lý: Thang máy dừng đột ngột Căn hộ cho thuê Hà Đông', 'COMPLETED'),
(NULL, '2026-08-01', '2026-07-27', NULL, 2, '2026-09-12 04:39:21.789221', 12, 12, 23, '2026-09-12 04:39:22.797937', 'WO-2026-0012', NULL, 'Thực hiện xử lý: Bình chữa cháy hết hạn Phòng 605', 'IN_PROGRESS');
INSERT INTO `work_orders` VALUES
(NULL, '2026-08-02', '2026-07-28', NULL, 3, '2026-09-12 04:39:21.844144', 13, 13, 25, '2026-09-12 04:39:21.844144', 'WO-2026-0013', NULL, 'Thực hiện xử lý: Chuông báo cháy kêu liên tục Phòng 201', 'NOT_STARTED'),
(NULL, '2026-08-02', '2026-07-29', NULL, 4, '2026-09-12 04:39:21.927106', 14, 14, 27, '2026-09-12 04:39:21.927106', 'WO-2026-0014', NULL, 'Thực hiện xử lý: Camera an ninh mất tín hiệu Phòng 302', 'NOT_STARTED'),
(NULL, '2026-08-05', '2026-07-30', NULL, 5, '2026-09-12 04:39:21.971371', 15, 15, 29, '2026-09-12 04:39:22.797937', 'WO-2026-0015', NULL, 'Thực hiện xử lý: Camera hành lang bị mờ Phòng 403', 'IN_PROGRESS');
ALTER TABLE `units` ADD CONSTRAINT `CONSTRAINT_6A` CHECK(`floor_number` >= 0);
ALTER TABLE `quotations` ADD CONSTRAINT `CONSTRAINT_4` CHECK(`estimated_days` >= 1);
ALTER TABLE `properties` ADD CONSTRAINT `CONSTRAINT_C8` CHECK(`number_of_units` >= 0);
ALTER TABLE `properties` ADD CONSTRAINT `CONSTRAINT_C` CHECK(`number_of_floors` >= 0);
ALTER TABLE `expenses` ADD CONSTRAINT `CONSTRAINT_8C` UNIQUE(`expense_code`);
ALTER TABLE `maintenance_categories` ADD CONSTRAINT `CONSTRAINT_2` UNIQUE(`name`);
ALTER TABLE `quotations` ADD CONSTRAINT `CONSTRAINT_46` UNIQUE(`quotation_code`);
ALTER TABLE `contractors` ADD CONSTRAINT `CONSTRAINT_8` UNIQUE(`contractor_code`);
ALTER TABLE `properties` ADD CONSTRAINT `CONSTRAINT_C8C` UNIQUE(`code`);
ALTER TABLE `maintenance_requests` ADD CONSTRAINT `CONSTRAINT_1` UNIQUE(`request_code`);
ALTER TABLE `users` ADD CONSTRAINT `CONSTRAINT_6A6` UNIQUE(`username`);
ALTER TABLE `units` ADD CONSTRAINT `uk_unit_property_code` UNIQUE(`property_id`, `code`);
ALTER TABLE `work_orders` ADD CONSTRAINT `CONSTRAINT_F` UNIQUE(`work_order_code`);
ALTER TABLE `expenses` ADD CONSTRAINT `fk_expense_contractor` FOREIGN KEY(`contractor_id`) REFERENCES `contractors`(`id`);
ALTER TABLE `quotations` ADD CONSTRAINT `fk_quotation_request` FOREIGN KEY(`maintenance_request_id`) REFERENCES `maintenance_requests`(`id`);
ALTER TABLE `inspections` ADD CONSTRAINT `fk_inspection_work_order` FOREIGN KEY(`work_order_id`) REFERENCES `work_orders`(`id`);
ALTER TABLE `expenses` ADD CONSTRAINT `fk_expense_property` FOREIGN KEY(`property_id`) REFERENCES `properties`(`id`);
ALTER TABLE `work_orders` ADD CONSTRAINT `fk_work_order_request` FOREIGN KEY(`maintenance_request_id`) REFERENCES `maintenance_requests`(`id`);
ALTER TABLE `work_orders` ADD CONSTRAINT `fk_work_order_quotation` FOREIGN KEY(`quotation_id`) REFERENCES `quotations`(`id`);
ALTER TABLE `expenses` ADD CONSTRAINT `fk_expense_work_order` FOREIGN KEY(`work_order_id`) REFERENCES `work_orders`(`id`);
ALTER TABLE `maintenance_requests` ADD CONSTRAINT `fk_request_category` FOREIGN KEY(`category_id`) REFERENCES `maintenance_categories`(`id`);
ALTER TABLE `work_orders` ADD CONSTRAINT `fk_work_order_contractor` FOREIGN KEY(`contractor_id`) REFERENCES `contractors`(`id`);
ALTER TABLE `maintenance_requests` ADD CONSTRAINT `fk_request_unit` FOREIGN KEY(`unit_id`) REFERENCES `units`(`id`);
ALTER TABLE `units` ADD CONSTRAINT `fk_unit_property` FOREIGN KEY(`property_id`) REFERENCES `properties`(`id`);
ALTER TABLE `maintenance_requests` ADD CONSTRAINT `fk_request_property` FOREIGN KEY(`property_id`) REFERENCES `properties`(`id`);
ALTER TABLE `quotations` ADD CONSTRAINT `fk_quotation_contractor` FOREIGN KEY(`contractor_id`) REFERENCES `contractors`(`id`);

SET FOREIGN_KEY_CHECKS = 1;
