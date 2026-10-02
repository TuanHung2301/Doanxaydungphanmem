package service;

import dao.ServiceDAO;
import model.Service;
import java.util.List;

public class ServiceManagementService {

    private ServiceDAO serviceDAO = new ServiceDAO();

    public List<Service> getServicesByPage(String keyword, int page, int pageSize) {
        return serviceDAO.getServicesByPage(keyword != null ? keyword.trim() : "", page, pageSize);
    }

    public int getTotalServices(String keyword) {
        return serviceDAO.getTotalServices(keyword != null ? keyword.trim() : "");
    }

    // Hàm này giữ lại để tạo TicketController gọi lấy danh sách đổ vào ComboBox
    public List<Service> getAllServices() {
        return serviceDAO.getAllServices();
    }

    // NGHIỆP VỤ THỰC SỰ: GÁNH TOÀN BỘ LOGIC THAY CHO CONTROLLER
    public String processAddService(Service s) {

        String serviceName = s.getServiceName() != null ? s.getServiceName().trim() : "";

        if (serviceName.isEmpty()) {
            return "Tên dịch vụ không được để trống!";
        }

        if (serviceDAO.isServiceNameExist(serviceName, -1)) {
            return "Dịch vụ [" + serviceName + "] đã tồn tại trên hệ thống!";
        }

        s.setServiceName(serviceName);

        //Đẩy xuống DB
        return serviceDAO.addService(s) ? "" : "Lỗi hệ thống: Không thể thêm mới dịch vụ lúc này!";
    }

    public String processUpdateService(Service newS, Service oldS) {

        if (oldS == null) {
            return "Chưa chọn dịch vụ để cập nhật!";
        }

        // 2. Lấy và làm sạch dữ liệu đầu vào
        String newName = newS.getServiceName() != null ? newS.getServiceName().trim() : "";
        String newDesc = newS.getDescription() != null ? newS.getDescription().trim() : "";

        if (newName.isEmpty()) {
            return "Tên dịch vụ không được để trống!";
        }

        newS.setServiceName(newName);
        newS.setDescription(newDesc);
        newS.setId(oldS.getId());

        try {

            if (serviceDAO.isServiceNameExist(newName, oldS.getId())) {
                return "Dịch vụ [" + newName + "] đã tồn tại trên hệ thống!";
            }

            return serviceDAO.updateService(newS) ? "" : "Lỗi hệ thống: Cập nhật dịch vụ thất bại!";

        } catch (RuntimeException e) {
            e.printStackTrace();
            return "Lỗi hệ thống: Không thể thực thi yêu cầu lúc này.";
        }
    }

    public String processDeleteService(int id) {
        return serviceDAO.deleteService(id) ? "" : " Lỗi xóa: Dịch vụ này có thể đang được sử dụng trong các Yêu cầu (Ticket)!";
    }
}
