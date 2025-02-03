package cinema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {

    private String url = "jdbc:sqlite:cinema-system.db";
    private Statement statement;

    public Database() {
        try {
            Connection connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Statement getStatement() {
        return statement;
    }

}
