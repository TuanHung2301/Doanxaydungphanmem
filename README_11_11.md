BÁO CÁO TIẾN ĐỘ DỰ ÁN VÀ Ý HIỂU CÔNG VIỆC TUẦN 2 (NGÀY 11/10/2026)

Học phần: Đồ án Xây dựng và Phát triển Phần mềm  
Sinh viên thực hiện: Nguyễn Tuấn Hưng  
Lớp: 74DCTT27  
Dự án thực tế: Hệ thống Chăm sóc và Xử lý Yêu cầu Khách hàng  


MỤC LỤC BÁO CÁO TUẦN 2
1. Tổng quan tiến độ tuần 2 so với tuần 1
2. Chi tiết các module nghiệp vụ hoàn thiện trong tuần 2
   - 2.1. Module Quản lý Phiếu Yêu cầu (Ticket Management & Workflow)
   - 2.2. Module Điều phối và Phân công công việc (Supervisor & Employee Flow)
   - 2.3. Module Quản lý Danh mục Dịch vụ (Service Management)
   - 2.4. Module Thống kê và Báo cáo Hiệu suất (Admin Stats)
3. Ứng dụng kỹ năng kiểm soát code và làm việc cùng AI trong tuần 2
   - 3.1. Phân rã bài toán theo lát cắt tính năng và commit liên tục
   - 3.2. Kiểm soát lỗi dữ liệu và an toàn truy vấn cơ sở dữ liệu
4. Thực hiện kiểm thử tính năng và xử lý lỗi thực tế
5. KẾ HOẠCH TRIỂN KHAI CHO TUẦN TIẾP THEO

1. TỔNG QUAN TIẾN ĐỘ TUẦN 2 SO VỚI TUẦN 1

1.1. Kết quả đạt được ở Tuần 1 (Báo cáo ngày 04/10):
- Dựng xong kiến trúc đa tầng MVC, các lớp Model (User, Ticket, Service) và kết nối cơ sở dữ liệu DBConnection theo mẫu Singleton.
- Xây dựng hoàn chỉnh tính năng Xác thực đăng nhập (LoginView, AuthController, UserService, UserDAO, Session).
- Đóng gói quy tắc kiểm tra tính hợp lệ của mật khẩu và bảo vệ tài khoản Quản trị viên (ADMIN).

1.2. Mục tiêu trọng tâm của Tuần 2 (Từ 05/10 đến 11/10):
- Hoàn thiện toàn bộ luồng xử lý vòng đời của một phiếu yêu cầu (Ticket Lifecycle): Tiếp nhận -> Phân công -> Đang xử lý -> Hoàn thành / Hủy bỏ.
- Xây dựng phân quyền màn hình theo đúng vai trò người dùng trong Session:
  + ADMIN: Toàn quyền quản trị tài khoản nhân viên, xem thống kê doanh thu và khối lượng công việc.
  + SUPERVISOR (Giám sát viên): Tiếp nhận yêu cầu mới, phân phối công việc cho nhân viên cấp dưới, theo dõi tiến độ xử lý.
  + EMPLOYEE (Nhân viên hỗ trợ): Tiếp nhận công việc được giao, cập nhật tiến độ xử lý và phản hồi giải pháp cho khách hàng.
- Tối ưu hóa truy vấn cơ sở dữ liệu MySQL, chống tràn bộ nhớ với phân trang phía máy chủ.



2. CHI TIẾT CÁC MODULE NGHIỆP VỤ HOÀN THIỆN TRONG TUẦN 2

2.1. Module Quản lý Phiếu Yêu cầu (Ticket Management & Workflow)
- Các file triển khai: Ticket.java, TicketDAO.java, TicketService.java, TicketManagementController.java, CreateTicketDialog.java, TicketDetailDialog.java.
- Ý hiểu nghiệp vụ:
  + Phiếu yêu cầu là thực thể trung tâm của bài toán CSKH. Mỗi phiếu gồm: Mã định danh, Tên khách hàng, Số điện thoại, Dịch vụ yêu cầu, Mô tả chi tiết, Trạng thái (OPEN, IN_PROGRESS, RESOLVED, CLOSED) và Mức độ ưu tiên (LOW, MEDIUM, HIGH, URGENT).
  + Xử lý phân trang và tìm kiếm: Tầng TicketDAO sử dụng SQL truy vấn động kết hợp LIMIT và OFFSET, cho phép lọc nhanh theo từ khóa, số điện thoại hoặc trạng thái phiếu.

2.2. Module Điều phối và Phân công công việc (Supervisor & Employee Flow)
- Các file triển khai: SupervisorView.java, EmployeeView.java, TicketController.java.
- Ý hiểu nghiệp vụ:
  + Giám sát viên (Supervisor) khi đăng nhập vào hệ thống sẽ thấy bảng danh sách các phiếu ở trạng thái OPEN chưa có người nhận.
  + Quy tắc lọc danh sách nhân viên: Phương thức getEmployees() trong UserDAO chỉ lấy ra các nhân viên có trạng thái ACTIVE để giao việc. Ngăn chặn tuyệt đối việc giao phiếu cho tài khoản đã bị vô hiệu hóa (INACTIVE).
  + Khi nhân viên (Employee) đăng nhập, hệ thống dựa vào Session.currentUser.getId() để lọc chính xác các phiếu mà nhân viên đó được phân công, đảm bảo tính bảo mật và độc lập dữ liệu.

2.3. Module Quản lý Danh mục Dịch vụ (Service Management)
- Các file triển khai: Service.java, ServiceDAO.java, ServiceManagementService.java, ServiceManagerView.java, ServiceManagerController.java.
- Ý hiểu nghiệp vụ:
  + Quản lý danh sách các gói cước và dịch vụ hỗ trợ của doanh nghiệp (Tên dịch vụ, Đơn giá, Mô tả, Trạng thái hoạt động).
  + Ràng buộc nghiệp vụ: Kiểm tra trùng tên dịch vụ trước khi thêm mới, chuẩn hóa dữ liệu đầu vào không chứa ký tự lạ gây lỗi giao diện.

2.4. Module Thống kê và Báo cáo Hiệu suất (Admin Stats)
- Các file triển khai: StatsDAO.java, StatsService.java, AdminStatsView.java, StatsController.java.
- Ý hiểu nghiệp vụ:
  + Cung cấp số liệu tổng quan thời gian thực cho Quản trị viên: Tổng số phiếu theo từng trạng thái, tỷ lệ xử lý đúng hạn, số lượng yêu cầu theo từng loại dịch vụ.
  + Tối ưu câu lệnh SQL với các hàm tổng hợp COUNT(), GROUP BY trực tiếp dưới cơ sở dữ liệu thay vì kéo toàn bộ dữ liệu lên bộ nhớ để tính thủ công.


3. ỨNG DỤNG KỸ NĂNG KIỂM SOÁT CODE VÀ LÀM VIỆC CÙNG AI TRONG TUẦN 2

3.1. Phân rã bài toán theo lát cắt tính năng và commit liên tục
- Tránh bẫy gom mã nguồn: Chia nhỏ bài toán theo từng tầng nghiệp vụ (Vertical Slicing) thay vì yêu cầu AI sinh mã hàng loạt cho toàn bộ module.
- Quy trình triển khai từng bước:
  + Bước 1: Xây dựng truy vấn phân công công việc trong TicketDAO -> Review mã nguồn và kiểm tra câu lệnh UPDATE trong MySQL -> Thực hiện Git Commit.
  + Bước 2: Viết logic ràng buộc tại TicketService (kiểm tra phiếu hợp lệ, nhân viên phải ACTIVE) -> Thực hiện Git Commit.
  + Bước 3: Ghép nối sự kiện trên SupervisorView và TicketController -> Chạy thử nghiệm trên giao diện -> Thực hiện Git Commit.
- Kết quả: Tiến trình commit phản ánh đúng từng bước hoàn thiện tính năng, mã nguồn được kiểm thử chắc chắn trước khi chuyển bước tiếp theo.

3.2. Kiểm soát lỗi dữ liệu và An toàn truy vấn
- Luôn sử dụng PreparedStatement cho mọi câu lệnh SQL có tham số để triệt tiêu nguy cơ tấn công SQL Injection.
- Sử dụng cú pháp try-with-resources để quản lý Connection, PreparedStatement và ResultSet, đảm bảo tài nguyên luôn được giải phóng ngay cả khi có ngoại lệ xảy ra.
- Tự động chuẩn hóa dữ liệu nhập vào: Cắt tỉa khoảng trắng ở hai đầu (trim()) và kiểm tra chuỗi rỗng trước khi xử lý logic.



4. THỰC HIỆN KIỂM THỬ TÍNH NĂNG VÀ XỬ LÝ LỖI THỰC TẾ

4.1. Các kịch bản kiểm thử đã thực hiện:
- Kịch bản 1: Kiểm thử phân quyền truy cập. Đăng nhập bằng tài khoản EMPLOYEE kiểm tra xem có bị ẩn các chức năng Quản trị tài khoản và Thống kê hay không -> Kết quả: Đạt.
- Kịch bản 2: Kiểm thử ràng buộc phân công. Thử phân công phiếu cho một nhân viên vừa bị đổi trạng thái sang INACTIVE -> Kết quả: Hệ thống chặn thành công và báo lỗi hợp lý.
- Kịch bản 3: Kiểm thử hiển thị văn bản dài trên bảng JTable. Nhập mô tả yêu cầu khách hàng dài hơn 500 ký tự -> Tùy chỉnh renderer cột để chữ tự động xuống dòng hoặc hiển thị xem chi tiết qua TicketDetailDialog mà không làm vỡ bố cục bảng -> Kết quả: Đạt.

4.2. Lỗi thực tế phát sinh và cách khắc phục:
- Lỗi kết nối đồng thời: Trong quá trình thao tác nhiều tác vụ cùng lúc, ứng dụng đôi khi bị khóa dữ liệu (database lock). Cách khắc phục: Rà soát lại việc đóng ResultSet và PreparedStatement tại các hàm trong TicketDAO và UserDAO.
- Lỗi giao diện Swing đơ cứng khi tải dữ liệu lớn: Khi tải danh sách hàng nghìn bản ghi, luồng xử lý sự kiện Event Dispatch Thread (EDT) bị nghẽn làm đơ chuột. Cách khắc phục: Bổ sung phân trang LIMIT 15 dòng trên một trang để thời gian phản hồi dưới 0.1 giây.



5. KẾ HOẠCH TRIỂN KHAI CHO TUẦN TIẾP THEO

- Bổ sung chức năng xuất báo cáo thống kê ra file định dạng Excel/PDF phục vụ công tác lưu trữ của doanh nghiệp.
- Bổ sung mã hóa.
- Nâng cấp bộ kiểm thử tự động với JUnit 5 cho toàn bộ các hàm nghiệp vụ trong TicketService.
- Rà soát toàn bộ giao diện, hoàn thiện bộ tài liệu hướng dẫn cài đặt và sử dụng phần mềm để chuẩn bị cho buổi cuối.
