package dao;

import DB.DBConnection;
import model.Ticket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

    public String[] getCustomerDetails(int ticketId) {
        // SQL chỉ lấy đúng 3 cột có thật
        String sql = "SELECT c.full_name, c.phone, c.address FROM tickets t JOIN customers c ON t.customer_id = c.id WHERE t.id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Trả về chuỗi rỗng "" ở vị trí thứ 2 để lấp chỗ trống của email
                    return new String[]{rs.getString("full_name"), "", rs.getString("phone"), rs.getString("address")};
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCustomerInfoByTicketId(int ticketId) {
        String[] details = getCustomerDetails(ticketId);
        if (details != null) {
            return "Tên: " + details[0] + "\nEmail: " + details[1] + "\nSĐT: " + details[2] + "\nĐ/c: " + details[3];
        }
        return "Không tìm thấy thông tin!";
    }

    public boolean deleteTicket(int id) {
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM interactions WHERE ticket_id=" + id);
            return stmt.executeUpdate("DELETE FROM tickets WHERE id=" + id) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    
    // TRUY VẤN TỐI ƯU HIỆU NĂNG: TÌM KIẾM & PHÂN TRANG TRỰC TIẾP DƯỚI DATABASE
    
    public List<Ticket> getTicketsByPage(int userId, String keyword, int page, int pageSize) {
        List<Ticket> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String search = "%" + keyword + "%";

        StringBuilder sql = new StringBuilder(
                "SELECT t.*, c.full_name as c_name, u.full_name as u_name FROM tickets t "
                + "LEFT JOIN customers c ON t.customer_id = c.id "
                + "LEFT JOIN users u ON t.assigned_to = u.id "
                + "WHERE (t.title LIKE ? OR c.full_name LIKE ? OR u.full_name LIKE ? OR t.status LIKE ?) "
        );

        if (userId > 0) {
            sql.append("AND t.assigned_to = ? ");
        }
        sql.append("ORDER BY t.id ASC LIMIT ? OFFSET ?");

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);
            ps.setString(4, search);
            int paramIndex = 5;
            if (userId > 0) {
                ps.setInt(paramIndex++, userId);
            }
            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Ticket(rs.getInt("id"), rs.getString("title"), rs.getString("description"), rs.getString("status"), rs.getString("priority"), rs.getInt("customer_id"), rs.getInt("assigned_to"), rs.getTimestamp("created_at"), rs.getTimestamp("completed_at"), rs.getString("c_name"), rs.getString("u_name")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getTotalTickets(int userId, String keyword) {
        String search = "%" + keyword + "%";
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM tickets t LEFT JOIN customers c ON t.customer_id = c.id LEFT JOIN users u ON t.assigned_to = u.id "
                + "WHERE (t.title LIKE ? OR c.full_name LIKE ? OR u.full_name LIKE ? OR t.status LIKE ?) "
        );
        if (userId > 0) {
            sql.append("AND t.assigned_to = ?");
        }

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);
            ps.setString(4, search);
            if (userId > 0) {
                ps.setInt(5, userId);
            }
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
  

    public boolean createTicket(String title, String desc, int customerId, int employeeId) {
        String sql = "INSERT INTO tickets(title, description, customer_id, assigned_to, status, priority, created_at) VALUES(?,?,?,?, 'OPEN', 'MEDIUM', NOW())";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, desc);
            ps.setInt(3, customerId);
            ps.setInt(4, employeeId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateContent(int id, String newTitle) {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement("UPDATE tickets SET title=? WHERE id=?")) {
            ps.setString(1, newTitle);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStatusAndLog(int tId, String status, int uId, String note) {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement("UPDATE tickets SET status=?, completed_at = CASE WHEN ? IN ('RESOLVED','CLOSED') THEN NOW() ELSE NULL END WHERE id=?")) {
            ps.setString(1, status);
            ps.setString(2, status);
            ps.setInt(3, tId);
            if (ps.executeUpdate() > 0) {
                addInteraction(tId, uId, "UPDATE_STATUS", "Đổi trạng thái sang " + status + ". " + note);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addInteraction(int tId, int uId, String type, String content) {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement("INSERT INTO interactions(ticket_id, user_id, action_type, comments, created_at) VALUES(?,?,?,?, NOW())")) {
            ps.setInt(1, tId);
            ps.setInt(2, uId);
            ps.setString(3, type);
            ps.setString(4, content);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean assignTicket(int ticketId, int userId, String note) {
        if (updateStatusAndLog(ticketId, "PROCESSING", userId, note)) {
            try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("UPDATE tickets SET assigned_to=" + userId + " WHERE id=" + ticketId);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public String getHistory(int ticketId) {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT i.comments, i.action_type, u.full_name, DATE_FORMAT(i.created_at, '%d/%m %H:%i') as nice_time FROM interactions i JOIN users u ON i.user_id = u.id WHERE i.ticket_id = ? ORDER BY i.created_at ASC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("full_name"), time = rs.getString("nice_time"), content = rs.getString("comments"), type = rs.getString("action_type");
                    if ("UPDATE_STATUS".equals(type)) {
                        sb.append(">>> HỆ THỐNG (").append(time).append("): ").append(content).append("\n");
                    } else {
                        sb.append(name).append(" (").append(time).append("): ").append(content).append("\n");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (sb.length() == 0) {
            return " Chưa có tin nhắn nào";
        }
        return sb.toString();
    }

    public void simulateAutoReply(int ticketId) {
        String[] replies = {"Vâng ạ.", "Tôi hiểu rồi, cảm ơn shop.", "Ok shop.", "Bạn chờ tôi chút...", "Thế bao lâu thì xong ạ?", "Cảm ơn bạn nhé!", "Ok bạn."};
        addInteraction(ticketId, 999, "RESPONSE", replies[(int) (Math.random() * replies.length)]); 
    }
    
    public boolean clearHistory(int ticketId) {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement("DELETE FROM interactions WHERE ticket_id = ?")) {
            ps.setInt(1, ticketId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
