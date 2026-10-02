package service;

import dao.TicketDAO;
import dao.UserDAO;
import model.Ticket;
import java.util.List;

public class TicketService {

    private TicketDAO ticketDAO = new TicketDAO();
    private UserDAO userDAO = new UserDAO();

    // Gọi 2 hàm lấy SQL trực tiếp (Bỏ Stream)
    public List<Ticket> getTicketsByPage(int userId, String keyword, int page, int pageSize) {
        return ticketDAO.getTicketsByPage(userId, keyword != null ? keyword.trim() : "", page, pageSize);
    }

    public int getTotalTickets(int userId, String keyword) {
        return ticketDAO.getTotalTickets(userId, keyword != null ? keyword.trim() : "");
    }

    public boolean createTicket(String title, String desc, int customerId, int creatorId) {
        return ticketDAO.createTicket(title, desc, customerId, creatorId);
    }

    public boolean assignTicket(int ticketId, int userId, String note) {
        return ticketDAO.assignTicket(ticketId, userId, note);
    }

    public boolean deleteTicket(int id) {
        return ticketDAO.deleteTicket(id);
    }

    public void updateContent(int id, String newTitle) {
        if (newTitle != null && !newTitle.trim().isEmpty()) {
            ticketDAO.updateContent(id, newTitle.trim());
        }
    }

    public String getHistory(int ticketId) {
        return ticketDAO.getHistory(ticketId);
    }

    public void sendResponse(int tId, int uId, String msg) {
        if (msg != null && !msg.trim().isEmpty()) {
            ticketDAO.addInteraction(tId, uId, "RESPONSE", msg.trim());
        }
    }

    public void simulateAutoReply(int tId) {
        ticketDAO.simulateAutoReply(tId);
    }

    public boolean updateStatus(int tId, String status, int uId, String note) {
        boolean success = ticketDAO.updateStatusAndLog(tId, status, uId, note != null ? note.trim() : "");
        if (success) {
            new Thread(() -> {
                try {
                    utils.EmailUtils.sendEmail("hungnguyen20052301@gmail.com", "Cập nhật yêu cầu #" + tId, "Trạng thái mới: " + status + " - Ghi chú: " + note);
                } catch (Exception ex) {
                }
            }).start();
        }
        return success;
    }

    public boolean clearHistory(int tId) {
        return ticketDAO.clearHistory(tId);
    }

    public String getCustomerInfo(int tId) {
        return ticketDAO.getCustomerInfoByTicketId(tId);
    }

    public String[] getCustomerDetails(int tId) {
        return ticketDAO.getCustomerDetails(tId);
    }

    public String processCreateTicketNewCustomer(String phone, String name, String title, String serviceName, int creatorId) {
        if (phone.isEmpty() || name.isEmpty() || title.isEmpty()) {
            return "Vui lòng nhập đủ thông tin (*)";
        }
        String fullTitle = serviceName.isEmpty() ? title : "[" + serviceName + "] " + title;

        if (userDAO.addCustomer(name, phone)) {
            model.User created = userDAO.findByPhone(phone);//Lấy ngược lại thông tin khách hàng vừa lưu
            if (created != null && ticketDAO.createTicket(fullTitle, "Hỗ trợ khách mới", created.getId(), creatorId)) {
                return "";
            }
            return "Lỗi: Không thể tạo yêu cầu lúc này!";
        }
        return "Lỗi: Không thể lưu hồ sơ khách hàng mới (SĐT đã tồn tại)!";
    }

    public String processCreateTicketOldCustomer(int customerId, String title, String serviceName, int creatorId) {
        if (title.isEmpty()) {
            return "Vui lòng nhập nội dung (*)";
        }
        if (customerId == -1) {
            return "Bạn phải bấm Tìm kiếm trước!";
        }
        String fullTitle = serviceName.isEmpty() ? title : "[" + serviceName + "] " + title;

        return ticketDAO.createTicket(fullTitle, "Hỗ trợ khách cũ", customerId, creatorId) ? "" : "Lỗi tạo yêu cầu!";
    }
}
