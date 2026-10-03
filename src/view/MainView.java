package view;

import DB.Session;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainView extends JFrame {

    // Các nút menu ở thanh Sidebar
    private JButton btnDashboard = UIUtils.createMenuButton("TRANG CHỦ");
    private JButton btnUsers = UIUtils.createMenuButton("QUẢN LÝ NGƯỜI DÙNG");
    private JButton btnServices = UIUtils.createMenuButton("QUẢN LÝ DỊCH VỤ");
    private JButton btnTickets = UIUtils.createMenuButton("GIÁM SÁT & BÁO CÁO");
    private JButton btnEmployee = UIUtils.createMenuButton("BÀN LÀM VIỆC (NV)");
    private JButton btnLogout = UIUtils.createMenuButton("ĐĂNG XUẤT");

    private JPanel pnlContent = new JPanel(new BorderLayout());

    public MainView() {
        setTitle("Hệ thống CSKH - KeySoft");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- SIDEBAR BÊN TRÁI ---
        JPanel pnlSidebar = new JPanel();
        pnlSidebar.setLayout(new BoxLayout(pnlSidebar, BoxLayout.Y_AXIS));
        pnlSidebar.setBackground(UIUtils.SIDEBAR_BG);
        pnlSidebar.setPreferredSize(new Dimension(320, 0));

        JLabel lblLogo = new JLabel("CSKH KEYSOFT", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setBorder(new EmptyBorder(30, 0, 30, 0));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlSidebar.add(lblLogo);
        pnlSidebar.add(btnDashboard);
        pnlSidebar.add(btnUsers);
        pnlSidebar.add(btnServices);
        pnlSidebar.add(btnTickets);
        pnlSidebar.add(btnEmployee);
        pnlSidebar.add(Box.createVerticalGlue()); // Đẩy nút Đăng xuất xuống đáy
        pnlSidebar.add(btnLogout);

        btnLogout.setBackground(new Color(221, 75, 57));
        btnLogout.setForeground(Color.WHITE);

        add(pnlSidebar, BorderLayout.WEST);

        // --- KHU VỰC NỘI DUNG CHÍNH ---
        pnlContent.setBackground(UIUtils.CONTENT_BG);
        add(pnlContent, BorderLayout.CENTER);

        showDashboardHome();
        applyRoles();
    }

    // --- GIAO DIỆN TRANG CHỦ MỚI (TỐI GIẢN & HIỆN ĐẠI) ---
    public void showDashboardHome() {
        pnlContent.removeAll();
        pnlContent.setLayout(new BorderLayout());

        // 1. Header: Thanh lời chào
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(new EmptyBorder(20, 30, 20, 30));

        String role = (Session.currentUser != null) ? Session.currentUser.getRole() : "GUEST";
        String name = (Session.currentUser != null) ? Session.currentUser.getFullName() : "User";

        JLabel lblHello = new JLabel("Xin chào, " + name);
        lblHello.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHello.setForeground(new Color(44, 62, 80));

        JLabel lblRole = new JLabel("Vai trò: " + role);
        lblRole.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblRole.setForeground(Color.GRAY);

        pnlHeader.add(lblHello, BorderLayout.WEST);
        pnlHeader.add(lblRole, BorderLayout.EAST);
        pnlContent.add(pnlHeader, BorderLayout.NORTH);

        // 2. Body: Hiển thị dòng chữ to ở chính giữa
        JPanel pnlBody = new JPanel(new GridBagLayout());
        pnlBody.setBackground(UIUtils.CONTENT_BG);

        JLabel lblWatermark = new JLabel("HỆ THỐNG QUẢN LÝ CSKH KEYSOFT");
        lblWatermark.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblWatermark.setForeground(new Color(52, 152, 219)); // Màu xanh dương sáng đẹp mắt

        pnlBody.add(lblWatermark);

        pnlContent.add(pnlBody, BorderLayout.CENTER);
        pnlContent.revalidate();
        pnlContent.repaint();
    }

    // --- PHÂN QUYỀN ẨN/HIỆN NÚT ---
    private void applyRoles() {
        if (Session.currentUser == null) {
            return;
        }
        String r = Session.currentUser.getRole();
        boolean isAdmin = "ADMIN".equals(r);
        boolean isSup = "SUPERVISOR".equals(r);

        btnUsers.setVisible(isAdmin);
        btnServices.setVisible(isAdmin);
        btnTickets.setVisible(isAdmin || isSup);
        btnEmployee.setVisible("EMPLOYEE".equals(r));
    }

    // --- CÁC HÀM LẮNG NGHE SỰ KIỆN CHO CONTROLLER ---
    public void addDashboardListener(ActionListener al) {
        btnDashboard.addActionListener(al);
    }

    public void addUsersListener(ActionListener al) {
        btnUsers.addActionListener(al);
    }

    public void addServicesListener(ActionListener al) {
        btnServices.addActionListener(al);
    }

    public void addTicketsListener(ActionListener al) {
        btnTickets.addActionListener(al);
    }

    public void addEmployeeListener(ActionListener al) {
        btnEmployee.addActionListener(al);
    }

    public void addLogoutListener(ActionListener al) {
        btnLogout.addActionListener(al);
    }
}
