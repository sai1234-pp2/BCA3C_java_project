
package com.quizportal;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegisterFrame extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;

    private final Color bg = new Color(20, 25, 48);
    private final Color purple = new Color(119, 87, 255);
    private final Color white = new Color(245, 245, 255);
    private final Color fieldBg = new Color(35, 42, 70);

    public RegisterFrame() {
        setTitle("QuizMaster - Create Account");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBackground(bg);

        // Left branding panel
        JPanel brandPanel = new JPanel();
        brandPanel.setBackground(purple);
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBorder(BorderFactory.createEmptyBorder(70, 25, 40, 25));

        JLabel logo = new JLabel("QUIZMASTER");
        logo.setFont(new Font("Arial", Font.BOLD, 27));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel icon = new JLabel("🎓");
        icon.setFont(new Font("Arial", Font.PLAIN, 55));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline = new JLabel("Start your journey!");
        tagline.setFont(new Font("Arial", Font.BOLD, 17));
        tagline.setForeground(Color.WHITE);
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel description = new JLabel("Learn. Play. Achieve.");
        description.setFont(new Font("Arial", Font.PLAIN, 13));
        description.setForeground(Color.WHITE);
        description.setAlignmentX(Component.CENTER_ALIGNMENT);

        brandPanel.add(logo);
        brandPanel.add(Box.createVerticalStrut(45));
        brandPanel.add(icon);
        brandPanel.add(Box.createVerticalStrut(20));
        brandPanel.add(tagline);
        brandPanel.add(Box.createVerticalStrut(10));
        brandPanel.add(description);

        // Right registration panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(bg);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(
                BorderFactory.createEmptyBorder(35, 30, 25, 30));

        JLabel heading = new JLabel("Create Account");
        heading.setFont(new Font("Arial", Font.BOLD, 25));
        heading.setForeground(white);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Join QuizMaster today");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(new Color(175, 180, 205));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        nameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();

        styleField(nameField);
        styleField(emailField);
        styleField(passwordField);

        JButton registerButton = new JButton("CREATE ACCOUNT");
        styleButton(registerButton, purple);

        JButton backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Arial", Font.PLAIN, 13));
        backButton.setForeground(white);
        backButton.setBackground(bg);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        formPanel.add(heading);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(subtitle);
        formPanel.add(Box.createVerticalStrut(25));

        addField(formPanel, "Full Name", nameField);
        addField(formPanel, "Email Address", emailField);
        addField(formPanel, "Password", passwordField);

        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(registerButton);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(backButton);

        mainPanel.add(brandPanel);
        mainPanel.add(formPanel);
        add(mainPanel);

        registerButton.addActionListener(e -> registerUser());

        backButton.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        setVisible(true);
    }

    private void addField(JPanel panel, String text, JComponent field) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(white);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(6));
        panel.add(field);
        panel.add(Box.createVerticalStrut(12));
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setForeground(Color.WHITE);
        field.setBackground(fieldBg);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(65, 73, 105)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void registerUser() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Please fill all fields.");
            return;
        }

        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this, "Account created successfully! Please login.");

            new LoginFrame();
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Registration failed. This email may already exist.\n"
                            + ex.getMessage()
            );
        }
    }
}
