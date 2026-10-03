package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UIUtils {

    // --- 1. MÀU SẮC ---
    public static final Color PRIMARY_COLOR = new Color(52, 152, 219); // Xanh dương
    public static final Color SIDEBAR_BG = new Color(34, 45, 50);      // Đen Sidebar
    public static final Color CONTENT_BG = new Color(236, 240, 245);   // Xám nền
    public static final Color WHITE = Color.WHITE;

    // Màu Header Bảng (Đen)
    public static final Color TABLE_HEADER_BG = new Color(34, 45, 50);
    public static final Color TABLE_HEADER_FG = Color.WHITE;

    // Màu nút bấm
    public static final Color SUCCESS_COLOR = new Color(40, 167, 69); // Xanh lá
    public static final Color DANGER_COLOR = new Color(220, 53, 69);  // Đỏ
    public static final Color WARNING_COLOR = new Color(255, 193, 7); // Vàng
    public static final Color INFO_COLOR = new Color(23, 162, 184);   // Xanh lơ
    public static final Color GRAY_BTN = new Color(149, 165, 166);    // Xám

    // Màu Card Thống kê
    public static final Color CARD_BLUE = new Color(52, 152, 219);
    public static final Color CARD_GREEN = new Color(46, 204, 113);
    public static final Color CARD_ORANGE = new Color(243, 156, 18);
    public static final Color CARD_PURPLE = new Color(155, 89, 182);

    // --- 2. FONTS ---
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_MENU = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_CARD_VAL = new Font("Segoe UI", Font.BOLD, 36);
    public static final Font FONT_CARD_LBL = new Font("Segoe UI", Font.PLAIN, 14);

    // --- 3. STYLE METHODS ---
    // Style 1: Input có tiêu đề (Dùng cho Login - NHƯ ẢNH 1)
    public static void styleTextField(JTextField txt, String title) {
        txt.setFont(FONT_NORMAL);
        txt.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), title,
                0, 0, new Font("Segoe UI", Font.BOLD, 12), new Color(80, 80, 80)
        ));
    }

    // Style 2: Input thường (Dùng cho Form quản lý - NHƯ ẢNH 3, 4)
    public static void styleTextFieldSimple(JTextField txt) {
        txt.setFont(FONT_NORMAL);
        txt.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(5, 8, 5, 8)
        ));
    }

    // Style 3: Nút bấm ĐẶC MÀU (FIX LỖI TRẮNG NÚT)
    public static void styleButton(JButton btn, Color bgColor) {
        btn.setFont(FONT_BOLD);
        btn.setBackground(bgColor);
        btn.setForeground(WHITE);

        // --- QUAN TRỌNG: Tắt hiệu ứng mặc định để hiện màu ---
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); // Bỏ viền 3D
        btn.setOpaque(true);         // Ép vẽ màu nền

        btn.setBorder(new EmptyBorder(10, 20, 10, 20)); // Padding
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Style 4: Bảng (Header đen, dòng cao 40px - NHƯ ẢNH 5)
    public static void styleTable(JTable table) {
        table.setFont(FONT_NORMAL);
        table.setRowHeight(40);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(220, 240, 255));
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setOpaque(true);
        header.setBackground(TABLE_HEADER_BG); // Đen
        header.setForeground(TABLE_HEADER_FG); // Trắng
        header.setPreferredSize(new Dimension(0, 45));

        // Renderer ép màu header
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean isS, boolean hasF, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, isS, hasF, r, c);
                l.setBackground(TABLE_HEADER_BG);
                l.setForeground(TABLE_HEADER_FG);
                l.setHorizontalAlignment(JLabel.CENTER);
                l.setFont(FONT_BOLD);
                return l;
            }
        });

        // Căn giữa nội dung
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    // Style 5: Nút Menu Sidebar (Màu tối)
    public static JButton createMenuButton(String text) {
        JButton btn = new JButton(text.toUpperCase());
        btn.setFont(FONT_MENU);
        btn.setForeground(new Color(184, 199, 206));
        btn.setBackground(SIDEBAR_BG);

        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(true);

        btn.setBorder(new EmptyBorder(15, 25, 15, 10));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(44, 59, 65));
                btn.setForeground(WHITE);
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(SIDEBAR_BG);
                btn.setForeground(new Color(184, 199, 206));
            }
        });
        return btn;
    }

    public static JPanel createHeader(String title) {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl.setBackground(CONTENT_BG);
        pnl.setBorder(new EmptyBorder(15, 20, 5, 20));
        JLabel lbl = new JLabel(title.toUpperCase());
        lbl.setFont(FONT_TITLE);
        lbl.setForeground(new Color(60, 70, 80));
        pnl.add(lbl);
        return pnl;
    }

    public static JPanel createStatsCard(String title, String value, Color bgColor) {
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(bgColor);
        card.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel lblVal = new JLabel(value, SwingConstants.CENTER);
        lblVal.setFont(FONT_CARD_VAL);
        lblVal.setForeground(WHITE);
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(FONT_CARD_LBL);
        lblTitle.setForeground(new Color(255, 255, 255, 200));
        card.add(lblVal);
        card.add(lblTitle);
        return card;
    }
    

 public static void addComponent(JPanel panel, Component comp, int x, int y, int width) {
  GridBagConstraints gbc = new GridBagConstraints();
  gbc.gridx = x;
  gbc.gridy = y;
  gbc.gridwidth = width;
  gbc.insets = new Insets(10, 10, 10, 10);
  gbc.fill = GridBagConstraints.HORIZONTAL;
  panel.add(comp, gbc);
 }


 public static JLabel createTitleLabel(String title) {
  JLabel label = new JLabel(title, SwingConstants.CENTER);
  label.setFont(new Font("Arial", Font.BOLD, 20));
  return label;
 }

 public static void addFormRow(JPanel panel, String labelText, Component input, int row) {
  JLabel label = new JLabel(labelText);
  label.setFont(new Font("SansSerif", Font.PLAIN, 14));
  label.setHorizontalAlignment(SwingConstants.RIGHT);

  GridBagConstraints gbcLabel = new GridBagConstraints();
  gbcLabel.gridx = 0;
  gbcLabel.gridy = row;
  gbcLabel.insets = new Insets(8, 10, 8, 5); // Trên-dưới | trái | phải
  gbcLabel.anchor = GridBagConstraints.EAST;

  GridBagConstraints gbcField = new GridBagConstraints();
  gbcField.gridx = 1;
  gbcField.gridy = row;
  gbcField.insets = new Insets(8, 5, 8, 10);
  gbcField.fill = GridBagConstraints.HORIZONTAL;
  gbcField.weightx = 1.0;

  panel.add(label, gbcLabel);
  panel.add(input, gbcField);
 }
 
 public static JButton createTextButton(String text) {
     JButton button = new JButton(text);
     button.setFont(new Font("SansSerif", Font.PLAIN, 13));
     button.setBackground(Color.WHITE);
     button.setFocusPainted(false);

     button.setMargin(new Insets(1, 3, 1, 3)); 

     return button;
}
}
