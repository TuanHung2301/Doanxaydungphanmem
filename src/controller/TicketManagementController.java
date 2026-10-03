package controller;

import DB.Session;
import model.Ticket;
import model.User;
import service.StatsService;
import service.TicketService;
import service.UserService;
import view.SupervisorView;
import view.TicketDetailDialog;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class TicketManagementController {

    private SupervisorView view;
    private TicketService ticketService;
    private UserService userService;
    private StatsService statsService;

    private int currentPage = 1, pageSize = 10, totalPages = 1;
    private List<User> allEmployees = new ArrayList<>();

    public TicketManagementController(SupervisorView view) {
        this.view = view;
        this.ticketService = new TicketService();
        this.userService = new UserService();
        this.statsService = new StatsService();
        initController();
        loadAllData();
        this.view.setVisible(true);
    }

    private void initController() {
        view.addRefreshListener(e -> {
            view.clearSearch();
            currentPage = 1;
            loadAllData();
        });
        view.addAssignListener(e -> handleAssign());
        view.addDetailListener(e -> openChatDialog());
        view.addDeleteListener(e -> handleDelete());
        view.addSearchListener(e -> {
            currentPage = 1;
            filterSupervisorTickets();
        });

        view.addEmployeeSearchListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                handleEmployeeSearch();
            }
        });

        view.addPrevListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                filterSupervisorTickets();
            }
        });
        view.addNextListener(e -> {
            if (currentPage < totalPages) {
                currentPage++;
                filterSupervisorTickets();
            }
        });
        view.addTableMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openChatDialog();
                }
            }
        });
    }

    private void loadAllData() {
        allEmployees = userService.getEmployees();
        view.setEmployeeData(allEmployees);
        DefaultTableModel modelEmp = new DefaultTableModel(new Object[]{"Nhân viên", "Đã giao", "Đang xử lý", "Hoàn thành", "Tỉ lệ Success", "Thời gian TB (Giờ)"}, 0);
        for (Object[] row : statsService.getEmployeePerformance()) {
            modelEmp.addRow(row);
        }

        DefaultTableModel modelTime = new DefaultTableModel(new Object[]{"Tháng / Năm", "Tổng Yêu cầu", "Đã xong"}, 0);
        for (Object[] row : statsService.getStatsByTime()) {
            modelTime.addRow(row);
        }

        DefaultTableModel modelType = new DefaultTableModel(new Object[]{"Mức độ ưu tiên", "Số lượng", "Tỉ trọng"}, 0);
        for (Object[] row : statsService.getStatsByType()) {
            modelType.addRow(row);
        }

        view.setStatsModels(modelEmp, modelTime, modelType);

        currentPage = 1;
        filterSupervisorTickets();
    }

    private void filterSupervisorTickets() {
        String keyword = view.getSearchKeyword().trim();

        int totalRows = ticketService.getTotalTickets(-1, keyword); // -1 lấy All Tickets
        totalPages = Math.max(1, (int) Math.ceil((double) totalRows / pageSize));
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        List<model.Ticket> pagedList = ticketService.getTicketsByPage(-1, keyword, currentPage, pageSize);

        view.setTicketData(pagedList, currentPage, pageSize);
        view.setPageInfo(currentPage, totalPages, totalRows);
    }

    private void handleEmployeeSearch() {
        String keyword = view.getEmployeeSearchKeyword().toLowerCase();
        List<User> filteredEmps = allEmployees.stream()
                .filter(u -> (u.getFullName() != null && u.getFullName().toLowerCase().contains(keyword))
                || (u.getUsername() != null && u.getUsername().toLowerCase().contains(keyword)))
                .collect(Collectors.toList());

        view.setEmployeeData(filteredEmps);
        if (!filteredEmps.isEmpty() && !keyword.isEmpty()) {
            view.showEmployeePopup();
        }
    }

    private void handleDelete() {
        int id = view.getSelectedTicketId();
        if (id < 0) {
            view.showMessage("Vui lòng chọn yêu cầu cần xóa!");
            return;
        }
        if (view.showConfirm("Xóa Ticket #" + id + "?") == JOptionPane.YES_OPTION) {
            if (ticketService.deleteTicket(id)) {
                view.showMessage("✅ Đã xóa!");
                filterSupervisorTickets();
            }
        }
    }

    private void handleAssign() {
        int ticketId = view.getSelectedTicketId();
        //lấy ID của ticket  cần xử lý
        if (ticketId < 0) {
            view.showMessage("Vui lòng chọn yêu cầu!");
            return;
        }
        User selectedEmp = view.getSelectedEmployee();
        if (selectedEmp != null) {
            ticketService.assignTicket(ticketId, selectedEmp.getId(), "Giám sát phân công");
            view.showMessage("Phân công thành công!");
            filterSupervisorTickets();
        }
    }

    private void openChatDialog() {
        int ticketId = view.getSelectedTicketId();
        if (ticketId < 0) {
            view.showMessage("Vui lòng chọn ticket!");
            return;
        }

        TicketDetailDialog dlg = new TicketDetailDialog(view, ticketId);
        dlg.setChatHistory(ticketService.getHistory(ticketId));

        dlg.addSendListener(e -> {
            String msg = dlg.getMessage();
            if (!msg.isEmpty()) {
                ticketService.sendResponse(ticketId, Session.currentUser.getId(), msg);
                dlg.clearMessageInput();
                dlg.setChatHistory(ticketService.getHistory(ticketId));
                new SwingWorker<Void, Void>() {
                    protected Void doInBackground() throws Exception {
                        Thread.sleep(500);
                        ticketService.simulateAutoReply(ticketId);
                        return null;
                    }

                    protected void done() {
                        dlg.setChatHistory(ticketService.getHistory(ticketId));
                    }
                }.execute();
            }
        });

        dlg.addUpdateStatusListener(e -> {
            String note = dlg.showInput("Ghi chú cập nhật:");
            if (note != null && ticketService.updateStatus(ticketId, dlg.getSelectedStatus(), Session.currentUser.getId(), note)) {
                dlg.showMessage("Đã cập nhật!");
                dlg.setChatHistory(ticketService.getHistory(ticketId));
                filterSupervisorTickets(); // Load lại bảng
            }
        });

        dlg.addClearHistoryListener(e -> {
            if (dlg.showConfirm("Xóa sạch lịch sử?") == JOptionPane.YES_OPTION) {
                ticketService.clearHistory(ticketId);
                dlg.setChatHistory(ticketService.getHistory(ticketId));
            }
        });
        dlg.setVisible(true);
    }
}
