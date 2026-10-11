package dao;

import DB.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StatsDAO {

    public List<Object[]> getEmployeePerformance() {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT u.full_name, COUNT(t.id) as total, SUM(CASE WHEN t.status IN ('OPEN', 'PROCESSING') THEN 1 ELSE 0 END) as processing, SUM(CASE WHEN t.status IN ('RESOLVED', 'CLOSED') THEN 1 ELSE 0 END) as completed, AVG(TIMESTAMPDIFF(HOUR, t.created_at, t.completed_at)) as avg_time FROM users u LEFT JOIN tickets t ON u.id = t.assigned_to WHERE u.role = 'EMPLOYEE' GROUP BY u.id, u.full_name";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int total = rs.getInt("total"), completed = rs.getInt("completed");
                double avgTime = rs.getDouble("avg_time");
                String rate = (total == 0) ? "0%" : String.format("%.1f%%", ((double) completed / total) * 100);
                String timeStr = (rs.getObject("avg_time") == null) ? "N/A" : String.format("%.1f h", avgTime);
                data.add(new Object[]{rs.getString("full_name"), total, rs.getInt("processing"), completed, rate, timeStr});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<Object[]> getStatsByTime() {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT DATE_FORMAT(created_at, '%m-%Y') as month_year, COUNT(*) as total, SUM(CASE WHEN status IN ('RESOLVED','CLOSED') THEN 1 ELSE 0 END) as done FROM tickets GROUP BY DATE_FORMAT(created_at, '%m-%Y') ORDER BY MIN(created_at) DESC LIMIT 12";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                data.add(new Object[]{rs.getString("month_year"), rs.getInt("total"), rs.getInt("done")});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<Object[]> getStatsByType() {
        List<Object[]> data = new ArrayList<>();
        int totalAll = 0;
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM tickets")) {
                if (rs.next()) {
                    totalAll = rs.getInt(1);
                }
            }
            try (ResultSet rs2 = stmt.executeQuery("SELECT priority, COUNT(*) as cnt FROM tickets GROUP BY priority")) {
                while (rs2.next()) {
                    int cnt = rs2.getInt("cnt");
                    String percent = (totalAll == 0) ? "0%" : String.format("%.1f%%", ((double) cnt / totalAll) * 100);
                    data.add(new Object[]{rs2.getString("priority"), cnt, percent});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
