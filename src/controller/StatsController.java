package controller;

import service.StatsService;
import view.AdminStatsView;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class StatsController {

    private AdminStatsView view;
    private StatsService service;

    public StatsController(AdminStatsView view) {
        this.view = view;
        this.service = new StatsService();
        loadData();
        this.view.setVisible(true);
    }

    private void loadData() {
        // Lắp ráp dữ liệu cho bảng 1
        DefaultTableModel modelEmp = new DefaultTableModel(new Object[]{"Nhân viên", "Đã giao", "Đang xử lý", "Hoàn thành", "Tỉ lệ thành công", "Thời gian TB (Giờ)"}, 0);
        for (Object[] row : service.getEmployeePerformance()) {
            modelEmp.addRow(row);
        }
        view.setEmployeePerformanceModel(modelEmp);

        // Lắp ráp dữ liệu cho bảng 2
        DefaultTableModel modelTime = new DefaultTableModel(new Object[]{"Tháng / Năm", "Tổng Yêu cầu", "Đã xong"}, 0);
        for (Object[] row : service.getStatsByTime()) {
            modelTime.addRow(row);
        }
        view.setStatsByTimeModel(modelTime);

        // Lắp ráp dữ liệu cho bảng 3
        DefaultTableModel modelType = new DefaultTableModel(new Object[]{"Mức độ ưu tiên", "Số lượng", "Tỉ trọng"}, 0);
        for (Object[] row : service.getStatsByType()) {
            modelType.addRow(row);
        }
        view.setStatsByTypeModel(modelType);
    }
}
