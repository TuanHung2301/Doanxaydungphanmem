package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginView() {
        setTitle("Hệ thống CSKH Keysoft");
        setSize(400, 450); // Form dọc thon gọn y như hình mẫu
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null); // Fix cứng layout để giữ đúng tỷ lệ đẹp nhất
        getContentPane().setBackground(Color.WHITE); // Nền trắng tinh

        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(52, 152, 219)); // Màu xanh dương nhạt giống hình
        pnlHeader.setBounds(0, 0, 400, 80);
        pnlHeader.setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.CENTER);
        add(pnlHeader);

        // SETTING FONT VÀ MÀU VIỀN CHUNG
        Font titleFont = new Font("Segoe UI", Font.BOLD, 12);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);
        Color borderColor = new Color(200, 200, 200); // Viền xám nhạt
        Color titleColor = new Color(80, 80, 80);     // Chữ tiêu đề xám đậm

        // ==========================================
        // 2. Ô NHẬP TÊN ĐĂNG NHẬP (Enabled)
        // ==========================================
        txtUsername = new JTextField();
        txtUsername.setFont(inputFont);
        txtUsername.setBounds(40, 110, 310, 50);
        txtUsername.setEnabled(true); // Đảm bảo ô nhập enabled
        TitledBorder userBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor),
                "Tên đăng nhập (Username)",
                TitledBorder.LEFT, TitledBorder.TOP, titleFont, titleColor);
        txtUsername.setBorder(userBorder);
        add(txtUsername);

        // ==========================================
        // 3. Ô NHẬP MẬT KHẨU (Enabled)
        // ==========================================
        txtPassword = new JPasswordField();
        txtPassword.setFont(inputFont);
        txtPassword.setBounds(40, 180, 310, 50);
        txtPassword.setEnabled(true); // Đảm bảo ô mật khẩu enabled
        TitledBorder passBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor),
                "Mật khẩu",
                TitledBorder.LEFT, TitledBorder.TOP, titleFont, titleColor);
        txtPassword.setBorder(passBorder);
        add(txtPassword);

        // ==========================================
        // 4. NÚT ĐĂNG NHẬP (MÀU ĐỎ RỰC - KHÔNG MỜ)
        // ==========================================
        btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setBounds(40, 260, 310, 50);

        // --- ĐÂY LÀ CHIÊU THỨC QUAN TRỌNG ĐỂ "KILL" CÁI MỜ MỜ DISABLED ---
        btnLogin.setEnabled(true); // Đảm bảo nút được enabled
        btnLogin.setOpaque(true);   // Bắt buộc Java vẽ màu nền
        btnLogin.setContentAreaFilled(true); // Bắt buộc Java fill màu nền

        // --- SƠN MÀU ĐỎ RỰC FLAT UI ---
        btnLogin.setBackground(new Color(231, 76, 60)); // Màu Đỏ Flat UI tuyệt đẹp
        btnLogin.setForeground(Color.WHITE);

        btnLogin.setFocusPainted(false); // Bỏ cái viền chấm chấm khi click
        btnLogin.setBorderPainted(false); // Xóa viền nút cho phẳng

        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(btnLogin);

        // 5. DÒNG NHẮC NHỞ LIÊN HỆ ADMIN
        JLabel lblHelp = new JLabel("Quên mật khẩu? Vui lòng liên hệ Admin để được cấp lại.");
        lblHelp.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblHelp.setForeground(Color.GRAY);
        lblHelp.setBounds(0, 340, 400, 30);
        lblHelp.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblHelp);
    }

    // --- CÁC HÀM LẤY DỮ LIỆU & SỰ KIỆN GIỮ NGUYÊN ---
    public String getUsername() {
        return txtUsername.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    public void addLoginListener(ActionListener al) {
        btnLogin.addActionListener(al);
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}
