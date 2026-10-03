package view;

import model.Service;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;

public class ServiceManagerView extends JFrame {

    private JTable tblServices = new JTable();
    private DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"STT", "Tên Dịch vụ", "Mô tả", "ID_AN"}, 0);
    private JTextField txtName = new JTextField();
    private JTextArea txtDesc = new JTextArea(3, 20);
    private JButton btnAdd = new JButton("THÊM MỚI");
    private JButton btnUpd = new JButton("CẬP NHẬT");
    private JButton btnDel = new JButton("XÓA");
    private JButton btnReset = new JButton("LÀM MỚI");
    private JTextField txtSearch = new JTextField(20);
    private JButton btnSearch = new JButton("Tìm kiếm");
    private JButton btnPrev = new JButton("< Trước");
    private JButton btnNext = new JButton("Sau >");
    private JLabel lblPageInfo = new JLabel("Trang 1 / 1");

    public ServiceManagerView() {
        setTitle("Quản lý Danh mục Dịch vụ");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(UIUtils.createHeader("QUẢN LÝ DỊCH VỤ & GÓI CƯỚC"), BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));
        pnlCenter.setBackground(UIUtils.CONTENT_BG);
        pnlCenter.setBorder(new EmptyBorder(10, 20, 20, 20));

        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(200, 200, 200), 1), new EmptyBorder(20, 30, 20, 30)));

        UIUtils.styleTextFieldSimple(txtName);
        txtDesc.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        txtDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDesc.setLineWrap(true);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlInput.add(new JLabel("Tên Dịch vụ (*):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        pnlInput.add(txtName, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        pnlInput.add(new JLabel("Mô tả chi tiết:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        pnlInput.add(new JScrollPane(txtDesc), gbc);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlButtons.setBackground(UIUtils.CONTENT_BG);
        UIUtils.styleButton(btnAdd, UIUtils.SUCCESS_COLOR);
        UIUtils.styleButton(btnUpd, UIUtils.WARNING_COLOR);
        btnUpd.setForeground(Color.BLACK);
        UIUtils.styleButton(btnDel, UIUtils.DANGER_COLOR);
        UIUtils.styleButton(btnReset, UIUtils.INFO_COLOR);

        // 🛠️ BỔ SUNG: Đặt kích thước (160, 45) giúp dàn nút bấm to, cao hoành tráng
        Dimension btnSize = new Dimension(160, 45);
        btnAdd.setPreferredSize(btnSize);
        btnUpd.setPreferredSize(btnSize);
        btnDel.setPreferredSize(btnSize);
        btnReset.setPreferredSize(btnSize);

        pnlButtons.add(btnAdd);
        pnlButtons.add(btnUpd);
        pnlButtons.add(btnDel);
        pnlButtons.add(btnReset);

        JPanel pnlTopWrapper = new JPanel(new BorderLayout());
        pnlTopWrapper.add(pnlInput, BorderLayout.CENTER);
        pnlTopWrapper.add(pnlButtons, BorderLayout.SOUTH);
        pnlCenter.add(pnlTopWrapper, BorderLayout.NORTH);

        JPanel pnlTableSection = new JPanel(new BorderLayout(0, 10));
        pnlTableSection.setBackground(UIUtils.CONTENT_BG);
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSearch.setBackground(UIUtils.CONTENT_BG);
        UIUtils.styleTextFieldSimple(txtSearch);
        UIUtils.styleButton(btnSearch, Color.GRAY);

        // Cố định kích thước nút tìm kiếm cho vuông vắn
        btnSearch.setPreferredSize(new Dimension(130, 30));

        pnlSearch.add(new JLabel("Tìm kiếm: "));
        pnlSearch.add(txtSearch);
        pnlSearch.add(btnSearch);

        UIUtils.styleTable(tblServices);
        tblServices.setModel(tableModel);
        tblServices.getColumnModel().getColumn(0).setMaxWidth(60);
        tblServices.getColumnModel().getColumn(3).setMinWidth(0);
        tblServices.getColumnModel().getColumn(3).setMaxWidth(0);

        JScrollPane scroll = new JScrollPane(tblServices);
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

        // CẬP NHẬT: Khóa nút Sửa khi mới mở Form
        setEditMode(false);
    }

    public Service getServiceFromForm() {
        return new Service(0, txtName.getText().trim(), txtDesc.getText().trim());
    }

    public String getSearchKeyword() {
        return txtSearch.getText().trim();
    }

    public Service getSelectedService() {
        int r = tblServices.getSelectedRow();
        if (r < 0) {
            return null;
        }
        return new Service(Integer.parseInt(tblServices.getValueAt(r, 3).toString()), tblServices.getValueAt(r, 1).toString(), tblServices.getValueAt(r, 2) != null ? tblServices.getValueAt(r, 2).toString() : "");
    }

    public void setTableData(List<Service> list, int currentPage, int pageSize) {
        tableModel.setRowCount(0);
        for (int i = 0; i < list.size(); i++) {
            Service s = list.get(i);
            tableModel.addRow(new Object[]{(currentPage - 1) * pageSize + i + 1, s.getServiceName(), s.getDescription(), s.getId()});
        }
    }

    public void setPageInfo(int current, int total, int totalRows) {
        lblPageInfo.setText("Trang " + current + " / " + total + " (Tổng: " + totalRows + ")");
        btnPrev.setEnabled(current > 1);
        btnNext.setEnabled(current < total);
    }

    public void fillForm(Service s) {
        if (s == null) {
            return;
        }
        txtName.setText(s.getServiceName());
        txtDesc.setText(s.getDescription());
    }

    public void clearForm() {
        txtName.setText("");
        txtDesc.setText("");
        txtSearch.setText(""); // CẬP NHẬT: Xóa trắng ô tìm kiếm
        tblServices.clearSelection();
    }

    public void setEditMode(boolean isEdit) {
        btnAdd.setEnabled(!isEdit);
        btnUpd.setEnabled(isEdit);
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

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
        tblServices.addMouseListener(ma);
    }
}
