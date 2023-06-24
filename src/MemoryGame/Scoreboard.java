package MemoryGame;

import Messages.Messages;
import UserDao.UserDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Scoreboard {
    public void showLeaderboard() {
        UserDao userDao = new UserDao();
        Messages messages = new Messages();
        // Query
        String query = "select distinct scores.score, users.user_name as username from scores " +
                "join users on scores.user_id = users.id " +
                "order by scores.score desc " +
                "limit 10";

        try {
            Statement statement = userDao.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);

            // Display the top 10 high scores
            System.out.println("Top 10 High Scores:");
            System.out.println("-------------------");
            System.out.println("Username - Score");
            while (rs.next()) {
                String playerName = rs.getString("username");
                int score = rs.getInt("score");
                System.out.println(playerName + " - " + score);
            }
        } catch (SQLException e) {
            messages.defaultErrorMessage(e.getMessage());
        }
    }
}
