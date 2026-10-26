package com.quizportal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

public class QuizFrame extends JFrame {

    private final int userId;
    private final List<Question> questions = new ArrayList<>();
    private int index = 0;
    private int score = 0;

    private JLabel questionLabel, countLabel, timerLabel;
    private JProgressBar progressBar;
    private final JRadioButton[] options = new JRadioButton[4];
    private final ButtonGroup group = new ButtonGroup();

    private JButton nextButton, backButton, clearButton;
    private Timer timer;
    private int remainingSeconds = 15 * 60;
    private boolean quizFinished = false;

    private int[] selectedAnswers;
    private JButton[] paletteButtons;

    // Dark navy-purple theme (same as Login and Result screens)
    private final Color bg = new Color(20, 25, 48);
    private final Color purple = new Color(119, 87, 255);
    private final Color darkText = Color.WHITE;
    private final Color optionBg = new Color(39, 46, 77);
    private final Color optionSelectedBg = new Color(68, 55, 140);
    private final Color optionBorder = new Color(75, 82, 120);
    private final Color mutedText = new Color(190, 195, 220);
    private final Color idleBg = new Color(55, 62, 90);
    private final Color amber = new Color(255, 180, 60);

    public QuizFrame(int userId) {
        this.userId = userId;

        setTitle("QuizMaster - Challenge");
        setSize(950, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        loadQuestions();

        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No questions found. Check database connection.");
            dispose();
            return;
        }

        selectedAnswers = new int[questions.size()];
        Arrays.fill(selectedAnswers, -1);

        JPanel main = new JPanel(new BorderLayout(20, 20));
        main.setBackground(bg);
        main.setBorder(new EmptyBorder(25, 35, 25, 35));

        JLabel title = new JLabel("QUIZ CHALLENGE");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(darkText);

        timerLabel = new JLabel("TIME LEFT: 15:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 17));
        timerLabel.setForeground(new Color(70, 215, 145));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(title, BorderLayout.WEST);
        titlePanel.add(timerLabel, BorderLayout.EAST);

        countLabel = new JLabel();
        countLabel.setForeground(mutedText);
        countLabel.setFont(new Font("Arial", Font.BOLD, 15));

        progressBar = new JProgressBar(0, questions.size());
        progressBar.setStringPainted(true);
        progressBar.setForeground(purple);
        progressBar.setBackground(idleBg);

        JPanel header = new JPanel(new BorderLayout(10, 15));
        header.setOpaque(false);
        header.add(titlePanel, BorderLayout.NORTH);

        JPanel progressPanel = new JPanel(new BorderLayout(10, 8));
        progressPanel.setOpaque(false);
        progressPanel.add(countLabel, BorderLayout.WEST);
        progressPanel.add(progressBar, BorderLayout.SOUTH);
        header.add(progressPanel, BorderLayout.SOUTH);

        JPanel center = new JPanel(new GridLayout(5, 1, 10, 10));
        center.setOpaque(false);

        questionLabel = new JLabel();
        questionLabel.setForeground(darkText);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 19));
        center.add(questionLabel);

        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setFont(new Font("Arial", Font.PLAIN, 16));
            options[i].setForeground(darkText);
            options[i].setBackground(optionBg);
            options[i].setOpaque(true);
            options[i].setFocusPainted(false);
            options[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(optionBorder, 1, true),
                    new EmptyBorder(10, 15, 10, 10)));
            options[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            options[i].addActionListener(e -> {
                saveCurrentAnswer();
                refreshOptionStyles();
                updatePalette();
            });
            group.add(options[i]);
            center.add(options[i]);
        }

        backButton = new JButton("← BACK");
        styleButton(backButton, idleBg);
        backButton.addActionListener(e -> previousQuestion());

        clearButton = new JButton("CLEAR");
        styleButton(clearButton, idleBg);
        clearButton.addActionListener(e -> clearSelection());

        nextButton = new JButton("NEXT QUESTION →");
        styleButton(nextButton, purple);
        nextButton.addActionListener(e -> nextQuestion());

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.add(backButton, BorderLayout.WEST);
        bottom.add(clearButton, BorderLayout.CENTER);
        bottom.add(nextButton, BorderLayout.EAST);

        // Question palette
        JPanel sidePanel = new JPanel(new BorderLayout(0, 10));
        sidePanel.setOpaque(false);
        sidePanel.setPreferredSize(new Dimension(180, 0));

        JLabel paletteTitle = new JLabel("QUESTIONS");
        paletteTitle.setForeground(darkText);
        paletteTitle.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel legend = new JLabel(
                "<html><font color='#9B9BB0'>■</font> skipped"
                        + " &nbsp; <font color='#7757FF'>■</font> attempted"
                        + " &nbsp; <font color='#FFB43C'>■</font> current</html>");
        legend.setFont(new Font("Arial", Font.PLAIN, 10));
        legend.setForeground(mutedText);

        JPanel paletteGrid = new JPanel(new GridLayout(0, 4, 8, 8));
        paletteGrid.setOpaque(false);

        paletteButtons = new JButton[questions.size()];

        for (int i = 0; i < questions.size(); i++) {
            final int qIndex = i;

            paletteButtons[i] = new JButton(String.valueOf(i + 1));
            paletteButtons[i].setFont(new Font("Arial", Font.BOLD, 12));
            paletteButtons[i].setOpaque(true);
            paletteButtons[i].setContentAreaFilled(true);
            paletteButtons[i].setBorderPainted(false);
            paletteButtons[i].setFocusPainted(false);
            paletteButtons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            paletteButtons[i].setBorder(new EmptyBorder(8, 5, 8, 5));
            paletteButtons[i].addActionListener(
                    e -> jumpToQuestion(qIndex));
            paletteGrid.add(paletteButtons[i]);
        }

        JPanel sideTop = new JPanel(new BorderLayout(0, 5));
        sideTop.setOpaque(false);
        sideTop.add(paletteTitle, BorderLayout.NORTH);
        sideTop.add(legend, BorderLayout.SOUTH);

        sidePanel.add(sideTop, BorderLayout.NORTH);
        sidePanel.add(paletteGrid, BorderLayout.CENTER);

        main.add(header, BorderLayout.NORTH);
        main.add(center, BorderLayout.CENTER);
        main.add(bottom, BorderLayout.SOUTH);
        main.add(sidePanel, BorderLayout.EAST);

        add(main);

        showQuestion();
        setVisible(true);
        startTimer();
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(13, 22, 13, 22));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void loadQuestions() {
        String sql = "SELECT * FROM questions";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                questions.add(new Question(
                        rs.getString("question"),
                        new String[]{
                                rs.getString("option_a"),
                                rs.getString("option_b"),
                                rs.getString("option_c"),
                                rs.getString("option_d")
                        },
                        rs.getString("correct_answer")
                ));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database error: " + ex.getMessage());
        }

        Collections.shuffle(questions);
    }

    private void startTimer() {
        timer = new Timer(1000, e -> {
            remainingSeconds--;

            int minutes = remainingSeconds / 60;
            int seconds = remainingSeconds % 60;

            timerLabel.setText(String.format(
                    "TIME LEFT: %02d:%02d", minutes, seconds));

            if (remainingSeconds <= 60) {
                timerLabel.setForeground(new Color(255, 85, 95));
            }

            if (remainingSeconds <= 0) {
                finishQuiz(true);
            }
        });

        timer.start();
    }

    private void saveCurrentAnswer() {
        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected()) {
                selectedAnswers[index] = i;
                return;
            }
        }

        selectedAnswers[index] = -1;
    }

    private void jumpToQuestion(int qIndex) {
        if (quizFinished || qIndex == index) {
            return;
        }

        saveCurrentAnswer();
        index = qIndex;
        showQuestion();
    }

    private void clearSelection() {
        if (quizFinished) {
            return;
        }

        group.clearSelection();
        selectedAnswers[index] = -1;
        refreshOptionStyles();
        updatePalette();
    }

    // Selected option ko highlight karta hai
    private void refreshOptionStyles() {
        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected()) {
                options[i].setBackground(optionSelectedBg);
                options[i].setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(purple, 2, true),
                        new EmptyBorder(9, 14, 9, 9)));
            } else {
                options[i].setBackground(optionBg);
                options[i].setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(optionBorder, 1, true),
                        new EmptyBorder(10, 15, 10, 10)));
            }
        }
    }

    private void updatePalette() {
        for (int i = 0; i < paletteButtons.length; i++) {
            if (i == index) {
                paletteButtons[i].setBackground(amber);
                paletteButtons[i].setForeground(new Color(35, 35, 55));
            } else if (selectedAnswers[i] != -1) {
                paletteButtons[i].setBackground(purple);
                paletteButtons[i].setForeground(Color.WHITE);
            } else {
                paletteButtons[i].setBackground(idleBg);
                paletteButtons[i].setForeground(mutedText);
            }
        }
    }

    private void showQuestion() {
        Question q = questions.get(index);

        countLabel.setText("QUESTION " + (index + 1)
                + " OF " + questions.size());

        progressBar.setValue(index + 1);
        progressBar.setString(
                (index + 1) + " / " + questions.size());

        questionLabel.setText("<html><body style='width:500px'>"
                + q.text + "</body></html>");

        group.clearSelection();

        for (int i = 0; i < 4; i++) {
            options[i].setText(
                    (char) ('A' + i) + ". " + q.options[i]);

            if (selectedAnswers[index] == i) {
                options[i].setSelected(true);
            }
        }

        backButton.setEnabled(index > 0);

        nextButton.setText(index == questions.size() - 1
                ? "FINISH QUIZ ✓" : "NEXT QUESTION →");

        refreshOptionStyles();
        updatePalette();
    }

    private void previousQuestion() {
        if (quizFinished || index == 0) {
            return;
        }

        saveCurrentAnswer();
        index--;
        showQuestion();
    }

    private void nextQuestion() {
        if (quizFinished) {
            return;
        }

        saveCurrentAnswer();

        if (index < questions.size() - 1) {
            index++;
            showQuestion();
        } else {
            confirmAndFinish();
        }
    }

    private void confirmAndFinish() {
        int skipped = 0;

        for (int a : selectedAnswers) {
            if (a == -1) {
                skipped++;
            }
        }

        if (skipped > 0) {
            int choice = JOptionPane.showConfirmDialog(this,
                    skipped + " question(s) are not attempted.\n"
                            + "Do you still want to submit the quiz?",
                    "Submit Quiz",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (choice != JOptionPane.OK_OPTION) {
                return;
            }
        }

        finishQuiz(false);
    }

    private void finishQuiz(boolean timeUp) {
        if (quizFinished) {
            return;
        }

        saveCurrentAnswer();
        score = 0;

        for (int i = 0; i < questions.size(); i++) {
            int selected = selectedAnswers[i];

            if (selected != -1
                    && String.valueOf((char) ('A' + selected))
                    .equalsIgnoreCase(
                            questions.get(i).correctAnswer.trim())) {
                score++;
            }
        }

        quizFinished = true;

        if (timer != null) {
            timer.stop();
        }

        dispose();

        if (timeUp) {
            JOptionPane.showMessageDialog(null,
                    "Time is up! Your quiz has been submitted.");
        }

        new ResultFrame(score, questions.size(), userId);
    }

    private static class Question {
        String text;
        String[] options;
        String correctAnswer;

        Question(String text, String[] options, String correctAnswer) {
            this.text = text;
            this.options = options;
            this.correctAnswer = correctAnswer;
        }
    }
}