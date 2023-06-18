import UserDao.UserDao;

public class Main {
    // Creating ENV VARIABLES
    static final String DB_URL = "jdbc:mysql://localhost:3306/memory_game";
    static final String USER = "root";
    static final String PASSWORD = "root";

    public static void main(String[] args) {
        System.out.println("Welcome to the Memory game");
        UserDao userDao = new UserDao();
        userDao.dbConnection(DB_URL, USER, PASSWORD);
    }
}