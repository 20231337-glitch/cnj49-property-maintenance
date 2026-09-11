# Use Case - CNJ49 Property Maintenance

## Actor

- **ADMIN**: toan quyen he thong.
- **MANAGER**: quan ly BDS, bao tri, nha thau, bao gia, nghiem thu, chi phi, bao cao.
- **STAFF**: ghi nhan su co, tao yeu cau bao tri, cap nhat thong tin, theo doi tien do.

## Danh sach Use Case chinh

1. Dang nhap / Dang xuat
2. Quan ly bat dong san (CRUD, tim kiem, loc, phan trang)
3. Quan ly can/phong (CRUD, loc theo BDS/tang/trang thai)
4. Quan ly hang muc bao tri (CRUD)
5. Tao yeu cau bao tri (STAFF/MANAGER/ADMIN)
6. Cap nhat / Huy / Dong yeu cau bao tri
7. Them bao gia cho yeu cau bao tri (theo nha thau)
8. So sanh bao gia
9. Duyet / Tu choi bao gia (MANAGER/ADMIN)
10. Tao phieu cong viec tu bao gia da duyet
11. Bat dau / Tam dung / Tiep tuc / Hoan thanh / Huy phieu cong viec
12. Nghiem thu phieu cong viec (PASSED/FAILED)
13. Ghi nhan chi phi van hanh (thu cong hoac tu dong khi nghiem thu dat)
14. Xem Dashboard thong ke
15. Xem 7 bao cao co bo loc

## Kich ban chinh (Use Case trung tam): Xu ly yeu cau bao tri tu dau den cuoi

1. STAFF phat hien su co, tao Yeu cau bao tri (trang thai NEW).
2. MANAGER xem xet, chuyen trang thai sang UNDER_REVIEW roi WAITING_QUOTATION.
3. Nhieu nha thau gui bao gia (Quotation PENDING).
4. MANAGER so sanh bang bao gia, duyet mot bao gia (APPROVED); cac bao gia con lai tu dong
   chuyen REJECTED (BR03); yeu cau chuyen QUOTATION_APPROVED (BR02).
5. He thong tao Phieu cong viec (WorkOrder) tu bao gia da duyet, nha thau lay dung tu bao gia (BR09).
6. Nha thau thuc hien: WorkOrder NOT_STARTED -> IN_PROGRESS -> COMPLETED; yeu cau dong bo
   IN_PROGRESS -> WAITING_INSPECTION (BR10, BR11).
7. Nguoi nghiem thu kiem tra ket qua:
   - Neu PASSED: WorkOrder ACCEPTED, yeu cau COMPLETED, chi phi thuc te duoc tu dong ghi nhan (BR12).
   - Neu FAILED: WorkOrder va yeu cau quay lai IN_PROGRESS de lam lai (BR13).
8. Sau khi COMPLETED, MANAGER dong yeu cau (CLOSED) (BR05).
9. Chi phi va bao cao duoc cap nhat theo thoi gian thuc tren Dashboard va trang Bao cao.
