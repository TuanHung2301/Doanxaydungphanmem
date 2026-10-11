---
name: graphify
description: Phân tích và trực quan hóa cây phụ thuộc (Call Graph & Dependency Graph) giữa các tầng MVC trong dự án Java Swing CSKHDEMOO.
---

# Graphify Skill - Dependency & Call Graph Analyzer

Skill này giúp AI Agent nắm vững luồng gọi hàm và cấu trúc phụ thuộc kiến trúc trong hệ thống CSKH:

## 1. Sơ đồ phụ thuộc chuẩn (Architectural Call Flow)
- View Layer (src/view) -> Controller Layer (src/controller)
- Controller Layer -> Service Layer (src/service)
- Service Layer -> DAO Layer (src/dao)
- DAO Layer -> DBConnection (src/DB) -> MySQL Database

## 2. Mục đích kiểm soát mã nguồn
1. **Phân tích tác động (Impact Analysis):** Khi sửa đổi cấu trúc bảng hoặc phương thức ở DAO/Model, Agent định vị ngay các lớp Service, Controller và View bị ảnh hưởng gián tiếp để cập nhật đồng bộ.
2. **Chống phụ thuộc vòng (Prevent Circular Dependency):** Đảm bảo Controller không gọi ngược View một cách luẩn quẩn, View tuyệt đối không import DAO.
3. **Tối ưu Context:** Giúp Agent chỉ nạp các file liên quan trực tiếp đến tính năng cần làm thay vì đọc toàn bộ dự án.
