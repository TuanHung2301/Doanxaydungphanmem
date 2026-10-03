package controller;

import model.Service;
import service.ServiceManagementService;
import view.ServiceManagerView;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

public class ServiceManagerController {

    private ServiceManagerView view;
    private ServiceManagementService service;
    private int currentPage = 1, pageSize = 10, totalPages = 1;

    public ServiceManagerController(ServiceManagerView view) {
        this.view = view;
        this.service = new ServiceManagementService();
        initController();
        loadData();
        this.view.setVisible(true);
    }

    private void initController() {
        view.addTableMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Service s = view.getSelectedService();
                if (s != null) {
                    view.fillForm(s);
                    view.setEditMode(true);
                }
            }
        });

        view.addResetListener(e -> {
            view.clearForm();
            currentPage = 1;
            loadData();
            view.setEditMode(false);
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
            String result = service.processAddService(view.getServiceFromForm());
            if (result.isEmpty()) {
                view.showMessage("Thêm thành công!");
                view.clearForm();
                loadData();
                view.setEditMode(false);
            } else {
                view.showMessage(result);
            }
        });

        view.addUpdateListener(e -> {
            Service selected = view.getSelectedService();
            if (selected == null) {
                view.showMessage("️Vui lòng click chọn dịch vụ dưới bảng để cập nhật!");
                return;
            }

            String result = service.processUpdateService(view.getServiceFromForm(), selected);
            if (result.isEmpty()) {
                view.showMessage("Cập nhật thành công!");
                view.clearForm();
                loadData();
                view.setEditMode(false);
            } else {
                view.showMessage(result);
            }
        });

        view.addDeleteListener(e -> {
            Service selected = view.getSelectedService();
            if (selected == null) {
                view.showMessage("️ Chưa chọn dòng để xóa!");
                return;
            }

            if (JOptionPane.showConfirmDialog(view, "Xóa dịch vụ: " + selected.getServiceName() + "?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                String result = service.processDeleteService(selected.getId());
                if (result.isEmpty()) {
                    view.showMessage(" Đã xóa dịch vụ thành công!");
                    view.clearForm();
                    loadData();
                    view.setEditMode(false);
                } else {
                    view.showMessage(result);
                }
            }
        });
    }

    private void loadData() {
        String kw = view.getSearchKeyword().trim();
        int totalRows = service.getTotalServices(kw);
        totalPages = Math.max(1, (int) Math.ceil((double) totalRows / pageSize));
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        view.setTableData(service.getServicesByPage(kw, currentPage, pageSize), currentPage, pageSize);
        view.setPageInfo(currentPage, totalPages, totalRows);
    }
}
