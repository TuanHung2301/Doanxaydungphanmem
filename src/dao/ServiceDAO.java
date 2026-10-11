package dao;

import DB.DBConnection;
import model.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {

    public List<Service> getServicesByPage(String keyword, int page, int pageSize) {
        List<Service> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT * FROM services WHERE service_name LIKE ? OR description LIKE ? ORDER BY id ASC LIMIT ? OFFSET ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            String search = "%" + keyword + "%";
            ps.setString(1, search);
            ps.setString(2, search);
            ps.setInt(3, pageSize);
            ps.setInt(4, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Service(rs.getInt("id"), rs.getString("service_name"), rs.getString("description")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getTotalServices(String keyword) {
        String sql = "SELECT COUNT(*) FROM services WHERE service_name LIKE ? OR description LIKE ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            String search = "%" + keyword + "%";
            ps.setString(1, search);
            ps.setString(2, search);
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
    public boolean isServiceNameExist(String serviceName, int excludeId) {
        // Bỏ qua ID hiện tại khi đang Update, hoặc -1 nếu là Add mới
        String sql = "SELECT 1 FROM services WHERE service_name = ? AND id != ?";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, serviceName);
            ps.setInt(2, excludeId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Nếu có kết quả tức là đã bị trùng
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return false;
    }

    public boolean addService(Service s) {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement("INSERT INTO services(service_name, description) VALUES(?,?)")) {
            ps.setString(1, s.getServiceName());
            ps.setString(2, s.getDescription());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateService(Service s) {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement("UPDATE services SET service_name=?, description=? WHERE id=?")) {
            ps.setString(1, s.getServiceName());
            ps.setString(2, s.getDescription());
            ps.setInt(3, s.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteService(int id) {
        try (Connection conn = DBConnection.getConnection(); java.sql.Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("UPDATE tickets SET service_id=NULL WHERE service_id=" + id);
            return stmt.executeUpdate("DELETE FROM services WHERE id=" + id) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Service> getAllServices() {
        List<Service> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM services ORDER BY id ASC"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Service(rs.getInt("id"), rs.getString("service_name"), rs.getString("description")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
