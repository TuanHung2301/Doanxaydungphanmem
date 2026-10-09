🚀 HỆ THỐNG CHĂM SÓC VÀ XỬ LÝ YÊU CẦU KHÁCH HÀNG

Phần mềm hỗ trợ tiếp nhận, quản lý và phân loại phiếu yêu cầu hỗ trợ khách hàng, xây dựng theo kiến trúc MVC nhiều tầng với giao diện Java Swing và cơ sở dữ liệu MySQL.

📌 1. BÁO CÁO TIẾN ĐỘ ĐỒ ÁN

🔹 Báo cáo tuần 1 (04/10/2026): Xem chi tiết tại README_4_11.md

🔹 Báo cáo tuần 2 (11/10/2026): Sẽ cập nhật tại README_11_11.md

🛠 2. CÔNG NGHỆ VÀ KỸ THUẬT SỬ DỤNG

🔹 Ngôn ngữ: Java 17

🔹 Giao diện: Java Swing

🔹 Kiến trúc: Model - View - Controller (MVC)

🔹 Cơ sở dữ liệu: MySQL 8.x

🔹 Kết nối: JDBC thuần, PreparedStatement, Singleton Pattern

🔹 Môi trường: IntelliJ IDEA, Git

📂 3. CẤU TRÚC THƯ MỤC MÃ NGUỒN (src/)

📁 controller: Điều hướng luồng nghiệp vụ, gắn kết nối sự kiện giao diện

📁 dao: Tầng truy xuất dữ liệu MySQL (UserDAO, TicketDAO, ServiceDAO...)

📁 DB: Quản lý phiên đăng nhập (Session) và kết nối duy nhất (DBConnection)

📁 model: Định nghĩa các thực thể dữ liệu (User, Ticket, Service)

📁 service: Xử lý quy tắc nghiệp vụ, kiểm tra ràng buộc dữ liệu

📁 utils: Tiện ích bổ trợ (định dạng ngày tháng, tiền tệ, màu sắc)

📁 view: Giao diện người dùng Swing (Màn hình chính, Đăng nhập, Hộp thoại)

📁 main: Điểm khởi chạy ứng dụng (Main.java)

⚙️ 4. HƯỚNG DẪN CÀI ĐẶT VÀ KHỞI CHẠY

1. Chuẩn bị môi trường:

🔹 Cài đặt JDK 17 trở lên

🔹 Cài đặt MySQL Server hoặc XAMPP

🔹 Mở dự án bằng IntelliJ IDEA

2. Thiết lập cơ sở dữ liệu:

🔹 Mở MySQL tạo mới cơ sở dữ liệu

🔹 Nhập dữ liệu từ file script .sql của dự án

🔹 Mở src/DB/DBConnection.java chỉnh lại tài khoản và mật khẩu MySQL

3. Khởi chạy:

🔹 Đảm bảo đã thêm thư viện MySQL Connector vào dự án

🔹 Chạy file src/main/Main.java để mở giao diện

👤 5. THÔNG TIN SINH VIÊN THỰC HIỆN

🔹 Họ và tên: Nguyễn Tuấn Hưng

🔹 Lớp: 74DCTT27

🔹 Học phần: Đồ án Xây dựng và Phát triển Phần mềm
