package controller;

import DB.Session;
import view.*;
import javax.swing.JOptionPane;

public class MainController {

    private MainView view;

    public MainController(MainView view) {
        this.view = view;
        initController();
        this.view.setVisible(true);
    }

    private void initController() {// bat su kien = cac nut bam
        view.addDashboardListener(e -> view.showDashboardHome());
        view.addUsersListener(e -> new UserController(new AdminUserView()));
      //khởi tạo AdminUserView và chuyển quyền xử lý sang UserController
        view.addServicesListener(e -> new ServiceManagerController(new ServiceManagerView()));
        view.addTicketsListener(e -> new TicketManagementController(new SupervisorView()));
        view.addEmployeeListener(e -> new TicketController(new EmployeeView()));

        view.addLogoutListener(e -> {
            if (JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                Session.currentUser = null;
                for (java.awt.Window window : java.awt.Window.getWindows()) {
                    if (window != null) {
                        window.dispose();
                    }
                }
                new AuthController(new LoginView());
            }
        });
    }
}
