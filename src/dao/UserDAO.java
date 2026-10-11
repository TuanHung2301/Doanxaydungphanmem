package dao;

import DB.DBConnection;
import model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    
    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND status = 'ACTIVE'";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getString("username"), null, rs.getString("full_name"), rs.getString("role"), rs.getString("status"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Ẩn nhân viên INACTIVE khỏi danh sách điều phối/phân công của Supervisor
    public List<User> getEmployees() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE status = 'ACTIVE'";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("full_name"), rs.getString("role"), rs.getString("status")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updatePassword(int userId, String newPassword) {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement("UPDATE users SET password = ? WHERE id = ?")) {
            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public List<User> getUsersByPage(String keyword, int page, int pageSize) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE (status = 'ACTIVE' OR status = 'INACTIVE') AND (username LIKE ? OR full_name LIKE ?) LIMIT ? OFFSET ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setInt(3, pageSize);
            ps.setInt(4, (page - 1) * pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("full_name"), rs.getString("role"), rs.getString("status")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //Hàm đếm tổng số lượng user chuẩn nghiệp vụ ACTIVE / INACTIVE 
    public int getTotalUsers(String keyword) {
        String sql = "SELECT COUNT(*) FROM users WHERE (status = 'ACTIVE' OR status = 'INACTIVE') AND (username LIKE ? OR full_name LIKE ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean addUser(User u) {
        String sql = "INSERT INTO users (username, password, full_name, role, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getFullName());
            ps.setString(4, u.getRole());
            ps.setString(5, u.getStatus());
            return ps.executeUpdate() > 0;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUser(User u) {
        String sql = "UPDATE users SET username = ?, password = ?, full_name = ?, role = ?, status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getFullName());
            ps.setString(4, u.getRole());
            ps.setString(5, u.getStatus());
            ps.setInt(6, u.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Thực thi xóa mềm chuyển trạng thái thành INACTIVE
    public String smartDeleteUser(int id) {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement("UPDATE users SET status = 'INACTIVE' WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0 ? "Đã vô hiệu hóa tài khoản thành công!" : "Không tìm thấy nhân viên!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi Database: Không thể vô hiệu hóa lúc này!";
        }
    }

    public User findByPhone(String phone) {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM customers WHERE phone = ?")) {
            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"), null, null, rs.getString("full_name"), null, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addCustomer(String fullName, String phone) {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement("INSERT INTO customers (full_name, phone, address) VALUES (?, ?, 'Chưa xác định', '')")) {
            ps.setString(1, fullName);
            ps.setString(2, phone);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

   
    public boolean isUsernameExist(String username, int excludeId) {    
        String sql = "SELECT 1 FROM users WHERE username = ? AND id != ? AND (status = 'ACTIVE' OR status = 'INACTIVE')";
        // chỉ cần kiểm tra username = 
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setInt(2, excludeId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 7. ĐÃ ĐỔI: Lấy thông tin chi tiết 1 bản ghi bằng ID
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ? AND (status = 'ACTIVE' OR status = 'INACTIVE')";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
                            rs.getString("full_name"), rs.getString("role"), rs.getString("status"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
