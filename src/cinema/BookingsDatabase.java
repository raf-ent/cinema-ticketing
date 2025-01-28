package cinema;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class BookingsDatabase {

    public static void bookTicket(Database database, Scanner s, int userID) {
        System.out.println("Enter Movie ID (-1 to show all movies): ");
        int movieID = s.nextInt();
        while (movieID==-1) {
            MoviesDatabase.showMovies(database);
            System.out.println("Enter Movie ID (-1 to show all movies): ");
            movieID = s.nextInt();
        }

        System.out.println("Enter Show ID (-1 to show all shows): ");
        int showID = s.nextInt();
        while (showID==-1) {
            MoviesDatabase.showMovieShowTimes(database, movieID);
            System.out.println("Enter Show ID (-1 to show all shows): ");
            showID = s.nextInt();
        }

        System.out.println("Enter number of seats (int): ");
        int seats = s.nextInt();

        int bookingID = getNextBookingID(database, userID);

        Show show = MoviesDatabase.getShowTime(movieID, showID, database);
        show.setAvailableSeats(show.getAvailableSeats()-seats);

        String insert = "INSERT INTO `user "+userID+" - bookings`"
                + "(`ID`, `Seats`, `MovieID`, `ShowID`) VALUES "
                + "('"+bookingID+"','"+seats+"','"+movieID+"','"+showID+"');";

        String update = "UPDATE `movie "+movieID+" - shows` SET "
                + "`showTime`='"+show.getDate()+"  "+show.getTime()+"',"
                + "`capacity`='"+show.getCapacity()+"',"
                + "`availableSeats`='"+show.getAvailableSeats()+"',"
                + "`place`='"+show.getPlace()+"' WHERE `ID` = "+show.getID()+" ;";

        try {
            database.getStatement().execute(insert);
            database.getStatement().execute(update);
            System.out.println("Booked successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static ArrayList<Booking> getUserBookings(Database database, int userID) {
        ArrayList<Booking> bookings = new ArrayList<>();
        ArrayList<Integer> movieIDs = new ArrayList<>();
        ArrayList<Integer> showIDs = new ArrayList<>();
        String select = "SELECT * FROM `user "+userID+" - bookings`;";
        try {
            ResultSet rs = database.getStatement().executeQuery(select);
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setID(rs.getInt("ID"));
                booking.setSeats(rs.getInt("Seats"));
                int movieID = rs.getInt("MovieID");
                int showID = rs.getInt("ShowID");
                movieIDs.add(movieID);
                showIDs.add(showID);
                bookings.add(booking);
            }
            for (int i=0;i<bookings.size();i++) {
                Movie movie = MoviesDatabase.getMovie(movieIDs.get(i), database);
                Show show = MoviesDatabase.getShowTime(movieIDs.get(i), showIDs.get(i), database);
                bookings.get(i).setMovie(movie);
                bookings.get(i).setShow(show);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    private static Booking getBooking(int userID, int bookingID, Database database) {
        Booking booking = new Booking();
        String select = "SELECT `ID`, `Seats`, `MovieID`, `ShowID` FROM "
                + "`user "+userID+" - bookings` WHERE `ID` = "+bookingID+" ;";
        try {
            ResultSet rs = database.getStatement().executeQuery(select);
            rs.next();
            booking.setID(rs.getInt("ID"));
            booking.setSeats(rs.getInt("Seats"));
            int movieID = rs.getInt("MovieID");
            int showID = rs.getInt("ShowID");
            booking.setMovie(MoviesDatabase.getMovie(movieID, database));
            booking.setShow(MoviesDatabase.getShowTime(movieID, showID, database));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return booking;
    }

    private static int getNextBookingID(Database database, int userID) {
        int ID = 0;
        ArrayList<Booking> bookings = getUserBookings(database, userID);
        int size = bookings.size();
        if (size>0) {
            Booking lastBooking = bookings.get(size-1);
            ID = lastBooking.getID()+1;
        }
        return ID;
    }

    public static void showBookings(Database database, int userID) {
        System.out.println("\n------------------------------------------------");
        System.out.println("ID\tSeats\tMovie\tDate\t\tTime");
        ArrayList<Booking> bookings = getUserBookings(database, userID);
        for (Booking b : bookings) {
            b.print();
        }
        System.out.println("------------------------------------------------\n");
    }

    public static void cancelBooking(Database database, int userID, Scanner s) {
        System.out.println("Enter Booking ID (-1 to show all bookings): ");
        int bookingID = s.nextInt();
        while (bookingID==-1) {
            showBookings(database, userID);
            System.out.println("Enter Booking ID (-1 to show all bookings): ");
            bookingID = s.nextInt();
        }

        Booking booking = getBooking(userID, bookingID, database);

        Show show = booking.getShow();
        show.setAvailableSeats(show.getAvailableSeats()+booking.getSeats());

        String update = "UPDATE `movie "+booking.getMovie().getID()+" - shows` SET "
                + "`showTime`='"+show.getDate()+"  "+show.getTime()+"',"
                + "`capacity`='"+show.getCapacity()+"',"
                + "`availableSeats`='"+show.getAvailableSeats()+"',"
                + "`place`='"+show.getPlace()+"' WHERE `ID` = "+show.getID()+" ;";

        String delete = "DELETE FROM `user "+userID+" - bookings` "
                + "WHERE `ID` = "+bookingID+" ;";

        try {
            database.getStatement().execute(update);
            database.getStatement().execute(delete);
            System.out.println("Booking cancelled successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showMovieBookings(Database database, Scanner s) {
        System.out.println("Enter Movie ID (-1 to show all movies): ");
        int movieID = s.nextInt();
        while (movieID==-1) {
            MoviesDatabase.showMovies(database);
            System.out.println("Enter Movie ID (-1 to show all movies): ");
            movieID = s.nextInt();
        }

        System.out.println("Enter Show ID (-1 to show all shows): ");
        int showID = s.nextInt();
        while (showID==-1) {
            MoviesDatabase.showMovieShowTimes(database, movieID);
            System.out.println("Enter Show ID (-1 to show all shows): ");
            showID = s.nextInt();
        }

        ArrayList<Booking> bs = new ArrayList<>();
        ArrayList<Visitor> vs = new ArrayList<>();

        for (Visitor v : UsersDatabase.getAllVisitors(database)) {
            ArrayList<Booking> bookings = getUserBookings(database, v.getID());
            for (Booking b : bookings) {
                if (b.getMovie().getID()==movieID && b.getShow().getID()==showID) {
                    bs.add(b);
                    vs.add(v);
                }
            }
        }

        System.out.println("\n------------------------");
        System.out.println("User\t\tSeats");
        for (int i=0;i<bs.size();i++) {
            System.out.print(vs.get(i).getFirstName()+" "+vs.get(i).getLastName()+"\t");
            System.out.println(bs.get(i).getSeats());
        }
        System.out.println("------------------------\n");
    }

}
