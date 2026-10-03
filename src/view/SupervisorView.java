package view;

import model.Ticket;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;

public class SupervisorView extends JFrame {

    private JTable tblTickets = new JTable();
    private DefaultTableModel ticketModel = new DefaultTableModel(new Object[]{"STT", "Tiêu đề", "Khách hàng", "NV Hiện tại", "Trạng thái", "Ngày tạo", "ID_AN"}, 0);

    private JTextField txtSearchEmp = new JTextField(12);
    private JComboBox<User> cbEmployees = new JComboBox<>();

    private JButton btnDetail = new JButton("XEM CHI TIẾT");
    private JButton btnAssign = new JButton("PHÂN CÔNG");
    private JButton btnDelete = new JButton("XÓA YÊU CẦU");
    private JButton btnRefresh = new JButton("LÀM MỚI");

    private JTextField txtSearch = new JTextField(25);
    private JButton btnSearch = new JButton("Tìm kiếm");
    private JButton btnPrev = new JButton("< Trước");
    private JButton btnNext = new JButton("Sau >");
    private JLabel lblPageInfo = new JLabel("Trang 1 / 1");

    private JTable tblStatEmp = new JTable();
    private JTable tblStatTime = new JTable();
    private JTable tblStatType = new JTable();

    public SupervisorView() {
        setTitle("Giám sát viên - Điều phối & Báo cáo");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(UIUtils.createHeader("KHU VỰC GIÁM SÁT & BÁO CÁO"), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabs.addTab("ĐIỀU PHỐI & PHÂN CÔNG", createAssignPanel());
        tabs.addTab("BÁO CÁO TỔNG HỢP", createReportPanel());

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel createAssignPanel() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnl.setBackground(UIUtils.CONTENT_BG);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSearch.setBackground(UIUtils.CONTENT_BG);
        UIUtils.styleTextFieldSimple(txtSearch);
        UIUtils.styleButton(btnSearch, Color.GRAY);
        pnlSearch.add(new JLabel("🔍 Nhập Tên NV, Khách hàng hoặc Tiêu đề: "));
        pnlSearch.add(txtSearch);
        pnlSearch.add(btnSearch);

        UIUtils.styleTable(tblTickets);
        tblTickets.setModel(ticketModel);

        tblTickets.getColumnModel().getColumn(0).setMaxWidth(50);
        tblTickets.getColumnModel().getColumn(6).setMinWidth(0);
        tblTickets.getColumnModel().getColumn(6).setMaxWidth(0);
        tblTickets.getColumnModel().getColumn(6).setWidth(0);

        JPanel pnlPaging = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        pnlPaging.setBackground(UIUtils.CONTENT_BG);
        pnlPaging.add(btnPrev);
        pnlPaging.add(lblPageInfo);
        pnlPaging.add(btnNext);

        JPanel pnlTableWrapper = new JPanel(new BorderLayout());
        pnlTableWrapper.add(pnlSearch, BorderLayout.NORTH);
        pnlTableWrapper.add(new JScrollPane(tblTickets), BorderLayout.CENTER);
        pnlTableWrapper.add(pnlPaging, BorderLayout.SOUTH);
        pnl.add(pnlTableWrapper, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlBottom.setBackground(Color.WHITE);
        pnlBottom.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        UIUtils.styleButton(btnDetail, UIUtils.INFO_COLOR);
        UIUtils.styleButton(btnAssign, UIUtils.SUCCESS_COLOR);
        UIUtils.styleButton(btnDelete, UIUtils.DANGER_COLOR);
        UIUtils.styleButton(btnRefresh, Color.GRAY);

        pnlBottom.add(btnRefresh);
        pnlBottom.add(btnDetail);
        pnlBottom.add(new JLabel(" | 🔍 Tìm NV: "));
        UIUtils.styleTextFieldSimple(txtSearchEmp);
        pnlBottom.add(txtSearchEmp);
        pnlBottom.add(cbEmployees);
        pnlBottom.add(btnAssign);
        pnlBottom.add(new JLabel(" | "));
        pnlBottom.add(btnDelete);

        pnl.add(pnlBottom, BorderLayout.SOUTH);
        return pnl;
    }

    private JPanel createReportPanel() {
        JPanel pnl = new JPanel(new BorderLayout());
        JTabbedPane subTabs = new JTabbedPane();
        subTabs.setFont(new Font("Segoe UI", Font.BOLD, 12));

        UIUtils.styleTable(tblStatEmp);
        UIUtils.styleTable(tblStatTime);
        UIUtils.styleTable(tblStatType);

        subTabs.addTab("HIỆU SUẤT NHÂN VIÊN", new JScrollPane(tblStatEmp));
        subTabs.addTab("THEO THỜI GIAN", new JScrollPane(tblStatTime));
        subTabs.addTab("THEO LOẠI YÊU CẦU", new JScrollPane(tblStatType));

        pnl.add(subTabs, BorderLayout.CENTER);
        return pnl;
    }

    public void setTicketData(List<Ticket> list) {
        ticketModel.setRowCount(0);
        for (int i = 0; i < list.size(); i++) {
            Ticket t = list.get(i);
            ticketModel.addRow(new Object[]{
                (i + 1), t.getTitle(), t.getCustomerName(), t.getAssignedName(), t.getStatus(), t.getCreatedAt(), t.getId()
            });
        }
    }

    public void setTicketData(List<Ticket> list, int currentPage, int pageSize) {
        ticketModel.setRowCount(0);
        for (int i = 0; i < list.size(); i++) {
            Ticket t = list.get(i);
            int stt = (currentPage - 1) * pageSize + i + 1;

            ticketModel.addRow(new Object[]{
                stt, t.getTitle(), t.getCustomerName(), t.getAssignedName(), t.getStatus(), t.getCreatedAt(), t.getId()
            });
        }
    }

    public void setEmployeeData(List<User> emps) {
        cbEmployees.removeAllItems();
        for (User u : emps) {
            cbEmployees.addItem(u);
        }
    }

    public void setStatsModels(DefaultTableModel mEmp, DefaultTableModel mTime, DefaultTableModel mType) {
        tblStatEmp.setModel(mEmp);
        tblStatTime.setModel(mTime);
        tblStatType.setModel(mType);
    }

    public void setPageInfo(int current, int total, int totalRows) {
        lblPageInfo.setText("Trang " + current + " / " + total + " (Tổng: " + totalRows + ")");
        btnPrev.setEnabled(current > 1);
        btnNext.setEnabled(current < total);
    }

    public int getSelectedTicketId() {
        int r = tblTickets.getSelectedRow();
        return r < 0 ? -1 : Integer.parseInt(tblTickets.getValueAt(r, 6).toString());
    }

    public User getSelectedEmployee() {
        return (User) cbEmployees.getSelectedItem();
    }

    public String getSearchKeyword() {
        return txtSearch.getText().trim();
    }

    public void clearSearch() {
        txtSearch.setText("");
        txtSearchEmp.setText("");
    }

    public void addSearchListener(ActionListener al) {
        btnSearch.addActionListener(al);
        txtSearch.addActionListener(al);
    }

    public void addPrevListener(ActionListener al) {
        btnPrev.addActionListener(al);
    }

    public void addNextListener(ActionListener al) {
        btnNext.addActionListener(al);
    }

    public String getEmployeeSearchKeyword() {
        return txtSearchEmp.getText().trim();
    }

    public void addEmployeeSearchListener(java.awt.event.KeyListener kl) {
        txtSearchEmp.addKeyListener(kl);
    }

    public void showEmployeePopup() {
        cbEmployees.setPopupVisible(true);
    }

    public void addRefreshListener(ActionListener al) {
        btnRefresh.addActionListener(al);
    }

    public void addDetailListener(ActionListener al) {
        btnDetail.addActionListener(al);
    }

    public void addDeleteListener(ActionListener al) {
        btnDelete.addActionListener(al);
    }

    public void addAssignListener(ActionListener al) {
        btnAssign.addActionListener(al);
    }

    public void addTableMouseListener(MouseAdapter ma) {
        tblTickets.addMouseListener(ma);
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public int showConfirm(String msg) {
        return JOptionPane.showConfirmDialog(this, msg, "Xác nhận", JOptionPane.YES_NO_OPTION);
    }
}
