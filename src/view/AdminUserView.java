package view;

import model.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;

public class AdminUserView extends JFrame {

    private JTable tblUsers = new JTable();

    // ĐÃ FIX 1: Khóa bảng không cho phép gõ chữ trực tiếp
    private DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"STT", "Tài khoản", "Họ tên", "Chức vụ", "Trạng thái", "ID_AN"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private JTextField txtUser = new JTextField();
    private JTextField txtName = new JTextField();
    private JPasswordField txtPass = new JPasswordField();
    private JComboBox<String> cbRole = new JComboBox<>(new String[]{"ADMIN", "SUPERVISOR", "EMPLOYEE"});
    private JComboBox<String> cbStatus = new JComboBox<>(new String[]{"ACTIVE", "INACTIVE"});

    private JButton btnAdd = new JButton("THÊM MỚI");
    private JButton btnUpd = new JButton("CẬP NHẬT");
    private JButton btnDel = new JButton("XÓA / KHÓA");
    private JButton btnReset = new JButton("LÀM MỚI");
    private JButton btnResetPass = new JButton("ĐỔI MẬT KHẨU"); // Nút Đổi pass

    private JTextField txtSearch = new JTextField(20);
    private JButton btnSearch = new JButton("Tìm kiếm");
    private JButton btnPrev = new JButton("< Trước");
    private JButton btnNext = new JButton("Sau >");
    private JLabel lblPageInfo = new JLabel("Trang 1 / 1");

    public AdminUserView() {
        setTitle("Quản lý Nhân sự");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(UIUtils.createHeader("QUẢN LÝ TÀI KHOẢN HỆ THỐNG"), BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));
        pnlCenter.setBackground(UIUtils.CONTENT_BG);
        pnlCenter.setBorder(new EmptyBorder(10, 20, 20, 20));

        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1), new EmptyBorder(20, 40, 20, 40)));

        UIUtils.styleTextFieldSimple(txtUser);
        UIUtils.styleTextFieldSimple(txtName);
        UIUtils.styleTextFieldSimple(txtPass);
        cbRole.setBackground(Color.WHITE);
        cbStatus.setBackground(Color.WHITE);
        cbRole.setPreferredSize(new Dimension(200, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlInput.add(new JLabel("Tài Khoản (*):"), gbc);
        gbc.gridx = 1;
        pnlInput.add(txtUser, gbc);
        gbc.gridx = 2;
        pnlInput.add(new JLabel("Mật Khẩu (*):"), gbc);
        gbc.gridx = 3;
        pnlInput.add(txtPass, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        pnlInput.add(new JLabel("Họ và Tên (*) :"), gbc);
        gbc.gridx = 1;
        pnlInput.add(txtName, gbc);
        gbc.gridx = 2;
        pnlInput.add(new JLabel("Chức Vụ:"), gbc);
        gbc.gridx = 3;
        pnlInput.add(cbRole, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        pnlInput.add(new JLabel("Trạng Thái:"), gbc);
        gbc.gridx = 1;
        pnlInput.add(cbStatus, gbc);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlButtons.setBackground(UIUtils.CONTENT_BG);

        UIUtils.styleButton(btnAdd, UIUtils.SUCCESS_COLOR);
        UIUtils.styleButton(btnUpd, UIUtils.WARNING_COLOR);
        btnUpd.setForeground(Color.BLACK);
        UIUtils.styleButton(btnDel, UIUtils.DANGER_COLOR);
        UIUtils.styleButton(btnReset, UIUtils.INFO_COLOR);
        UIUtils.styleButton(btnResetPass, Color.DARK_GRAY);
        btnResetPass.setForeground(Color.WHITE);

        Dimension btnSize = new Dimension(180, 40);
        btnAdd.setPreferredSize(btnSize);
        btnUpd.setPreferredSize(btnSize);
        btnDel.setPreferredSize(btnSize);
        btnReset.setPreferredSize(btnSize);
        btnResetPass.setPreferredSize(btnSize);

        pnlButtons.add(btnAdd);
        pnlButtons.add(btnUpd);
        pnlButtons.add(btnDel);
        pnlButtons.add(btnReset);
        pnlButtons.add(btnResetPass);

        JPanel pnlTopWrapper = new JPanel(new BorderLayout());
        pnlTopWrapper.setBackground(UIUtils.CONTENT_BG);
        pnlTopWrapper.add(pnlInput, BorderLayout.CENTER);
        pnlTopWrapper.add(pnlButtons, BorderLayout.SOUTH);
        pnlCenter.add(pnlTopWrapper, BorderLayout.NORTH);

        JPanel pnlTableSection = new JPanel(new BorderLayout(0, 10));
        pnlTableSection.setBackground(UIUtils.CONTENT_BG);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSearch.setBackground(UIUtils.CONTENT_BG);
        UIUtils.styleTextFieldSimple(txtSearch);
        txtSearch.setColumns(20);
        UIUtils.styleButton(btnSearch, Color.GRAY);
        btnSearch.setPreferredSize(new Dimension(140, 30));
        pnlSearch.add(new JLabel("Tìm kiếm: "));
        pnlSearch.add(txtSearch);
        pnlSearch.add(btnSearch);

        UIUtils.styleTable(tblUsers);
        tblUsers.setModel(tableModel);
        tblUsers.getColumnModel().getColumn(5).setMinWidth(0);
        tblUsers.getColumnModel().getColumn(5).setMaxWidth(0);
        tblUsers.getColumnModel().getColumn(0).setMaxWidth(50);

        JScrollPane scroll = new JScrollPane(tblUsers);
        scroll.getViewport().setBackground(Color.WHITE);

        JPanel pnlPaging = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        pnlPaging.setBackground(UIUtils.CONTENT_BG);
        pnlPaging.add(btnPrev);
        pnlPaging.add(lblPageInfo);
        pnlPaging.add(btnNext);

        pnlTableSection.add(pnlSearch, BorderLayout.NORTH);
        pnlTableSection.add(scroll, BorderLayout.CENTER);
        pnlTableSection.add(pnlPaging, BorderLayout.SOUTH);

        pnlCenter.add(pnlTableSection, BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        setEditMode(false);
    }

    // ĐÃ FIX 2: Bọc thép lấy dữ liệu từ Form (Chống Null ComboBox)
    public User getUserFromForm() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();
        String name = txtName.getText().trim();

        Object roleObj = cbRole.getSelectedItem();
        String role = (roleObj != null) ? roleObj.toString() : "EMPLOYEE";

        Object statusObj = cbStatus.getSelectedItem();
        String status = (statusObj != null) ? statusObj.toString() : "ACTIVE";

        return new User(0, user, pass, name, role, status);
    }

    public String getSearchKeyword() {
        return txtSearch.getText().trim();
    }

    // ĐÃ FIX 3: Hiển thị Dữ liệu lên bảng (Lấp chỗ trống mượt mà)
    public void setTableData(List<User> list, int currentPage, int pageSize) {
        tableModel.setRowCount(0);
        for (int i = 0; i < list.size(); i++) {
            User u = list.get(i);

            String userDisplay = (u.getUsername() == null || u.getUsername().trim().isEmpty()) ? "(Trống)" : u.getUsername();
            String nameDisplay = (u.getFullName() == null || u.getFullName().trim().isEmpty()) ? "(Trống)" : u.getFullName();
            String roleDisplay = (u.getRole() == null || u.getRole().trim().isEmpty()) ? "EMPLOYEE" : u.getRole();
            String statusDisplay = (u.getStatus() == null || u.getStatus().trim().isEmpty()) ? "ACTIVE" : u.getStatus();

            tableModel.addRow(new Object[]{
                (currentPage - 1) * pageSize + i + 1,
                userDisplay, nameDisplay, roleDisplay, statusDisplay, u.getId()
            });
        }
    }

    public void setPageInfo(int current, int total, int totalRows) {
        lblPageInfo.setText("Trang " + current + " / " + total + " (Tổng: " + totalRows + ")");
        btnPrev.setEnabled(current > 1);
        btnNext.setEnabled(current < total);
    }

    // ĐÃ FIX 4: Lấy dữ liệu từ Bảng (Chống Null Pointer)
    public User getSelectedUser() {
        int r = tblUsers.getSelectedRow();
        if (r < 0) {
            return null;
        }

        User u = new User();
        Object userObj = tblUsers.getValueAt(r, 1);
        u.setUsername(userObj == null ? "" : userObj.toString());
        Object nameObj = tblUsers.getValueAt(r, 2);
        u.setFullName(nameObj == null ? "" : nameObj.toString());
        Object roleObj = tblUsers.getValueAt(r, 3);
        u.setRole(roleObj == null ? "" : roleObj.toString());
        Object statusObj = tblUsers.getValueAt(r, 4);
        u.setStatus(statusObj == null ? "" : statusObj.toString());

        try {
            Object idObj = tblUsers.getValueAt(r, 5);
            if (idObj != null && !idObj.toString().trim().isEmpty()) {
                u.setId(Integer.parseInt(idObj.toString()));
            }
        } catch (Exception e) {
        }

        return u;
    }

    public void fillForm(User u) {
        if (u == null) {
            return;
        }
        txtUser.setText(u.getUsername());
        txtName.setText(u.getFullName());
        cbRole.setSelectedItem(u.getRole());
        cbStatus.setSelectedItem(u.getStatus());
        txtPass.setText("");

        // 🛠️ BỔ SUNG: Khóa mờ hẳn ô mật khẩu đi khi click vào dòng dưới bảng để sửa thông tin
        txtPass.setEnabled(false);
    }

    public void clearForm() {
        txtUser.setText("");
        txtName.setText("");
        txtPass.setText("");
        txtSearch.setText("");
        if (cbRole.getItemCount() > 0) {
            cbRole.setSelectedIndex(0);
        }
        if (cbStatus.getItemCount() > 0) {
            cbStatus.setSelectedIndex(0);
        }
        tblUsers.clearSelection();

        // 🛠️ BỔ SUNG: Mở sáng lại ô mật khẩu khi bấm nút "Làm mới" để chuẩn bị thêm người mới
        txtPass.setEnabled(true);
    }

    public void setEditMode(boolean isEdit) {
        btnAdd.setEnabled(!isEdit);
        btnUpd.setEnabled(isEdit);
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public String showInput(String msg) {
        return JOptionPane.showInputDialog(this, msg);
    }

    // --- CÁC SỰ KIỆN ---
    public void addAddListener(ActionListener al) {
        btnAdd.addActionListener(al);
    }

    public void addUpdateListener(ActionListener al) {
        btnUpd.addActionListener(al);
    }

    public void addDeleteListener(ActionListener al) {
        btnDel.addActionListener(al);
    }

    public void addResetListener(ActionListener al) {
        btnReset.addActionListener(al);
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

    public void addTableMouseListener(MouseAdapter ma) {
        tblUsers.addMouseListener(ma);
    }

    public void addResetPassListener(ActionListener al) {
        btnResetPass.addActionListener(al);
    }

    // --- HÀM TIỆN ÍCH ---
    public int getSelectedUserId() {
        User u = getSelectedUser();
        return u != null ? u.getId() : -1;
    }

    public String getSelectedUsername() {
        User u = getSelectedUser();
        return u != null ? u.getUsername() : "";
    }
}
