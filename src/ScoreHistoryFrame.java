
package com.quizportal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ScoreHistoryFrame extends JFrame {

    private final Color bg = new Color(20, 25, 48);
    private final Color purple = new Color(119, 87, 255);

    private JTable table;
    private DefaultTableModel model;

    public ScoreHistoryFrame(int userId) {
        setTitle("QuizMaster - Score History");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout(15, 20));
        main.setBackground(bg);
        main.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel title = new JLabel("MY SCORE HISTORY");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Your previous quiz attempts");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 15));
        subtitle.setForeground(new Color(190, 195, 220));

        JPanel heading = new JPanel(new GridLayout(2, 1, 5, 5));
        heading.setOpaque(false);
        heading.add(title);
        heading.add(subtitle);

        String[] columns = {
                "Attempt", "Score", "Percentage", "Date & Time", "ID"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(32);
        table.setBackground(new Color(39, 46, 77));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(65, 75, 115));
        table.setSelectionBackground(purple);
        table.setSelectionForeground(Color.WHITE);

        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(purple);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);

        // Hide the database ID column
        table.removeColumn(table.getColumnModel().getColumn(4));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(39, 46, 77));
        scrollPane.setBorder(BorderFactory.createLineBorder(
                new Color(65, 75, 115)));

        // Right-click a history record to delete it
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Delete this history");
        popupMenu.add(deleteItem);

        deleteItem.addActionListener(e -> deleteSelectedHistory(userId));

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());

                    if (row >= 0) {
                        table.setRowSelectionInterval(row, row);
                        popupMenu.show(table, e.getX(), e.getY());
                    }
                }
            }
        });

        JButton deleteButton = new JButton("DELETE SELECTED");
        styleButton(deleteButton);
        deleteButton.addActionListener(
                e -> deleteSelectedHistory(userId));

        JButton back = new JButton("BACK TO DASHBOARD");
        styleButton(back);
        back.addActionListener(e -> dispose());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottom.setOpaque(false);
        bottom.add(deleteButton);
        bottom.add(back);

        main.add(heading, BorderLayout.NORTH);
        main.add(scrollPane, BorderLayout.CENTER);
        main.add(bottom, BorderLayout.SOUTH);

        add(main);

        loadHistory(userId);
        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(purple);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 16, 12, 16));
    }

    private void loadHistory(int userId) {
        model.setRowCount(0);

        String sql = "SELECT id, score, total_questions, quiz_date " +
                "FROM quiz_results WHERE user_id = ? " +
                "ORDER BY quiz_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                int attempt = 1;

                while (rs.next()) {
                    int id = rs.getInt("id");
                    int score = rs.getInt("score");
                    int total = rs.getInt("total_questions");

                    int percentage = total == 0
                            ? 0 : score * 100 / total;

                    model.addRow(new Object[]{
                            attempt++,
                            score + " / " + total,
                            percentage + "%",
                            rs.getTimestamp("quiz_date"),
                            id
                    });
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not load history: " + ex.getMessage());
        }
    }

    private void deleteSelectedHistory(int userId) {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a history record first.");
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        int historyId = (int) model.getValueAt(modelRow, 4);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this quiz history?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM quiz_results WHERE id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, historyId);
            ps.setInt(2, userId);

            int deleted = ps.executeUpdate();

            if (deleted > 0) {
                JOptionPane.showMessageDialog(this,
                        "Selected history deleted successfully!");
                loadHistory(userId);
            } else {
                JOptionPane.showMessageDialog(this,
                        "History not found or already deleted.");
                loadHistory(userId);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not delete history: " + ex.getMessage());
        }
    }
}
