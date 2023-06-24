package UserDao;

import MemoryGame.StartGame;
import Messages.Messages;

import java.sql.*;
import java.util.Scanner;

public class UserDao {
    Scanner scanner = new Scanner(System.in);
    Messages messages = new Messages();
    StartGame startGame = new StartGame();

    private static Connection con;

    public Connection getConnection() {
        return con;
    }

    public void setConnection(String DB_URL, String USER, String PASSWORD) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, USER, PASSWORD);

        } catch (Exception e) {
            messages.defaultErrorMessage(e.getMessage());
        }
    }

    private static int userID;

    public int getUserId() {
        return userID;
    }

    public void setUserId(int id) {
        userID = id;
    }

    public void dbConnection(String DB_URL, String USER, String PASSWORD) {

        System.out.println("If you have an account type 'l' or 'r' if you don't have an account:");

        String authentication = scanner.nextLine();

//        // Connect to db
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            con = DriverManager.getConnection(DB_URL, USER, PASSWORD);
//
        setConnection(DB_URL, USER, PASSWORD);

        if (authentication.equals("l")) {
            login(getConnection());
        } else if (authentication.equals("r")) {
            signup(getConnection());
        } else {
            messages.invalidMessage();
        }
//        } catch (Exception e) {
//            messages.defaultErrorMessage(e.getMessage());
//        }

    }

    public void login(Connection con) {
        // Get user inputs
        System.out.println("Username:");
        String username = scanner.next();

        System.out.println("Password:");
        String password = scanner.next();

        // Get userID query
        String query = "Select id from users where user_name = ? AND `password` = ? ";

        // Validate if user already exists
        boolean userExists = validateUser(username, password, getConnection());

        if (userExists) {

            // Get userID and save in user_ID variable
            try {
                PreparedStatement statement = getConnection().prepareStatement(query);
                statement.setString(1, username);
                statement.setString(2, password);

                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    // Save the id the user gets into user_ID
                    setUserId(rs.getInt("id"));
//                    user_ID = rs.getInt("id");
                }
            } catch (SQLException e) {
                messages.defaultErrorMessage(e.getMessage());
            }

            // Continue to the game
            messages.successMessage("Welcome " + username);
            startGame.difficultySelection();
        } else {
            messages.errorMessage("Something went wrong.");
            System.out.println("Do you wish to update your password or create a new account?");
            System.out.println("Type 'u' for updating password or 'r' to create new account or 't' to retry login:");
            String choose = scanner.next();

            if (choose.equals("u")) {
                updateUser(username, getConnection());
            } else if (choose.equals("r")) {
                signup(getConnection());
            } else if (choose.equals("t")) {
                login(getConnection());
            } else {
                messages.invalidMessage();
            }
        }
    }

    public void signup(Connection con) {
        // Create insert query
        String query = "Insert into users(user_name,birth_date, password) values (?,?,?)";

        // Query to get the user id
        String getUserID = "select id from users order by id desc limit 1";

        // Get input from user
        System.out.println("Username: ");
        String userName = scanner.next();

        System.out.println("Birthdate: 'year-month-day'");
        String birthdate = scanner.next();

        System.out.println("Password: ");
        String password = scanner.next();

        // Prepare statement
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(query);

            // Replace '?' in query with user inputs
            statement.setString(1, userName);
            statement.setString(2, birthdate);
            statement.setString(3, password);
            statement.executeUpdate();

            // Get user id after data inserted into database
            ResultSet rs = statement.executeQuery(getUserID);
            while (rs.next()) {
                // Save the id the user gets saved into user_ID
                setUserId(rs.getInt("id"));
                messages.successMessage("Registered successful");
//                System.out.println("Your account code is: " + rs.getInt("id"));
            }

            startGame.difficultySelection();

        } catch (SQLException e) {
            messages.defaultErrorMessage(e.getMessage());
        }
    }

    public static boolean validateUser(String username, String pass, Connection con) {
        boolean userExists = false;

        // Select query that returns the total users in the table
        String sql = "SELECT COUNT(*) FROM users WHERE user_name = ? AND password = ?";

        PreparedStatement statement = null;

        try {
            // Prepare a connection to the db with the query
            statement = con.prepareStatement(sql);

            // Replace '?' in sql with username parameter
            statement.setString(1, username);
            statement.setString(2, pass);

            // Save query response in result
            ResultSet result = statement.executeQuery();

            // .next() → move the cursor to the first row of the result set
            result.next();

            // .getInt() → obtain the value of the first column in the row
            int count = result.getInt(1);

            userExists = (count > 0);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return userExists;
    }

    public void updateUser(String username, Connection con) {
        // Create update query
        String query = "Update users set password = ? where user_name = ?";

        System.out.println("Enter new password:  ");
        String newPassword = scanner.next();

        PreparedStatement statement = null;

        try {
            statement = con.prepareStatement(query);
            statement.setString(1, newPassword);
            statement.setString(2, username);
            statement.executeUpdate();

            messages.successMessage("Password updated successfully");

            startGame.difficultySelection();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
