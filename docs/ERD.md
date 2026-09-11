# ERD - CNJ49 Property Maintenance

```
PROPERTY (1) ----< (N) UNIT
PROPERTY (1) ----< (N) MAINTENANCE_REQUEST
PROPERTY (1) ----< (N) EXPENSE

MAINTENANCE_CATEGORY (1) ----< (N) MAINTENANCE_REQUEST
UNIT (1) ----< (N) MAINTENANCE_REQUEST   [tuy chon, co the null]

MAINTENANCE_REQUEST (1) ----< (N) QUOTATION >---- (N) CONTRACTOR
MAINTENANCE_REQUEST (1) ----< (N) WORK_ORDER >---- (N) CONTRACTOR
QUOTATION (1) ---- (1) WORK_ORDER   [work_order.quotation_id, chi tao khi quotation APPROVED]

WORK_ORDER (1) ----< (N) INSPECTION
WORK_ORDER (1) ----< (N) EXPENSE
CONTRACTOR (1) ----< (N) EXPENSE   [tuy chon]

USER (khong tham chieu truc tiep du lieu nghiep vu, dung cho dang nhap/audit_logs.username)
AUDIT_LOG: ghi nhat ky theo (entity_type, entity_id)
```

## Bang du lieu chinh

| Bang | Khoa chinh | Khoa ngoai |
|---|---|---|
| properties | id | - |
| units | id | property_id -> properties.id |
| maintenance_categories | id | - |
| maintenance_requests | id | property_id, unit_id (null), category_id |
| contractors | id | - |
| quotations | id | maintenance_request_id, contractor_id |
| work_orders | id | maintenance_request_id, quotation_id, contractor_id |
| inspections | id | work_order_id |
| expenses | id | property_id, work_order_id (null), contractor_id (null) |
| users | id | - |
| audit_logs | id | - (luu entity_type + entity_id dang tu do) |

## Ghi chu thiet ke

- Khong dung `CascadeType.ALL` bua bai: xoa Property/Contractor bi chan boi service (BR14/BR15)
  neu con du lieu lien quan, tranh loi khoa ngoai va xoa nham du lieu lich su.
- `Quotation.totalAmount` va `WorkOrder`/`Request` trang thai duoc dong bo tu Service, khong
  dua vao trigger database, giup logic de kiem thu va giai thich khi bao ve do an.
