BÁO CÁO TIẾN ĐỘ DỰ ÁN VÀ Ý HIỂU KIẾN TRÚC HỆ THỐNG (NGÀY 04/10/2026)

PHẦN 1: THÔNG TIN CHUNG

1.1. Thông tin cá nhân
- Họ và tên: Nguyễn Tuấn Hưng
- Lớp: 74DCTT27

1.2. Thông tin dự án
- Tên dự án: Hệ thống Chăm sóc và Xử lý Yêu cầu Khách hàng (CSKH KeySoft)
- Công nghệ sử dụng: Java 17, Java Swing, JDBC, MySQL

PHẦN 2: Ý HIỂU VỀ BÀI TOÁN VÀ KIẾN TRÚC PHẦN MỀM (MÔ HÌNH MVC)

Em xây dựng hệ thống theo mô hình kiến trúc MVC nhiều tầng rõ ràng để code không bị chồng chéo, dễ tìm lỗi và thuận tiện khi nâng cấp:

2.1. Tầng Model (gói model)
- Quản lý các đối tượng dữ liệu cốt lõi của bài toán.
- Lớp User: Đại diện cho nhân viên, quản trị viên và thông tin khách hàng.
- Lớp Ticket: Đại diện cho phiếu yêu cầu hỗ trợ, theo dõi trạng thái tiếp nhận và xử lý.
- Lớp Service: Đại diện cho danh mục các gói cước và dịch vụ của doanh nghiệp.

2.2. Tầng Kết nối và Truy xuất Dữ liệu (gói DB và dao)
- Lớp DBConnection: Thiết kế theo mẫu Singleton để duy trì một kết nối MySQL duy nhất trong suốt quá trình chạy, hạn chế tối đa việc tạo kết nối lặp đi lặp lại gây đầy RAM và treo ứng dụng.
- Lớp Session: Lưu trữ đối tượng người dùng hiện tại đang đăng nhập để phục vụ việc kiểm tra quyền hạn khi mở các chức năng.
- Các lớp DAO (UserDAO, TicketDAO, ServiceDAO, StatsDAO): Đóng vai trò cầu nối thực thi các lệnh SQL xuống MySQL bằng PreparedStatement.
- Tối ưu hóa truy vấn: Tìm kiếm và phân trang được xử lý trực tiếp dưới cơ sở dữ liệu thông qua mệnh đề LIMIT và OFFSET, không tải toàn bộ bản ghi lên bộ nhớ để lọc thủ công.

2.3. Tầng Xử lý Nghiệp vụ (gói service)
- Là tầng trung gian kiểm tra tính toàn vẹn của dữ liệu từ người dùng trước khi gọi xuống DAO lưu vào cơ sở dữ liệu.
- Ràng buộc mật khẩu: Bắt buộc độ dài từ 8 ký tự trở lên và phải chứa ít nhất một ký tự đặc biệt như a còng, thăng, chấm than.
- Quy tắc hệ thống: Ngăn chặn tuyệt đối việc xóa mềm hoặc hạ quyền của tài khoản Quản trị viên (ADMIN).
- Chuẩn hóa dữ liệu: Tự động loại bỏ khoảng trắng thừa ở hai đầu chuỗi nhập vào trước khi ghi nhận.

2.4. Tầng Giao diện (gói view)
- Thiết kế bằng thư viện Java Swing theo phong cách phẳng (Flat UI).
- Cấu hình bảng dữ liệu hiển thị: Sử dụng chế độ AUTO_RESIZE_OFF kết hợp chia độ rộng từng cột hợp lý để không bị cắt chữ thành dấu ba chấm khi nội dung yêu cầu quá dài.
- Tách cột trực quan: Tự động bóc tách chuỗi tiêu đề thành hai cột riêng biệt gồm Tên dịch vụ và Nội dung chi tiết để nhân viên dễ theo dõi.

2.5. Tầng Điều khiển (gói controller)
- Đóng vai trò trung tâm tiếp nhận sự kiện từ người dùng thông qua ActionListener và MouseListener.
- Lấy dữ liệu từ màn hình View, chuyển cho tầng Service kiểm tra và nhận kết quả trả về để thông báo cho người dùng.
- Đảm bảo tính độc lập: View và DAO không bao giờ gọi trực tiếp nhau, mọi dữ liệu đều phải đi qua Controller và Service.

PHẦN 3: CÔNG VIỆC ĐÃ THỰC HIỆN VÀ QUÁ TRÌNH ỨNG DỤNG AI HỖ TRỢ

3.1. Kế hoạch triển khai mã nguồn
- Phân chia bài toán theo từng module cụ thể theo thứ tự từ dưới lên: Dựng cơ sở dữ liệu và Model, lập trình kết nối DB và DAO, hoàn thiện tầng Service, thiết kế giao diện View và cuối cùng ghép nối Controller.

3.2. Quá trình làm việc cùng AI
- Sử dụng AI làm công cụ hỗ trợ gợi ý cấu trúc khung cho các câu lệnh truy vấn JDBC có điều kiện động.
- Tham khảo giải pháp kỹ thuật từ AI về thuật toán phân trang dữ liệu và cách khắc phục lỗi hiển thị văn bản dài trên JTable.

3.3. Kiểm soát và tinh chỉnh mã nguồn thực tế
- Rà soát toàn bộ mã nguồn do AI gợi ý, đối chiếu với cấu trúc các bảng thực tế trong MySQL để đồng bộ chính xác tên cột và kiểu dữ liệu.
- Khóa tính năng chỉnh sửa trực tiếp trên từng ô của bảng dữ liệu để tránh xung đột hoặc sai lệch thông tin trong quá trình thao tác.