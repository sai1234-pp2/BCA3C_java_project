package com.quizportal;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DashboardFrame extends JFrame {

    private final int userId;
    private final Color background = new Color(20, 25, 48);
    private final Color purple = new Color(119, 87, 255);

    public DashboardFrame(int userId, String name) {
        this.userId = userId;

        setTitle("QuizMaster - Dashboard");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout(20, 20));
        main.setBackground(background);
        main.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel heading = new JLabel("QUIZMASTER");
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel welcome = new JLabel("Welcome back, " + name + "!");
        welcome.setForeground(new Color(190, 195, 220));
        welcome.setFont(new Font("Arial", Font.PLAIN, 18));

        JPanel top = new JPanel(new GridLayout(2, 1, 5, 5));
        top.setOpaque(false);
        top.add(heading);
        top.add(welcome);

        JPanel center = new JPanel(new GridLayout(1, 2, 20, 20));
        center.setOpaque(false);

        center.add(createCard(String.valueOf(countQuestions()),
                "Available Questions"));
        center.add(createCard("Java", "Test Your Knowledge"));

        JButton start = createButton("START QUIZ  →", purple);
        JButton history = createButton("SCORE HISTORY  📊",
                new Color(54, 65, 100));
        JButton leaderboard = createButton("LEADERBOARD  🏆",
                new Color(54, 65, 100));

        start.addActionListener(e -> {
            dispose();
            new QuizFrame(userId);
        });

        history.addActionListener(e -> new ScoreHistoryFrame(userId));

        leaderboard.addActionListener(e -> new LeaderboardFrame(userId));

        JPanel bottom = new JPanel(new GridLayout(1, 3, 15, 0));
        bottom.setOpaque(false);
        bottom.add(start);
        bottom.add(history);
        bottom.add(leaderboard);

        main.add(top, BorderLayout.NORTH);
        main.add(center, BorderLayout.CENTER);
        main.add(bottom, BorderLayout.SOUTH);

        add(main);
        setVisible(true);
    }

    private int countQuestions() {
        String sql = "SELECT COUNT(*) FROM questions";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception ex) {
            // agar DB error aaye to dashboard phir bhi khulega
        }
        return 0;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JPanel createCard(String title, String subtitle) {
        JPanel card = new JPanel(new GridLayout(2, 1, 5, 5));
        card.setBackground(new Color(39, 46, 77));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(65, 75, 115)),
                BorderFactory.createEmptyBorder(30, 20, 30, 20)
        ));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));

        JLabel subLabel = new JLabel(subtitle, SwingConstants.CENTER);
        subLabel.setForeground(new Color(190, 195, 220));
        subLabel.setFont(new Font("Arial", Font.PLAIN, 15));

        card.add(titleLabel);
        card.add(subLabel);

        return card;
    }
}