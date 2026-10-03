package controller;

import model.User;
import service.UserService;
import view.AdminUserView;
import javax.swing.JOptionPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UserController {

    private AdminUserView view;
    private UserService userService;
    private int currentPage = 1, pageSize = 10, totalPages = 1;

        public UserController(AdminUserView view) {
        this.view = view;
        this.userService = new UserService();
        initEvents();
        loadData();
        this.view.setVisible(true);
    }

    private void initEvents() {
        view.addTableMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                User selectedUser = view.getSelectedUser();
                if (selectedUser != null) {
                    view.fillForm(selectedUser);
                    view.setEditMode(true);
                }
            }
        });

        view.addResetListener(e -> {
            view.clearForm();
            view.setEditMode(false);
            currentPage = 1;
            loadData();
        });
        view.addSearchListener(e -> {
            currentPage = 1;
            loadData();
        });
        view.addPrevListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadData();
            }
        });
        view.addNextListener(e -> {
            if (currentPage < totalPages) {
                currentPage++;
                loadData();
            }
        });

   
        view.addAddListener(e -> {
            String result = userService.processAddUser(view.getUserFromForm());
            if (result.isEmpty()) {
                view.showMessage("Thêm mới thành công.");
                loadData();
                view.clearForm();
            } else {
                view.showMessage(result);
            }
        });

        view.addUpdateListener(e -> {
            int id = view.getSelectedUserId();
            if (id <= 0) {
                view.showMessage("Vui lòng chọn nhân viên!");
                return;
            }
            User u = view.getUserFromForm();
            u.setId(id);

            String result = userService.processUpdateUser(u, id);
            if (result.isEmpty()) {
                view.showMessage("Cập nhật thành công!");
                loadData();
                view.clearForm();
                view.setEditMode(false);
            } else {
                view.showMessage(result);
            }
        });

        view.addDeleteListener(e -> {
            int id = view.getSelectedUserId();
            if (id <= 0) {
                view.showMessage("Vui lòng click chọn nhân viên!");
                return;
            }
            if (JOptionPane.showConfirmDialog(null, "Xóa nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                view.showMessage(userService.processDeleteUser(id));
                loadData();
                view.clearForm();
                view.setEditMode(false);
            }
        });

        view.addResetPassListener(e -> {
            int userId = view.getSelectedUserId();
            if (userId < 0) {
                view.showMessage("️Chọn nhân viên để đổi mật khẩu!");
                return;
            }
            String newPass = view.showInput("Nhập mật khẩu mới cho [" + view.getSelectedUsername() + "]:");
            if (newPass != null) {
                String result = userService.processResetPassword(userId, newPass);
                if (result.isEmpty()) {
                    view.showMessage("Đã đổi mật khẩu!");
                    loadData();
                    view.clearForm();
                } else {
                    view.showMessage(result);
                }
            }
        });
    }

    private void loadData() {
        String kw = view.getSearchKeyword().trim();
        int totalRows = userService.getTotalUsers(kw);//thực hiện câu lệnh SQL đếm tổng số người dùng phù hợp với từ khóa tìm kiếm
        totalPages = Math.max(1, (int) Math.ceil((double) totalRows / pageSize));
        //lấy tổng số bản ghi chia cho số dòng trên một trang.
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }
        view.setTableData(userService.getUsersByPage(kw, currentPage, pageSize), currentPage, pageSize);
        //gọi xuống DAO để lấy danh sách ng dùng 
        view.setPageInfo(currentPage, totalPages, totalRows);
    }
}
