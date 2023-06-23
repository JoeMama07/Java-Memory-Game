import Config.Config;
import UserDao.UserDao;

public class Main {
    public static void main(String[] args) {
        Config config = new Config();
        System.out.println("Welcome to the Memory game");
        UserDao userDao = new UserDao();
        userDao.dbConnection(config.DB_URL, config.USER, config.PASSWORD);
    }
}