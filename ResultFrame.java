package com.quizportal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ResultFrame extends JFrame {

    private final Color bg = new Color(20, 25, 48);
    private final Color purple = new Color(119, 87, 255);

    private static final String[] JAVA_TIPS = {
            "Java ko pehle 'Oak' naam diya gaya tha, baad mein Java rakha gaya.",
            "Java 1995 mein James Gosling ki team ne Sun Microsystems mein launch kiya.",
            "'Write Once, Run Anywhere' Java ka famous motto hai, JVM ki wajah se.",
            "String Java mein immutable hota hai, yaani ek baar bana to badal nahi sakta.",
            "Java mein pointers nahi hote, isliye wo C/C++ se zyada safe mana jata hai.",
            "Garbage Collector apne aap unused memory free karta hai.",
            "Java mein multiple inheritance classes se nahi, interfaces se milti hai."
    };

    private final List<Particle> particles = new ArrayList<>();
    private final Random random = new Random();
    private Timer confettiTimer;
    private int confettiTicks = 0;

    public ResultFrame(int score, int totalQuestions) {
        this(score, totalQuestions, 1);
    }

    public ResultFrame(int score, int totalQuestions, int userId) {
        setTitle("QuizMaster - Your Result");
        setSize(600, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        saveResult(score, totalQuestions, userId);

        int percentage = totalQuestions == 0 ? 0 :
                (score * 100 / totalQuestions);

        String rank;
        String emoji;
        Color rankColor;
        if (percentage == 100) {
            rank = "PERFECT SCORE - JAVA LEGEND";
            emoji = "👑";
            rankColor = new Color(255, 200, 70);
        } else if (percentage >= 90) {
            rank = "JAVA MASTER";
            emoji = "🏆";
            rankColor = new Color(255, 200, 70);
        } else if (percentage >= 70) {
            rank = "JAVA PRO";
            emoji = "🎉";
            rankColor = new Color(70, 215, 145);
        } else if (percentage >= 40) {
            rank = "JAVA LEARNER";
            emoji = "📘";
            rankColor = new Color(120, 170, 255);
        } else {
            rank = "JAVA BEGINNER";
            emoji = "🌱";
            rankColor = new Color(190, 195, 220);
        }

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(bg);
        main.setBorder(new EmptyBorder(25, 35, 25, 35));

        JLabel trophy = new JLabel(emoji);
        trophy.setFont(new Font("Arial", Font.PLAIN, 55));
        trophy.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("QUIZ COMPLETED!");
        title.setFont(new Font("Arial", Font.BOLD, 27));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel rankLabel = new JLabel(rank);
        rankLabel.setFont(new Font("Arial", Font.BOLD, 16));
        rankLabel.setForeground(rankColor);
        rankLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel message = new JLabel(
                percentage >= 70
                        ? "Excellent work! You did great!"
                        : "Good effort! Keep practicing!");
        message.setFont(new Font("Arial", Font.PLAIN, 17));
        message.setForeground(new Color(190, 195, 220));
        message.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel scoreLabel = new JLabel(score + " / " + totalQuestions);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 48));
        scoreLabel.setForeground(new Color(160, 135, 255));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel percentLabel = new JLabel("Your Score: " + percentage + "%");
        percentLabel.setFont(new Font("Arial", Font.BOLD, 20));
        percentLabel.setForeground(Color.WHITE);
        percentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Correct / wrong summary
        JLabel summary = new JLabel("✔ Correct: " + score
                + "     ✘ Wrong/Skipped: " + (totalQuestions - score));
        summary.setFont(new Font("Arial", Font.PLAIN, 14));
        summary.setForeground(new Color(190, 195, 220));
        summary.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Java fun fact card
        String tip = JAVA_TIPS[random.nextInt(JAVA_TIPS.length)];
        JLabel tipLabel = new JLabel("<html><div style='text-align:center;width:380px'>"
                + "<b>☕ Java Fact:</b> " + tip + "</div></html>");
        tipLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        tipLabel.setForeground(Color.WHITE);
        tipLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel tipCard = new JPanel(new BorderLayout());
        tipCard.setBackground(new Color(39, 46, 77));
        tipCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(75, 82, 120), 1, true),
                new EmptyBorder(10, 15, 10, 15)));
        tipCard.setMaximumSize(new Dimension(440, 70));
        tipCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        tipCard.add(tipLabel, BorderLayout.CENTER);

        JButton retry = new JButton("TRY AGAIN");
        retry.setFont(new Font("Arial", Font.BOLD, 16));
        retry.setForeground(Color.WHITE);
        retry.setBackground(purple);
        retry.setOpaque(true);
        retry.setContentAreaFilled(true);
        retry.setBorderPainted(false);
        retry.setFocusPainted(false);
        retry.setAlignmentX(Component.CENTER_ALIGNMENT);
        retry.setBorder(new EmptyBorder(12, 35, 12, 35));
        retry.setCursor(new Cursor(Cursor.HAND_CURSOR));
        retry.addActionListener(e -> {
            stopConfetti();
            dispose();
            new QuizFrame(userId);
        });

        JButton home = new JButton("BACK TO DASHBOARD");
        home.setFont(new Font("Arial", Font.BOLD, 14));
        home.setForeground(Color.WHITE);
        home.setBackground(new Color(55, 62, 90));
        home.setOpaque(true);
        home.setContentAreaFilled(true);
        home.setBorderPainted(false);
        home.setFocusPainted(false);
        home.setAlignmentX(Component.CENTER_ALIGNMENT);
        home.setBorder(new EmptyBorder(10, 28, 10, 28));
        home.setCursor(new Cursor(Cursor.HAND_CURSOR));
        home.addActionListener(e -> {
            stopConfetti();
            dispose();
            new DashboardFrame(userId, getUserName(userId));
        });

        main.add(trophy);
        main.add(Box.createVerticalStrut(6));
        main.add(title);
        main.add(Box.createVerticalStrut(6));
        main.add(rankLabel);
        main.add(Box.createVerticalStrut(6));
        main.add(message);
        main.add(Box.createVerticalStrut(14));
        main.add(scoreLabel);
        main.add(percentLabel);
        main.add(Box.createVerticalStrut(6));
        main.add(summary);
        main.add(Box.createVerticalStrut(14));
        main.add(tipCard);
        main.add(Box.createVerticalStrut(18));
        main.add(retry);
        main.add(Box.createVerticalStrut(10));
        main.add(home);

        add(main);
        setVisible(true);

        // Celebration: 70% ya usse zyada par confetti
        if (percentage >= 70) {
            startConfetti(percentage == 100 ? 220 : 130);
        }
    }

    // ---------- Confetti celebration ----------

    private static class Particle {
        double x, y, vx, vy, angle, spin;
        int w, h;
        Color color;
    }

    private void startConfetti(int count) {
        Color[] palette = {
                new Color(255, 200, 70), new Color(119, 87, 255),
                new Color(70, 215, 145), new Color(255, 85, 120),
                new Color(80, 190, 255), Color.WHITE
        };

        for (int i = 0; i < count; i++) {
            Particle p = new Particle();
            p.x = random.nextInt(getWidth());
            p.y = -random.nextInt(450);
            p.vx = random.nextDouble() * 2 - 1;
            p.vy = 2 + random.nextDouble() * 3;
            p.angle = random.nextDouble() * Math.PI * 2;
            p.spin = random.nextDouble() * 0.3 - 0.15;
            p.w = 6 + random.nextInt(6);
            p.h = 4 + random.nextInt(5);
            p.color = palette[random.nextInt(palette.length)];
            particles.add(p);
        }

        JComponent glass = new JComponent() {
            @Override
            public boolean contains(int x, int y) {
                return false; // clicks neeche buttons tak jayenge
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                for (Particle p : particles) {
                    g2.setColor(p.color);
                    g2.rotate(p.angle, p.x, p.y);
                    g2.fillRect((int) p.x, (int) p.y, p.w, p.h);
                    g2.rotate(-p.angle, p.x, p.y);
                }
                g2.dispose();
            }
        };
        glass.setOpaque(false);
        setGlassPane(glass);
        glass.setVisible(true);

        confettiTimer = new Timer(30, e -> {
            confettiTicks++;
            for (Particle p : particles) {
                p.x += p.vx + Math.sin(p.y / 25.0) * 0.8;
                p.y += p.vy;
                p.angle += p.spin;
            }
            glass.repaint();

            // ~7 second baad band
            if (confettiTicks > 230) {
                stopConfetti();
            }
        });
        confettiTimer.start();
    }

    private void stopConfetti() {
        if (confettiTimer != null) {
            confettiTimer.stop();
        }
        particles.clear();
        if (getGlassPane() != null) {
            getGlassPane().setVisible(false);
        }
    }
    private String getUserName(int userId) {
        String sql = "SELECT name FROM users WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        } catch (Exception ex) {

        }
        return "Student";
    }
    private void saveResult(int score, int totalQuestions, int userId) {
        String sql = "INSERT INTO quiz_results " +
                "(user_id, score, total_questions) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, score);
            ps.setInt(3, totalQuestions);
            ps.executeUpdate();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not save score: " + ex.getMessage());
        }
    }
}