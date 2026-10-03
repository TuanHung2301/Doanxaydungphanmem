package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import model.Service;

public class CreateTicketDialog extends JDialog {

    // 1. Khai báo đúng các nút mà Controller đang gọi
    private JRadioButton rdoOld = new JRadioButton("Khách cũ (Tìm SĐT)", true);
    private JRadioButton rdoNew = new JRadioButton("Khách mới (Tạo hồ sơ)");
    private JTextField txtPhone = new JTextField(15);
    private JButton btnSearch = new JButton("🔍 Tìm");
    private JTextField txtName = new JTextField(15);
    private JComboBox<String> cbServices = new JComboBox<>();
    private JTextField txtDetails = new JTextField();
    private JButton btnOK = new JButton("Tạo Yêu Cầu");
    private JButton btnCancel = new JButton("Hủy");

    public CreateTicketDialog(JFrame parent) {
        super(parent, "Tạo Yêu Cầu", true);
        initUI();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        // Panel chứa form, dùng GridBagLayout để chia cột cho thẳng
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Tạo khoảng cách với mép cửa sổ

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Khoảng cách giữa các ô
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlForm.add(new JLabel("Loại khách hàng:"), gbc);

        gbc.gridx = 1;
        JPanel pnlRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        ButtonGroup bg = new ButtonGroup();
        bg.add(rdoOld);
        bg.add(rdoNew);
        pnlRadio.add(rdoOld);
        pnlRadio.add(rdoNew);
        pnlForm.add(pnlRadio, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        pnlForm.add(new JLabel("Số điện thoại (*):"), gbc);

        gbc.gridx = 1;
        JPanel pnlPhone = new JPanel(new BorderLayout(5, 0));
        pnlPhone.add(txtPhone, BorderLayout.CENTER);
        pnlPhone.add(btnSearch, BorderLayout.EAST);
        pnlForm.add(pnlPhone, gbc);

        // --- HÀNG 2: HỌ TÊN ---
        gbc.gridx = 0;
        gbc.gridy = 2;
        pnlForm.add(new JLabel("Họ tên khách (*):"), gbc);

        gbc.gridx = 1;
        pnlForm.add(txtName, gbc);

        // --- HÀNG 3: DỊCH VỤ ---
        gbc.gridx = 0;
        gbc.gridy = 3;
        pnlForm.add(new JLabel("Chọn Dịch vụ:"), gbc);

        gbc.gridx = 1;
        pnlForm.add(cbServices, gbc);

        // --- HÀNG 4: NỘI DUNG ---
        gbc.gridx = 0;
        gbc.gridy = 4;
        pnlForm.add(new JLabel("Nội dung chi tiết (*):"), gbc);

        gbc.gridx = 1;
        pnlForm.add(txtDetails, gbc);
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlButtons.add(btnOK);
        pnlButtons.add(btnCancel);
        setLayout(new BorderLayout());
        add(pnlForm, BorderLayout.CENTER);
        add(pnlButtons, BorderLayout.SOUTH);

        // Đặt kích thước cố định cho các ô nhập để không bị co giãn lung tung
        txtPhone.setPreferredSize(new Dimension(150, 25));
        txtName.setPreferredSize(new Dimension(250, 25));
        txtDetails.setPreferredSize(new Dimension(250, 25));
    }

    public String getPhone() {
        return txtPhone.getText().trim();
    }

    public String getCustomerName() {
        return txtName.getText().trim();
    }

    public String getTitle() {
        return txtDetails.getText().trim();
    }

    public String getServiceName() {
        return cbServices.getSelectedItem().toString();
    }

    public boolean isNewCustomer() {
        return rdoNew.isSelected();
    }

    public void setCustomerName(String name) {
        txtName.setText(name);
    }

    public void setPhone(String p) {
        txtPhone.setText(p);
    }

    public void enableSearch(boolean b) {
        btnSearch.setEnabled(b);
    }

    public void enableName(boolean b) {
        txtName.setEditable(b);
    }

    public void loadServices(List<Service> list) {
        cbServices.removeAllItems();
        cbServices.addItem(" Khác / Không phân loại ");
        if (list != null) {
            for (Service s : list) {
                cbServices.addItem(s.getServiceName());
            }
        }
    }

    public void addRadioListener(ActionListener l) {
        rdoOld.addActionListener(l);
        rdoNew.addActionListener(l);
    }

    public void addSearchListener(ActionListener l) {
        btnSearch.addActionListener(l);
    }

    public void addSubmitListener(ActionListener l) {
        btnOK.addActionListener(l);
    }
}
