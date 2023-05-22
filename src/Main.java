import java.sql.*;
import java.util.Scanner;

public class Main {
    // Creating ENV VARIABLES
    static final String DB_URL = "jdbc:mysql://localhost:3306/memory_game";
    static final String USER = "root";
    static final String PASSWORD = "root";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        // Keep the application running
        while (isRunning) {
            System.out.println("Welcome to the Memory game");
            System.out.println("Type 'exit' if you want to close the application or 'continue' to play the game: ");
            String startingPoint = scanner.nextLine();

            // Check what the user wants to do
            if (startingPoint.equals("exit")) {
                isRunning = false;
            } else {
                UserDao userDao = new UserDao();
                userDao.dbConnection(DB_URL, USER, PASSWORD);
            }
        }
    }
}