package cinema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database {

    // SQLite uses a file-based connection string.
    String url = "jdbc:sqlite::memory:";
    private Statement statement;
    private Connection connection;

    public Database() {
        try {
            // Establish the connection (database file is created automatically if it does not exist)
            connection = DriverManager.getConnection(url);
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

    public Connection getdbConnection(){
        return connection;
    }


    public boolean doesTableExist(Connection connection, String tableName) {
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, tableName);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
