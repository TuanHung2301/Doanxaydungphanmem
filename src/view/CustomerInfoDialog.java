package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CustomerInfoDialog extends JDialog {

    private JTextField txtName = new JTextField();
    private JTextField txtPhone = new JTextField();
    private JTextArea txtAddress = new JTextArea(3, 20); // Ô địa chỉ nhiều dòng
    private JButton btnClose = new JButton("ĐÓNG");

    public CustomerInfoDialog(Frame parent, int ticketId) {
        super(parent, true);
        setTitle("Hồ sơ Khách hàng");
        setSize(450, 400); // Kích thước vừa vặn
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        add(UIUtils.createHeader("THÔNG TIN KHÁCH HÀNG"), BorderLayout.NORTH);

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(new EmptyBorder(20, 30, 20, 30));

        // --- STYLE ĐỒNG BỘ CÁC Ô NHẬP ---
        UIUtils.styleTextFieldSimple(txtName);
        txtName.setEditable(false);
        txtName.setBackground(new Color(250, 250, 250));

        UIUtils.styleTextFieldSimple(txtPhone);
        txtPhone.setEditable(false);
        txtPhone.setBackground(new Color(250, 250, 250));

        // Style cho ô Địa chỉ (Bỏ khung cuộn, làm phẳng)
        txtAddress.setEditable(false);
        txtAddress.setBackground(new Color(250, 250, 250));
        txtAddress.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        txtAddress.setFont(UIUtils.FONT_NORMAL);
        txtAddress.setLineWrap(true);       // Tự xuống dòng
        txtAddress.setWrapStyleWord(true);  // Ngắt dòng theo từ cho đẹp

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 5, 0);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // --- ADD CÁC THÀNH PHẦN VÀO FORM ---
        gbc.gridy = 0;
        pnlForm.add(new JLabel("Họ và Tên:"), gbc);
        gbc.gridy = 1;
        pnlForm.add(txtName, gbc);

        gbc.gridy = 2;
        pnlForm.add(new JLabel("Số điện thoại:"), gbc);
        gbc.gridy = 3;
        pnlForm.add(txtPhone, gbc);

        gbc.gridy = 4;
        pnlForm.add(new JLabel("Địa chỉ:"), gbc);
        gbc.gridy = 5;
        // CHỐT HẠ: Add trực tiếp txtAddress, KHÔNG qua JScrollPane nữa
        pnlForm.add(txtAddress, gbc);

        add(pnlForm, BorderLayout.CENTER);

        // --- NÚT BẤM DƯỚI CÙNG ---
        JPanel pnlBot = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBot.setBackground(new Color(240, 240, 240));
        UIUtils.styleButton(btnClose, Color.GRAY);
        btnClose.setPreferredSize(new Dimension(100, 35));
        pnlBot.add(btnClose);
        add(pnlBot, BorderLayout.SOUTH);

        btnClose.addActionListener(e -> dispose());
    }

    public void setCustomerData(String[] info) {
        if (info != null && info.length >= 3) {
            txtName.setText(info[0]);

            // Fix index để bỏ qua Email (vốn ở vị trí số 1)
            // Lấy SĐT ở vị trí số 2 và Địa chỉ ở vị trí số 3
            if (info.length > 2) {
                txtPhone.setText(info[2]);
            }
            if (info.length > 3) {
                txtAddress.setText(info[3]);
            }
        } else {
            txtName.setText("Không tìm thấy thông tin");
        }
    }
}
