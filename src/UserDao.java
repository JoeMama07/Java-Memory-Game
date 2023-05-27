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

        System.out.println("ID:");
        String ID = scanner.next();

        // Validate if user already exists
        boolean userExists = validateUser(username, ID, con);

        if (userExists) {
            // Continue to the game
            System.out.println("continue");
        } else {
            System.out.println("Wrong id or username.");
            System.out.println("Do you wish to update your username or create a new account? Type 'update' for updating username or 'register' to create new account");
            String choose = scanner.next();

            if (choose.equals("update")) {
                updateUser(con);
            } else if (choose.equals("register")) {
                signup(con);
            }
        }
    }

    public void signup(Connection con) {
        // Create insert query
        String query = "Insert into users(user_name,birth_date) values (?,?)";

        // Query to get the user id
        String getUserID = "select id from users order by id desc limit 1";

        // Get input from user
        System.out.println("Username:");
        String userName = scanner.next();

        System.out.println("Birthdate: 'year-month-day'");
        String birthdate = scanner.next();

        // Prepare statement
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(query);

            // Replace '?' in query with user inputs
            statement.setString(1, userName);
            statement.setString(2, birthdate);
            statement.executeUpdate();

            // Continue to the game
            System.out.println("Register successful");

            // Get user id after data inserted into database
            ResultSet rs = statement.executeQuery(getUserID);
            while (rs.next()) {
                System.out.println("Your account code is: " + rs.getInt("id"));
                System.out.println("Use this code to login");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean validateUser(String username, String ID, Connection con) {
        boolean userExists = false;

        // Select query that returns the total users in the table
        String sql = "SELECT COUNT(*) FROM users WHERE user_name = ? AND id = ?";

        PreparedStatement statement = null;

        try {
            // Prepare a connection to the db with the query
            statement = con.prepareStatement(sql);

            // Replace '?' in sql with username parameter
            statement.setString(1, username);
            statement.setString(2, ID);

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

    public void updateUser(Connection con) {
        // Create update query
        String query = "Update users set user_name = ? where id = ?";

        System.out.println("What do you wish to change your username too?");
        String changedUsername = scanner.next();

        System.out.println("What is your account code?");
        String accountCode = scanner.next();

        PreparedStatement statement = null;

        try {
            statement = con.prepareStatement(query);
            statement.setString(1, changedUsername);
            statement.setString(2, accountCode);
            statement.executeUpdate();

            System.out.println("Username successfully update to " + changedUsername);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
