package service;

import dao.UserDAO;
import model.User;
import java.util.List;

public class UserService {

    private UserDAO userDAO = new UserDAO();

    public User login(String username, String password) {
        return userDAO.login(username, password);
    }

    public List<User> getEmployees() {
        return userDAO.getEmployees();
    }

    public List<User> getUsersByPage(String keyword, int page, int pageSize) {
        return userDAO.getUsersByPage(keyword, page, pageSize);
    }

    public int getTotalUsers(String keyword) {
        return userDAO.getTotalUsers(keyword);
    }

    public User findByPhone(String phone) {
        return userDAO.findByPhone(phone);
    }

    public String processAddUser(User u) {
        //lấy dữ liệu từ giao diện và loại bỏ khoảng trắng
        String username = u.getUsername() != null ? u.getUsername().trim() : "";
        String password = u.getPassword() != null ? u.getPassword().trim() : "";
        String fullName = u.getFullName() != null ? u.getFullName().trim() : "";

        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
            return "Tên đăng nhập, Mật khẩu và Họ tên không được để trống.";
        }

        if (password.length() < 8 || !password.matches(".*[^a-zA-Z0-9].*")) {
            return "Mật khẩu phải dài tối thiểu 8 ký tự và có ít nhất một ký tự đặc biệt (!, @, #, $, %...).";
        }

        if ("INACTIVE".equalsIgnoreCase(u.getStatus())) {
            return "Lỗi: Không được phép thêm mới tài khoản ở trạng thái Vô hiệu hóa (INACTIVE)!";
        }

        u.setUsername(username);
        u.setPassword(password);
        u.setFullName(fullName);

        try {
            if (userDAO.isUsernameExist(username, -1)) {
                // khai báo biến rõ ràng hơn ko cần chuyền -1
                return "Tài khoản [" + username + "] đã tồn tại trên hệ thống.";
            }
            return userDAO.addUser(u) ? "" : "Lỗi hệ thống: Không thể thực thi yêu cầu.";
        } catch (RuntimeException e) {
            e.printStackTrace();
            return "Lỗi hệ thống: Không thể thực thi yêu cầu.";
        }
    }

    public String processUpdateUser(User u, int id) {
        
        String username = u.getUsername() != null ? u.getUsername().trim() : "";
        String fullName = u.getFullName() != null ? u.getFullName().trim() : "";

        if (username.isEmpty() || fullName.isEmpty()) {
            return "Tên đăng nhập và Họ tên không được để trống.";
        }

        u.setUsername(username);
        u.setFullName(fullName);

        try {

            if (userDAO.isUsernameExist(username, id)) {
                return "Tài khoản [" + username + "] đã bị trùng với người khác.";
            }

            User oldUser = userDAO.getUserById(id);
            if (oldUser == null) {
                return "Không tìm thấy dữ liệu gốc!";
            }

            if ("ADMIN".equalsIgnoreCase(oldUser.getRole()) && (!"ADMIN".equalsIgnoreCase(u.getRole()) || "INACTIVE".equalsIgnoreCase(u.getStatus()))) {
                return "Cảnh báo vi phạm: Không được hạ quyền hoặc vô hiệu hóa Quản trị viên (ADMIN)!";
            }

            u.setPassword(oldUser.getPassword());

            return userDAO.updateUser(u) ? "" : "Lỗi hệ thống: Cập nhật thất bại!";

        } catch (RuntimeException e) {
            e.printStackTrace();
            return "Lỗi hệ thống: Không thể thực thi yêu cầu.";
        }
    }

    public String processDeleteUser(int id) {
        try {
            User oldUser = userDAO.getUserById(id);//trước khi xóa là để lấy toàn bộ thông tin gốc hiện tại của tài khoản đó từ Database lên bộ nhớ.
            if (oldUser == null) {
                return "Không tìm thấy tài khoản!";
            }

            if ("INACTIVE".equalsIgnoreCase(oldUser.getStatus())) {
                return "Tài khoản này đã bị vô hiệu hóa từ trước!";
            }

            if ("ADMIN".equalsIgnoreCase(oldUser.getRole())) {
                return "Cảnh báo vi phạm: Không thể vô hiệu hóa tài khoản của Quản trị viên (ADMIN)!";
            }

            return userDAO.smartDeleteUser(id);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return "Lỗi hệ thống: Không thể thực thi yêu cầu.";
        }
    }

    public String processResetPassword(int userId, String newPass) {
        String password = newPass != null ? newPass.trim() : "";

        if (password.isEmpty()) {
            return "Lỗi: Mật khẩu mới không được để trống!";
        }

        if (password.length() < 8 || !password.matches(".*[^a-zA-Z0-9].*")) {
            return "Lỗi: Mật khẩu mới phải dài tối thiểu 8 ký tự và có ít nhất một ký tự đặc biệt (!, @, #, $, %...).";
        }

        try {
            return userDAO.updatePassword(userId, password) ? "" : "Lỗi Database: Không thể cập nhật mật khẩu lúc này!";

        } catch (RuntimeException e) {
            e.printStackTrace();
            return "Lỗi hệ thống: Không thể thực thi yêu cầu.";
        }

    }
}
