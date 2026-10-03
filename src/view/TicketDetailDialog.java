package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class TicketDetailDialog extends JDialog {

    private JTextArea txtHistory = new JTextArea();
    private JTextField txtMsg = new JTextField();

    private JButton btnSend = new JButton("Phản hồi KH");
    private JButton btnClear = new JButton("Xóa lịch sử");

    private JComboBox<String> cbStatus = new JComboBox<>(new String[]{"OPEN", "PROCESSING", "RESOLVED", "CLOSED"});
    private JButton btnUpdate = new JButton("CẬP NHẬT TRẠNG THÁI");
    private int ticketId;

    public TicketDetailDialog(Frame parent, int tId) {
        super(parent, true);
        this.ticketId = tId;
        setTitle("Xử lý Yêu cầu - Ticket #" + tId);
        setSize(750, 600); // Mở rộng chiều ngang một chút
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // TOP: Cập nhật trạng thái
        JPanel pTop = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pTop.setBackground(new Color(240, 240, 240));
        pTop.setBorder(new EmptyBorder(5, 5, 5, 5));
        pTop.add(new JLabel("Đổi trạng thái:"));
        pTop.add(cbStatus);
        UIUtils.styleButton(btnUpdate, UIUtils.PRIMARY_COLOR);
        pTop.add(btnUpdate);
        add(pTop, BorderLayout.NORTH);

        // CENTER: Lịch sử tương tác
        txtHistory.setEditable(false);
        txtHistory.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtHistory.setLineWrap(true);
        txtHistory.setWrapStyleWord(true);
        txtHistory.setMargin(new Insets(10, 10, 10, 10));
        txtHistory.setBackground(new Color(250, 250, 250));
        JScrollPane scroll = new JScrollPane(txtHistory);
        add(scroll, BorderLayout.CENTER);

        // BOTTOM: Nhập tin nhắn & Nút bấm
        JPanel pBot = new JPanel(new BorderLayout(10, 5));
        pBot.setBorder(new EmptyBorder(10, 10, 10, 10));
        pBot.setBackground(Color.WHITE);

        txtMsg.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtMsg.setPreferredSize(new Dimension(0, 45));
        UIUtils.styleTextFieldSimple(txtMsg); // Style ô nhập liệu

        // Nhóm nút bấm
        JPanel pBtnGroup = new JPanel(new GridLayout(1, 3, 10, 0));
        pBtnGroup.setPreferredSize(new Dimension(420, 45)); // Rộng đủ chứa 3 nút

        UIUtils.styleButton(btnClear, UIUtils.DANGER_COLOR);

        UIUtils.styleButton(btnSend, UIUtils.SUCCESS_COLOR); // Nút phản hồi (Xanh)

        pBtnGroup.add(btnClear);

        pBtnGroup.add(btnSend);

        pBot.add(txtMsg, BorderLayout.CENTER);
        pBot.add(pBtnGroup, BorderLayout.EAST);
        add(pBot, BorderLayout.SOUTH);
    }

    // --- API CHO CONTROLLER ---
    public int getTicketId() {
        return ticketId;
    }

    public void setChatHistory(String history) {
        txtHistory.setText(history);
        txtHistory.setCaretPosition(txtHistory.getDocument().getLength());
    }

    public String getMessage() {
        return txtMsg.getText().trim();
    }

    public void clearMessageInput() {
        txtMsg.setText("");
    }

    public String getSelectedStatus() {
        return cbStatus.getSelectedItem().toString();
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public String showInput(String msg) {
        return JOptionPane.showInputDialog(this, msg);
    }

    public int showConfirm(String msg) {
        return JOptionPane.showConfirmDialog(this, msg, "Xác nhận", JOptionPane.YES_NO_OPTION);
    }

    // --- LISTENERS ---
    public void addSendListener(ActionListener al) {
        btnSend.addActionListener(al);
        txtMsg.addActionListener(al);
    }

    public void addUpdateStatusListener(ActionListener al) {
        btnUpdate.addActionListener(al);
    }

    public void addClearHistoryListener(ActionListener al) {
        btnClear.addActionListener(al);
    }
}
