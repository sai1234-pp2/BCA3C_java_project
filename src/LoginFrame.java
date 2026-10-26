
package com.quizportal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    private final Color bg = new Color(20, 25, 48);
    private final Color purple = new Color(119, 87, 255);

    public LoginFrame() {
        setTitle("QuizMaster - Login");
        setSize(650, 470);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel main = new JPanel(new GridLayout(1, 2));
        main.setBackground(bg);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(new Color(39, 46, 77));
        left.setBorder(new EmptyBorder(70, 25, 30, 25));

        JLabel logo = new JLabel("QUIZMASTER");
        logo.setFont(new Font("Arial", Font.BOLD, 28));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emoji = new JLabel("🎯");
        emoji.setFont(new Font("Arial", Font.PLAIN, 65));
        emoji.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline = new JLabel(
                "<html><center>Challenge your mind.<br>Test your knowledge.</center></html>"
        );
        tagline.setFont(new Font("Arial", Font.PLAIN, 16));
        tagline.setForeground(new Color(205, 210, 235));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        left.add(logo);
        left.add(Box.createVerticalStrut(45));
        left.add(emoji);
        left.add(Box.createVerticalStrut(20));
        left.add(tagline);

        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBackground(bg);
        right.setBorder(new EmptyBorder(55, 25, 30, 25));

        JLabel heading = new JLabel("Welcome Back!");
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setForeground(Color.WHITE);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subheading = new JLabel("Login to start your quiz");
        subheading.setFont(new Font("Arial", Font.PLAIN, 13));
        subheading.setForeground(new Color(190, 195, 220));
        subheading.setAlignmentX(Component.LEFT_ALIGNMENT);

        emailField = new JTextField();
        passwordField = new JPasswordField();

        styleField(emailField);
        styleField(passwordField);

        JButton login = new JButton("LOGIN");
        styleButton(login);

        JButton register = new JButton("Create New Account");
        register.setForeground(new Color(180, 160, 255));
        register.setBackground(bg);
        register.setBorderPainted(false);
        register.setFocusPainted(false);
        register.setAlignmentX(Component.CENTER_ALIGNMENT);

        right.add(heading);
        right.add(Box.createVerticalStrut(8));
        right.add(subheading);
        right.add(Box.createVerticalStrut(25));
        right.add(makeLabel("Email Address"));
        right.add(Box.createVerticalStrut(5));
        right.add(emailField);
        right.add(Box.createVerticalStrut(15));
        right.add(makeLabel("Password"));
        right.add(Box.createVerticalStrut(5));
        right.add(passwordField);
        right.add(Box.createVerticalStrut(25));
        right.add(login);
        right.add(Box.createVerticalStrut(10));
        right.add(register);

        login.addActionListener(e -> loginUser());

        register.addActionListener(e -> {
            dispose();
            new RegisterFrame();
        });

        main.add(left);
        main.add(right);
        add(main);
        setVisible(true);
    }

    private JLabel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void styleField(JTextField field) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setPreferredSize(new Dimension(220, 38));
        field.setBackground(new Color(39, 46, 77));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(75, 82, 120)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void styleButton(JButton button) {
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.setBackground(purple);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void loginUser() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter email and password.");
            return;
        }

        String sql = "SELECT id, name FROM users WHERE email=? AND password=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    dispose();
                    new DashboardFrame(id, name);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Invalid email or password.");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Login failed: " + ex.getMessage());
        }
    }
}
