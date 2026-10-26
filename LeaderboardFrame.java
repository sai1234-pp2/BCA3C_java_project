package com.quizportal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardFrame extends JFrame {

    private final Color bg = new Color(20, 25, 48);
    private final Color cardBg = new Color(39, 46, 77);
    private final Color purple = new Color(119, 87, 255);
    private final Color muted = new Color(190, 195, 220);

    private final int currentUserId;
    private final List<Integer> rowUserIds = new ArrayList<>();

    public LeaderboardFrame(int currentUserId) {
        this.currentUserId = currentUserId;

        setTitle("QuizMaster - Leaderboard");
        setSize(650, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBackground(bg);
        main.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel title = new JLabel("🏆 LEADERBOARD - TOP 10", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Ranked by best score percentage",
                SwingConstants.CENTER);
        sub.setFont(new Font("Arial", Font.PLAIN, 14));
        sub.setForeground(muted);

        JPanel top = new JPanel(new GridLayout(2, 1, 0, 4));
        top.setOpaque(false);
        top.add(title);
        top.add(sub);

        String[] cols = {"Rank", "Name", "Best Score", "Attempts"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        loadData(model);

        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 15));
        table.setBackground(cardBg);
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(60, 68, 105));
        table.setSelectionBackground(purple);
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(false);
        table.setFocusable(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(purple);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t,
                                                           Object value, boolean sel, boolean focus,
                                                           int row, int col) {
                super.getTableCellRendererComponent(
                        t, value, sel, focus, row, col);
                setHorizontalAlignment(col == 1 ? LEFT : CENTER);
                setBorder(new EmptyBorder(0, 12, 0, 12));
                setForeground(Color.WHITE);

                boolean me = row < rowUserIds.size()
                        && rowUserIds.get(row) == currentUserId;
                setBackground(me ? new Color(68, 55, 140) : cardBg);
                setFont(new Font("Arial",
                        me ? Font.BOLD : Font.PLAIN, 15));
                return this;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        table.getColumnModel().getColumn(0).setMaxWidth(80);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(
                new Color(65, 75, 115)));
        scroll.getViewport().setBackground(cardBg);

        JLabel hint = new JLabel("Highlighted row = you",
                SwingConstants.CENTER);
        hint.setFont(new Font("Arial", Font.PLAIN, 12));
        hint.setForeground(muted);

        JButton close = new JButton("CLOSE");
        close.setFont(new Font("Arial", Font.BOLD, 14));
        close.setForeground(Color.WHITE);
        close.setBackground(new Color(55, 62, 90));
        close.setOpaque(true);
        close.setContentAreaFilled(true);
        close.setBorderPainted(false);
        close.setFocusPainted(false);
        close.setBorder(new EmptyBorder(12, 30, 12, 30));
        close.setCursor(new Cursor(Cursor.HAND_CURSOR));
        close.addActionListener(e -> dispose());

        JPanel bottom = new JPanel(new BorderLayout(0, 8));
        bottom.setOpaque(false);
        bottom.add(hint, BorderLayout.NORTH);
        bottom.add(close, BorderLayout.SOUTH);

        main.add(top, BorderLayout.NORTH);
        main.add(scroll, BorderLayout.CENTER);
        main.add(bottom, BorderLayout.SOUTH);

        add(main);
        setVisible(true);
    }

    private void loadData(DefaultTableModel model) {
        String sql = "SELECT u.id, u.name, "
                + "MAX(r.score * 100.0 / r.total_questions) AS best_pct, "
                + "COUNT(*) AS attempts "
                + "FROM quiz_results r JOIN users u ON u.id = r.user_id "
                + "WHERE r.total_questions > 0 "
                + "GROUP BY u.id, u.name "
                + "ORDER BY best_pct DESC, attempts ASC "
                + "LIMIT 10";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            int rank = 1;
            while (rs.next()) {
                String medal = rank == 1 ? "🥇" : rank == 2 ? "🥈"
                        : rank == 3 ? "🥉" : String.valueOf(rank);

                rowUserIds.add(rs.getInt("id"));
                model.addRow(new Object[]{
                        medal,
                        rs.getString("name"),
                        Math.round(rs.getDouble("best_pct")) + "%",
                        rs.getInt("attempts")
                });
                rank++;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not load leaderboard: " + ex.getMessage());
        }
    }
}