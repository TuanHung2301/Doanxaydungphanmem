---
name: mvc-rules
description: Quy chuẩn kiến trúc đa tầng MVC, an toàn truy vấn SQL và chuẩn mã hóa cho dự án CSKHDEMOO.
---

# MVC Rules & Security Enforcement Skill

## 1. Phân định trách nhiệm đơn lẻ (Single Responsibility Principle)
- **View Layer (`src/view`):** Chỉ xây dựng thành phần giao diện Swing (JFrame, JPanel, JTable...), tiếp nhận tương tác người dùng và hiển thị thông báo. Tuyệt đối không chứa logic nghiệp vụ và không gọi trực tiếp tầng DAO.
- **Controller Layer (`src/controller`):** Đăng ký ActionListener lắng nghe sự kiện từ View, trích xuất dữ liệu form và chuyển giao cho Service xử lý.
- **Service Layer (`src/service`):** Chịu trách nhiệm kiểm tra hợp lệ dữ liệu (validate), kiểm tra quy tắc bảo mật (mật khẩu >= 8 ký tự, có ký tự đặc biệt), bảo vệ quyền hạn ADMIN và kiểm tra trạng thái ACTIVE của nhân viên.
- **DAO Layer (`src/dao`):** Thao tác CRUD thuần túy với MySQL thông qua JDBC. Không chứa logic giao diện hay kiểm tra nghiệp vụ người dùng.

## 2. An toàn Cơ sở dữ liệu (Database Security)
- Bắt buộc sử dụng `PreparedStatement` với tham số `?` cho mọi câu lệnh SQL có biến đầu vào để triệt tiêu nguy cơ tấn công SQL Injection.
- Bắt buộc sử dụng cú pháp `try-with-resources` để tự động đóng `Connection`, `PreparedStatement` và `ResultSet`, ngăn ngừa rò rỉ bộ nhớ (Resource Leak) và Database Lock.

## 3. Tiêu chuẩn mã hóa và định dạng
- Toàn bộ chuỗi giao diện và thông báo sử dụng tiếng Việt có dấu, lưu trữ mã hóa UTF-8.
- Xử lý dữ liệu đầu vào: Cắt tỉa khoảng trắng ở hai đầu bằng `.trim()` và kiểm tra `.isEmpty()` trước khi xử lý.
