package controller;

import DB.Session;
import model.User;
import service.ServiceManagementService;
import service.TicketService;
import service.UserService;
import view.*;
import javax.swing.JOptionPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TicketController {

    private TicketService ticketService;
    private UserService userService;

    private EmployeeView empView;

    private int empCurrentPage = 1, empPageSize = 10, empTotalPages = 1;

    public TicketController(EmployeeView view) {
        this.empView = view;
        this.ticketService = new TicketService();
        this.userService = new UserService();
        initEmployeeEvents();
        loadEmployeeData();
        this.empView.setVisible(true);
    }

  
    private void initEmployeeEvents() {
        empView.addRefreshListener(e -> {
            empView.clearSearch();
            empCurrentPage = 1;
            loadEmployeeData();
        });
        empView.addFilterListener(e -> {
            empCurrentPage = 1;
            loadEmployeeData();
        });
        empView.addSearchListener(e -> {
            empCurrentPage = 1;
            loadEmployeeData();
        });
        empView.addPrevListener(e -> {
            if (empCurrentPage > 1) {
                empCurrentPage--;
                loadEmployeeData();
            }
        });
        empView.addNextListener(e -> {
            if (empCurrentPage < empTotalPages) {
                empCurrentPage++;
                loadEmployeeData();
            }
        });

        empView.addCreateListener(e -> createTicket());
        empView.addEditListener(e -> editTicketContent());
        empView.addInfoListener(e -> showCustomerInfo(empView, empView.getSelectedTicketId()));
        empView.addWorkListener(e -> openChatDialog(empView));

        empView.addTableMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openChatDialog(empView);
                }
            }
        });
    }

    private void loadEmployeeData() {
        int userId = (empView.getFilterIndex() == 0) ? Session.currentUser.getId() : -1;
        String keyword = empView.getSearchKeyword().trim();

        int totalRows = ticketService.getTotalTickets(userId, keyword);
        empTotalPages = Math.max(1, (int) Math.ceil((double) totalRows / empPageSize));
        if (empCurrentPage > empTotalPages) {
            empCurrentPage = empTotalPages;
        }

        // Controller giờ chỉ nhận dữ liệu đã được lọc sẵn từ Database
        List<model.Ticket> pagedList = ticketService.getTicketsByPage(userId, keyword, empCurrentPage, empPageSize);

        empView.setTableData(pagedList, empCurrentPage, empPageSize);
        empView.setPageInfo(empCurrentPage, empTotalPages, totalRows);
    }

    private void createTicket() {
        CreateTicketDialog dialog = new CreateTicketDialog(empView);
        final int[] foundCustomerId = {-1};//lưu ID khách hàng khi tìm kiếm khách cũ

        try {
            dialog.loadServices(new ServiceManagementService().getAllServices());
        } catch (Exception ex) {
        }

        dialog.addRadioListener(e -> {// Lấy xem người dùng đang tích vào nút Khách Mới hay Khách Cũ
            boolean isNew = dialog.isNewCustomer();
            dialog.enableSearch(!isNew);
            dialog.enableName(isNew);
            dialog.setCustomerName("");
            dialog.setPhone("");
            foundCustomerId[0] = -1;
        });

        dialog.addSearchListener(e -> {
            String phone = dialog.getPhone();
            if (phone.isEmpty()) {
                empView.showMessage("️Nhập SĐT để tìm");
                return;
            }
            User c = userService.findByPhone(phone);
            if (c != null) {
                dialog.setCustomerName(c.getFullName());
                foundCustomerId[0] = c.getId();
                empView.showMessage("Đã tìm thấy: " + c.getFullName());
            } else {
                empView.showMessage("Không tìm thấy SĐT này!");
                foundCustomerId[0] = -1;
            }
        });

        dialog.addSubmitListener(e -> {
            String phone = dialog.getPhone(), name = dialog.getCustomerName();
            String title = dialog.getTitle(), serviceName = dialog.getServiceName();

            if (dialog.isNewCustomer()) {
                String result = ticketService.processCreateTicketNewCustomer(phone, name, title, serviceName, Session.currentUser.getId());
                if (result.isEmpty()) {
                    empView.showMessage(" Đã tạo hồ sơ & yêu cầu!");
                    dialog.dispose();
                    loadEmployeeData();
                } else {
                    empView.showMessage(result);
                }
            } else {
                String result = ticketService.processCreateTicketOldCustomer(foundCustomerId[0], title, serviceName, Session.currentUser.getId());
                if (result.isEmpty()) {
                    empView.showMessage("Đã tạo yêu cầu");
                    dialog.dispose();
                    loadEmployeeData();
                } else {
                    empView.showMessage(result);
                }
            }
        });
        dialog.setVisible(true);
    }

    private void editTicketContent() {
        int id = empView.getSelectedTicketId();
        if (id < 0) {
            empView.showMessage("️Chọn yêu cầu để sửa");
            return;
        }
        String newContent = empView.showInput("Chỉnh sửa nội dung yêu cầu:", empView.getSelectedTitle());
        if (newContent != null && !newContent.trim().isEmpty()) {
            ticketService.updateContent(id, newContent);
            empView.showMessage(" Đã cập nhật thành công!");
            loadEmployeeData();
        }
    }

    private void openChatDialog(EmployeeView parent) {
        int ticketId = parent.getSelectedTicketId();
        if (ticketId < 0) {
            JOptionPane.showMessageDialog(parent, "️Chọn một yêu cầu!");
            return;
        }

        TicketDetailDialog dlg = new TicketDetailDialog(parent, ticketId);
        dlg.setChatHistory(ticketService.getHistory(ticketId));

        dlg.addSendListener(e -> {
            String msg = dlg.getMessage();
            if (!msg.isEmpty()) {
                // 1. Ghi nhận tin nhắn của Nhân viên vào DB và tải lại khung chat
                ticketService.sendResponse(ticketId, Session.currentUser.getId(), "Phản hồi: " + msg);
                dlg.clearMessageInput();
                dlg.setChatHistory(ticketService.getHistory(ticketId));

                new javax.swing.SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        Thread.sleep(500);
                        ticketService.simulateAutoReply(ticketId); // Gọi hàm sinh câu chửi/khen ngẫu nhiên
                        return null;
                    }

                    @Override
                    protected void done() {
                        // 3. Tải lại khung chat lần nữa để hiển thị câu trả lời của Khách
                        dlg.setChatHistory(ticketService.getHistory(ticketId));
                    }
                }.execute();

            }
        });

        dlg.addUpdateStatusListener(e -> {
            String note = dlg.showInput("Ghi chú cập nhật:");
            if (note != null && ticketService.updateStatus(ticketId, dlg.getSelectedStatus(), Session.currentUser.getId(), note)) {
                dlg.showMessage("Đã cập nhật trạng thái!");
                dlg.setChatHistory(ticketService.getHistory(ticketId));
                loadEmployeeData();
            }
        });
        dlg.setVisible(true);
    }

    private void showCustomerInfo(javax.swing.JFrame parent, int ticketId) {
        if (ticketId < 0) {
            JOptionPane.showMessageDialog(parent, "️Vui lòng chọn một yêu cầu!");
            return;
        }
        CustomerInfoDialog dlg = new CustomerInfoDialog(parent, ticketId);
        dlg.setCustomerData(ticketService.getCustomerDetails(ticketId));
        dlg.setVisible(true);
    }
}
