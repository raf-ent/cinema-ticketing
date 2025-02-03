package cinema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    // SQLite uses a file-based connection string.
    String url = "jdbc:sqlite:cinema-system.db"; // Timeout of 3 seconds
    private Statement statement;

    public Database() {
        try {
            // Establish the connection (database file is created automatically if it does not exist)
            Connection connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            statement.execute("PRAGMA journal_mode = WAL;");

            // Initialize main tables if they do not exist
            initializeTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Statement getStatement() {
        return statement;
    }

    // Initialization routine: create main tables if they don't exist
    private void initializeTables() {
        try {
            // Create Movies table
            String createMovies = "CREATE TABLE IF NOT EXISTS movies (" +
                    "ID INTEGER PRIMARY KEY, " +
                    "Name TEXT, " +
                    "Language TEXT, " +
                    "Genre TEXT, " +
                    "\"Running Time\" INTEGER, " +
                    "Starring TEXT, " +
                    "Rating TEXT" +
                    ");";
            // Create Visitors table
            String createVisitors = "CREATE TABLE IF NOT EXISTS visitors (" +
                    "ID INTEGER PRIMARY KEY, " +
                    "firstName TEXT, " +
                    "lastName TEXT, " +
                    "email TEXT UNIQUE, " +
                    "phoneNumber TEXT, " +
                    "password TEXT" +
                    ");";
            // Create Admins table
            String createAdmins = "CREATE TABLE IF NOT EXISTS admins (" +
                    "ID INTEGER PRIMARY KEY, " +
                    "firstName TEXT, " +
                    "lastName TEXT, " +
                    "email TEXT UNIQUE, " +
                    "phoneNumber TEXT, " +
                    "password TEXT" +
                    ");";

            statement.execute(createMovies);
            statement.execute(createVisitors);
            statement.execute(createAdmins);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
