import java.sql.*;
import java.util.Scanner;

public class UserDao {
    Scanner scanner = new Scanner(System.in);

    public void dbConnection(String DB_URL, String USER, String PASSWORD) {

        System.out.println("If you have an account type 'login' or 'register' if you don't have an account");

        String authentication = scanner.nextLine();

        // Connect to db
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            // Create starting statement
            Statement statement = con.createStatement();

            if (authentication.equals("login")) {
                login(con);
            } else {
                signup(con);
            }


        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void login(Connection con) {
        // Get user inputs
        System.out.println("Username:");
        String username = scanner.next();

        System.out.println("Password:");
        String password = scanner.next();

        // Validate if user already exists
        boolean userExists = validateUser(username, con);
        if (userExists) {
            // Continue to the game
            System.out.println("continue");
        } else {
            signup(con);
        }
    }

    public void signup(Connection con) {
        // Create insert query
        String query = "Insert into users(user_name,password,birth_date) values (?,?,?)";

        // Get input from user
        System.out.println("Username:");
        String userName = scanner.next();

        System.out.println("Password:");
        String password = scanner.next();

        System.out.println("Birthdate: 'year-month-day'");
        String birthdate = scanner.next();

        boolean userExists = validateUser(userName, con);

        if (userExists) {
            System.out.println("You already have an account. Please login:");
            login(con);
        } else {

            // Prepare statement
            PreparedStatement statement = null;
            try {
                statement = con.prepareStatement(query);

                // Replace '?' in query with user inputs
                statement.setString(1, userName);
                statement.setString(2, password);
                statement.setString(3, birthdate);
                statement.executeUpdate();

                // Continue to the game
                System.out.println("Register successful");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean validateUser(String username, Connection con) {
        boolean userExists = false;

        // Select query that returns the total users in the table
        String sql = "SELECT COUNT(*) FROM users WHERE user_name = ?";

        PreparedStatement statement = null;

        try {
            // Prepare a connection to the db with the query
            statement = con.prepareStatement(sql);

            // Replace '?' in sql with username parameter
            statement.setString(1, username);

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

}
