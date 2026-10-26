# QuizMaster - Online Quiz Portal

A desktop Online Quiz Portal built with **Java (Swing)** and **MySQL (JDBC)**. Students can register, log in, take a timed Java quiz, see their result with a celebration, check their score history and compete on a leaderboard.

## Team Members

| Name | Roll No. |
|---|---|
| V. Sai Pratap Reddy | 92500527150 |
| M. Nikhil Kumar | 92500527152 |

**Class:** BCA 3C | **Subject:** Java Project (2026-27)

## Features

- User registration and login
- Dashboard showing available question count and the user's name
- Timed quiz (15 minutes) with countdown timer (turns red in the last minute)
- Progress bar and question palette (attempted / skipped / current)
- Back, Next, Clear and Finish options with submit confirmation
- Questions are shuffled for every attempt
- Result screen with score, percentage, rank badge, correct/wrong summary and a random Java fact
- Confetti celebration for scores of 70% and above
- Leaderboard (Top 10 by best score percentage)
- Score history with a Delete Selected option
- Modern dark navy-purple theme on all screens

## Technologies Used

- Java (Swing, AWT)
- MySQL 8
- JDBC (MySQL Connector/J)

## Project Structure

```
BCA3C_Java_Project/
├── README.md
├── code/            Java source files (package com.quizportal)
├── database/        schema.sql, questions_data.sql
└── documents/       Project report (.docx and .pdf)
```

Main classes: `LoginFrame`, `RegisterFrame`, `DashboardFrame`, `QuizFrame`, `ResultFrame`, `ScoreHistoryFrame`, `LeaderboardFrame`, `DBConnection`.

## Setup Instructions

1. Install **JDK** and **MySQL**, and add the **MySQL Connector/J** jar to the project libraries.
2. Create the database by running the SQL files in this order:
   ```
   mysql -u root -p < database/schema.sql
   mysql -u root -p online_quiz_db < database/questions_data.sql
   ```
3. Open `DBConnection.java` and set your MySQL username and password. The database name is `online_quiz_db`.
4. Compile and run the project. The application starts with the Login screen.
5. Register a new account, log in and start the quiz.

## Database Tables

- **users**: id, name, email, password
- **questions**: id, question, option_a, option_b, option_c, option_d, correct_answer
- **quiz_results**: id, user_id, score, total_questions, quiz_date

## Future Scope

- Admin panel to add, edit and delete questions
- Multiple categories and difficulty levels
- Answer review screen after the quiz
- Password hashing for better security
