package view;

import model.Ticket;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;

public class EmployeeView extends JFrame {

    private JTable tblTickets = new JTable();

    // Khởi tạo cấu trúc bảng gồm 8 cột (7 cột hiển thị và 1 cột ID ẩn)
    private DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"STT", "Dịch vụ", "Nội dung", "Khách hàng", "NV Hiện tại", "Trạng thái", "Ngày tạo", "ID_AN"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Ngăn chặn người dùng chỉnh sửa trực tiếp trên ô của bảng
        }
    };

    private JComboBox<String> cbFilter = new JComboBox<>(new String[]{"Việc của tôi", "Tất cả công việc"});
    private JTextField txtSearch = new JTextField(20);
    private JButton btnSearch = new JButton("Tìm kiếm");

    private JButton btnRefresh = new JButton("LÀM MỚI");
    private JButton btnCreate = new JButton("TẠO YÊU CẦU");
    private JButton btnEdit = new JButton("SỬA NỘI DUNG");
    private JButton btnInfo = new JButton("THÔNG TIN KH");
    private JButton btnWork = new JButton("XỬ LÝ / CHAT");

    private JButton btnPrev = new JButton("< Trước");
    private JButton btnNext = new JButton("Sau >");
    private JLabel lblPageInfo = new JLabel("Trang 1 / 1");

    public EmployeeView() {
        setTitle("Nhân viên - Xử lý Yêu cầu");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(UIUtils.createHeader("KHU VỰC TIẾP NHẬN & XỬ LÝ YÊU CẦU"), BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));
        pnlCenter.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnlCenter.setBackground(UIUtils.CONTENT_BG);

        // --- KHU VỰC LỌC & TÌM KIẾM ---
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlTop.setBackground(UIUtils.CONTENT_BG);
        pnlTop.add(new JLabel("Hiển thị:"));
        pnlTop.add(cbFilter);
        pnlTop.add(new JLabel(" Tìm kiếm:"));
        UIUtils.styleTextFieldSimple(txtSearch);
        pnlTop.add(txtSearch);
        UIUtils.styleButton(btnSearch, Color.GRAY);
        pnlTop.add(btnSearch);

        // --- CẤU HÌNH BẢNG DỮ LIỆU CHỐNG CẮT CHỮ (...) ---
        UIUtils.styleTable(tblTickets);
        tblTickets.setModel(tableModel);
        tblTickets.setRowSelectionAllowed(true);
        tblTickets.setRowHeight(30); // Chiều cao tiêu chuẩn cho mỗi hàng

     
        tblTickets.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Phân bổ kích thước pixel cụ thể cho từng cột hiển thị để đảm bảo chứa đủ dữ liệu full text
        tblTickets.getColumnModel().getColumn(0).setPreferredWidth(50);  // STT
        tblTickets.getColumnModel().getColumn(1).setPreferredWidth(160); // Dịch vụ
        tblTickets.getColumnModel().getColumn(2).setPreferredWidth(500); // Nội dung (Rộng tối đa để hiển thị trọn vẹn câu dài)
        tblTickets.getColumnModel().getColumn(3).setPreferredWidth(150); // Khách hàng
        tblTickets.getColumnModel().getColumn(4).setPreferredWidth(150); // Nhân viên hiện tại
        tblTickets.getColumnModel().getColumn(5).setPreferredWidth(100); // Trạng thái
        tblTickets.getColumnModel().getColumn(6).setPreferredWidth(180); // Ngày tạo (Đủ khoảng trống hiển thị cả năm-tháng-ngày giờ:phút:giây)

        // Cấu hình ẩn cột ID (Cột số 7) phục vụ logic ngầm
        tblTickets.getColumnModel().getColumn(7).setMinWidth(0);
        tblTickets.getColumnModel().getColumn(7).setMaxWidth(0);
        tblTickets.getColumnModel().getColumn(7).setWidth(0);

        // --- KHU VỰC PHÂN TRANG ---
        JPanel pnlPaging = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        pnlPaging.setBackground(UIUtils.CONTENT_BG);
        pnlPaging.add(btnPrev);
        pnlPaging.add(lblPageInfo);
        pnlPaging.add(btnNext);

        JPanel pnlTableWrapper = new JPanel(new BorderLayout());
        pnlTableWrapper.add(pnlTop, BorderLayout.NORTH);
        pnlTableWrapper.add(new JScrollPane(tblTickets), BorderLayout.CENTER);
        pnlTableWrapper.add(pnlPaging, BorderLayout.SOUTH);
        pnlCenter.add(pnlTableWrapper, BorderLayout.CENTER);

        // --- KHU VỰC CHỨC NĂNG (NÚT BẤM) ---
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlBottom.setBackground(Color.WHITE);
        pnlBottom.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        UIUtils.styleButton(btnRefresh, Color.GRAY);
        UIUtils.styleButton(btnCreate, UIUtils.SUCCESS_COLOR);
        UIUtils.styleButton(btnEdit, UIUtils.WARNING_COLOR);
        btnEdit.setForeground(Color.BLACK);
        UIUtils.styleButton(btnInfo, UIUtils.INFO_COLOR);
        UIUtils.styleButton(btnWork, new Color(138, 43, 226));

        pnlBottom.add(btnRefresh);
        pnlBottom.add(btnCreate);
        pnlBottom.add(btnEdit);
        pnlBottom.add(btnInfo);
        pnlBottom.add(btnWork);

        pnlCenter.add(pnlBottom, BorderLayout.SOUTH);
        add(pnlCenter, BorderLayout.CENTER);
    }


     //* Đổ dữ liệu từ tầng Controller vào TableModel và bóc tách chuỗi tiêu đề
 
    public void setTableData(List<Ticket> list, int currentPage, int pageSize) {
        tableModel.setRowCount(0);
        for (int i = 0; i < list.size(); i++) {
            Ticket t = list.get(i);
            int stt = (currentPage - 1) * pageSize + i + 1;

            String fullTitle = t.getTitle() != null ? t.getTitle() : "";
            String serviceName = "Khác";
            String content = fullTitle;

            // Xử lý bóc tách chuỗi dạng [Tên dịch vụ] Nội dung yêu cầu
            if (fullTitle.startsWith("[") && fullTitle.contains("]")) {
                int closeIndex = fullTitle.indexOf("]");
                serviceName = fullTitle.substring(1, closeIndex);
                content = fullTitle.substring(closeIndex + 1).trim();
            }

            // Đưa dữ liệu đã bóc tách vào cấu trúc bảng 8 cột
            tableModel.addRow(new Object[]{
                stt, serviceName, content, t.getCustomerName(), t.getAssignedName(), t.getStatus(), t.getCreatedAt(), t.getId()
            });
        }
    }

    public void setPageInfo(int current, int total, int totalRows) {
        lblPageInfo.setText("Trang " + current + " / " + total + " (Tổng: " + totalRows + ")");
        btnPrev.setEnabled(current > 1);
        btnNext.setEnabled(current < total);
    }

    // Lấy ID ẩn của Ticket từ cột số 7
    public int getSelectedTicketId() {
        int r = tblTickets.getSelectedRow();
        return r < 0 ? -1 : Integer.parseInt(tblTickets.getValueAt(r, 7).toString());
    }

    // Lấy Nội dung chi tiết của Ticket từ cột số 2
    public String getSelectedTitle() {
        int r = tblTickets.getSelectedRow();
        return r < 0 ? "" : tblTickets.getValueAt(r, 2).toString();
    }

    public int getFilterIndex() {
        return cbFilter.getSelectedIndex();
    }

    public String getSearchKeyword() {
        return txtSearch.getText().trim();
    }

    public void clearSearch() {
        txtSearch.setText("");
    }

    public void addFilterListener(ActionListener al) {
        cbFilter.addActionListener(al);
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

    public void addRefreshListener(ActionListener al) {
        btnRefresh.addActionListener(al);
    }

    public void addCreateListener(ActionListener al) {
        btnCreate.addActionListener(al);
    }

    public void addEditListener(ActionListener al) {
        btnEdit.addActionListener(al);
    }

    public void addInfoListener(ActionListener al) {
        btnInfo.addActionListener(al);
    }

    public void addWorkListener(ActionListener al) {
        btnWork.addActionListener(al);
    }

    public void addTableMouseListener(MouseAdapter ma) {
        tblTickets.addMouseListener(ma);
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public String showInput(String msg) {
        return JOptionPane.showInputDialog(this, msg);
    }

    public String showInput(String msg, String defaultText) {
        return (String) JOptionPane.showInputDialog(this, msg, "Nhập liệu", JOptionPane.PLAIN_MESSAGE, null, null, defaultText);
    }
}
