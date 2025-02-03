package cinema;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatabaseTest {
    private Database db;

    @BeforeEach
    public void setup() {
        db = new Database();
    }

    @Test
    public void testMainTablesExist() {
        try {
            // Check Movies table
            ResultSet rs = db.getStatement().executeQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name='movies';");
            assertTrue(rs.next(), "Movies table should exist");
            // Check Visitors table
            rs = db.getStatement().executeQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name='visitors';");
            assertTrue(rs.next(), "Visitors table should exist");
            // Check Admins table
            rs = db.getStatement().executeQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name='admins';");
            assertTrue(rs.next(), "Admins table should exist");
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }
}

class MoviesDatabaseTest {
    private Database db;

    @BeforeEach
    public void setup() {
        db = new Database();
        try {
            db.getStatement().execute("DELETE FROM movies;");
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAddNewMovie() {
        String input = "TestMovie\nEnglish\nAction\n120\nTestActor\nPG\n";
        Scanner scanner = new Scanner(input);
        MoviesDatabase.addNewMovie(db, scanner);

        // Verify that the movie was inserted
        try {
            ResultSet rs = db.getStatement().executeQuery("SELECT * FROM movies WHERE Name = 'TestMovie';");
            assertTrue(rs.next(), "Movie should have been inserted");
            assertEquals("English", rs.getString("Language"), "Language should be English");
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateMovie() {
        // Add a movie
        String input = "TestMovie\nEnglish\nAction\n120\nTestActor\nPG\n";
        Scanner scanner = new Scanner(input);
        MoviesDatabase.addNewMovie(db, scanner);

        // Retrieve movie ID for the inserted movie
        int movieID = 0;
        try {
            ResultSet rs = db.getStatement().executeQuery("SELECT ID FROM movies WHERE Name = 'TestMovie';");
            if (rs.next()) {
                movieID = rs.getInt("ID");
            }
        } catch (SQLException e) {
            fail(e.getMessage());
        }

        String updateInput = movieID + "\nUpdatedMovie\n-1\n-1\n-1\n-1\n-1\n";
        Scanner updateScanner = new Scanner(updateInput);
        MoviesDatabase.updateMovie(db, updateScanner);

        // Verify update:
        try {
            ResultSet rs = db.getStatement().executeQuery("SELECT Name FROM movies WHERE ID = " + movieID + ";");
            if (rs.next()) {
                assertEquals("UpdatedMovie", rs.getString("Name"), "Movie name should be updated");
            }
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteMovie() {
        // Add a movie to delete
        String input = "DeleteMovie\nEnglish\nDrama\n90\nActor\nPG-13\n";
        Scanner scanner = new Scanner(input);
        MoviesDatabase.addNewMovie(db, scanner);

        int movieID = 0;
        try {
            ResultSet rs = db.getStatement().executeQuery("SELECT ID FROM movies WHERE Name = 'DeleteMovie';");
            if (rs.next()) {
                movieID = rs.getInt("ID");
            }
        } catch (SQLException e) {
            fail(e.getMessage());
        }

        // Delete movie
        String deleteInput = movieID + "\n";
        Scanner deleteScanner = new Scanner(deleteInput);
        MoviesDatabase.deleteMovie(db, deleteScanner);

        try {
            ResultSet rs = db.getStatement().executeQuery("SELECT * FROM movies WHERE ID = " + movieID + ";");
            assertFalse(rs.next(), "Movie should have been deleted");
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }
}

class UsersDatabaseTest {
    private Database db;

    @BeforeEach
    public void setup() {
        db = new Database();
        try {
            db.getStatement().execute("DELETE FROM visitors;");
            db.getStatement().execute("DELETE FROM admins;");
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAddVisitorAndLogin() {
        Visitor v = new Visitor();
        v.setID(UsersDatabase.getNextVisitorID(db));
        v.setFirstName("John");
        v.setLastName("Doe");
        v.setEmail("john@example.com");
        v.setPhoneNumber("1234567890");
        v.setPassword("password");

        UsersDatabase.addVisitor(v, db);

        // Check if email is marked as used
        boolean emailUsed = UsersDatabase.isEmailUsed("john@example.com", db);
        assertTrue(emailUsed, "Email should be marked as used");

        // Test login with correct credentials
        boolean loginSuccess = UsersDatabase.login("john@example.com", "password", db);
        assertTrue(loginSuccess, "Login should succeed with correct credentials");

        // Test login with wrong password
        boolean loginFail = UsersDatabase.login("john@example.com", "wrong", db);
        assertFalse(loginFail, "Login should fail with wrong credentials");
    }

    @Test
    public void testGetNextVisitorID() {
        // When table is empty, next ID should be 0
        int nextID = UsersDatabase.getNextVisitorID(db);
        assertEquals(0, nextID, "Next visitor ID should be 0 when table is empty");

        Visitor v = new Visitor();
        v.setID(nextID);
        v.setFirstName("Alice");
        v.setLastName("Smith");
        v.setEmail("alice@example.com");
        v.setPhoneNumber("5555555555");
        v.setPassword("secret");
        UsersDatabase.addVisitor(v, db);

        int nextIDAfter = UsersDatabase.getNextVisitorID(db);
        assertEquals(v.getID() + 1, nextIDAfter, "Next visitor ID should increment by 1");
    }
}

class BookingsDatabaseTest {
    private Database db;

    @BeforeEach
    public void setup() {
        db = new Database();
        try {
            db.getStatement().execute("DELETE FROM visitors;");
            db.getStatement().execute("DELETE FROM movies;");
            db.getStatement().execute("DELETE FROM admins;");
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testBookAndCancelTicket() {
        // Create a movie
        String movieInput = "BookingMovie\nEnglish\nAction\n120\nActor\nPG\n";
        Scanner movieScanner = new Scanner(movieInput);
        MoviesDatabase.addNewMovie(db, movieScanner);

        int movieID = 0;
        try {
            ResultSet rs = db.getStatement().executeQuery("SELECT ID FROM movies WHERE Name = 'BookingMovie';");
            if (rs.next()) {
                movieID = rs.getInt("ID");
            }
        } catch (SQLException e) {
            fail(e.getMessage());
        }

        // Create a show for the movie
        String showInput = movieID + "\n2025-02-03\n18:00\n100\nTestPlace\n";
        Scanner showScanner = new Scanner(showInput);
        MoviesDatabase.addShowTime(db, showScanner);

        int showID = 0;
        try {
            ResultSet rs = db.getStatement().executeQuery("SELECT ID FROM \"Movie " + movieID + " - Shows\";");
            if (rs.next()) {
                showID = rs.getInt("ID");
            }
        } catch (SQLException e) {
            fail(e.getMessage());
        }

        // Create a visitor
        Visitor v = new Visitor();
        v.setID(UsersDatabase.getNextVisitorID(db));
        v.setFirstName("Alice");
        v.setLastName("Smith");
        v.setEmail("alice@example.com");
        v.setPhoneNumber("5551234567");
        v.setPassword("secret");
        UsersDatabase.addVisitor(v, db);
        int visitorID = v.getID();

        // Book a ticket
        String bookingInput = movieID + "\n" + showID + "\n2\n";
        Scanner bookingScanner = new Scanner(bookingInput);
        BookingsDatabase.bookTicket(db, bookingScanner, visitorID);

        // Verify booking exists in the "User <ID> - Bookings" table.
        try {
            ResultSet rs = db.getStatement().executeQuery("SELECT * FROM \"User " + visitorID + " - Bookings\";");
            assertTrue(rs.next(), "Booking should exist for visitor");
            int seats = rs.getInt("Seats");
            assertEquals(2, seats, "Booked seats should be 2");
        } catch (SQLException e) {
            fail(e.getMessage());
        }

        // Cancel the booking
        int bookingID = 0;
        try {
            ResultSet rs = db.getStatement().executeQuery("SELECT ID FROM \"User " + visitorID + " - Bookings\";");
            if (rs.next()) {
                bookingID = rs.getInt("ID");
            }
        } catch (SQLException e) {
            fail(e.getMessage());
        }
        String cancelInput = bookingID + "\n";
        Scanner cancelScanner = new Scanner(cancelInput);
        BookingsDatabase.cancelBooking(db, visitorID, cancelScanner);

        // Verify that the booking has been removed.
        try {
            ResultSet rs = db.getStatement().executeQuery("SELECT * FROM \"User " + visitorID + " - Bookings\";");
            assertFalse(rs.next(), "Booking should have been cancelled");
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }
}
