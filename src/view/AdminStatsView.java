     package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminStatsView extends JFrame {

    private JTabbedPane tabs = new JTabbedPane();
    private JTable t1 = new JTable();
    private JTable t2 = new JTable();
    private JTable t3 = new JTable();

    public AdminStatsView() {
        setTitle("Báo cáo Thống kê");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(UIUtils.createHeader("TRUNG TÂM BÁO CÁO"), BorderLayout.NORTH);

        // Style các bảng
        UIUtils.styleTable(t1);
        UIUtils.styleTable(t2);
        UIUtils.styleTable(t3);

        // Style TabbedPane
        tabs.setFont(UIUtils.FONT_BOLD);
        tabs.setBackground(Color.WHITE);

        // Tạo ScrollPane nền trắng cho từng bảng
        JScrollPane s1 = new JScrollPane(t1);
        s1.getViewport().setBackground(Color.WHITE);
        JScrollPane s2 = new JScrollPane(t2);
        s2.getViewport().setBackground(Color.WHITE);
        JScrollPane s3 = new JScrollPane(t3);
        s3.getViewport().setBackground(Color.WHITE);

        tabs.add("Hiệu suất Nhân viên", s1);
        tabs.add("Theo Thời gian", s2);
        tabs.add("Theo Loại Ticket", s3);

        JPanel pnlC = new JPanel(new BorderLayout());
        pnlC.setBorder(new EmptyBorder(10, 20, 20, 20));
        pnlC.setBackground(UIUtils.CONTENT_BG);
        pnlC.add(tabs);
        add(pnlC, BorderLayout.CENTER);
    }

    public void setEmployeePerformanceModel(DefaultTableModel model) {
        t1.setModel(model);
    }

    public void setStatsByTimeModel(DefaultTableModel model) {
        t2.setModel(model);
    }

    public void setStatsByTypeModel(DefaultTableModel model) {
        t3.setModel(model);
    }
}
