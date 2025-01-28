package cinema;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class MoviesDatabase {

    public static void addNewMovie(Database database, Scanner s) {

        System.out.println("Enter Movie Name: ");
        String name = s.next();
        System.out.println("Enter Movie Language: ");
        String language = s.next();
        System.out.println("Enter Movie Genre: ");
        String genre = s.next();
        System.out.println("Enter Movie Running Time (int) : ");
        int runningTime = s.nextInt();
        System.out.println("Enter Movie Starring: ");
        String starring = s.next();
        System.out.println("Enter Movie Rating: ");
        String rating = s.next();
        int ID = getNextID(database);

        String insert = "INSERT INTO `movies`(`ID`, `Name`, `Language`, `Genre`,"
                + " `Running Time`, `Starring`, `Rating`) VALUES ('"+ID+"','"+name+
                "','"+language+"','"+genre+"','"+runningTime+"','"+starring+"','"+rating
                +"');";

        String create = "CREATE TABLE `Movie "+ID+" - Shows` "
                + "(ID int, showTime text, capacity int, "
                + "availableSeats int, place text);";
        try {
            database.getStatement().execute(insert);
            database.getStatement().execute(create);
            System.out.println("Movie added successfully\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<Movie> getAllMovies(Database database) {
        ArrayList<Movie> movies = new ArrayList<>();
        String select = "SELECT * FROM `movies`;";
        try {
            ResultSet rs = database.getStatement().executeQuery(select);
            while (rs.next()) {
                Movie m = new Movie();
                m.setID(rs.getInt("ID"));
                m.setName(rs.getString("Name"));
                m.setLanguage(rs.getString("Language"));
                m.setGenre(rs.getString("Genre"));
                m.setRunningTime(rs.getInt("Running Time"));
                m.setStarring(rs.getString("Starring"));
                m.setRating(rs.getString("Rating"));
                movies.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public static int getNextID(Database database) {
        int ID = 0;
        ArrayList<Movie> movies = getAllMovies(database);
        int size = movies.size();
        if (size!=0) {
            Movie lastMovie = movies.get(size-1);
            ID = lastMovie.getID()+1;
        }
        return ID;
    }

    public static void updateMovie(Database database, Scanner s) {
        System.out.println("Enter Movie ID (int) (-1 to show all movies): ");
        int ID = s.nextInt();
        while (ID==-1) {
            showMovies(database);
            System.out.println("Enter Movie ID (int) (-1 to show all movies) : ");
            ID = s.nextInt();
        }

        Movie movie = getMovie(ID, database);

        System.out.println("Enter Movie Name (-1 to keep old value): ");
        String name = s.next();
        if (!name.equals("-1")) movie.setName(name);

        System.out.println("Enter Movie Language (-1 to keep old value): ");
        String language = s.next();
        if (!name.equals("-1")) movie.setLanguage(language);

        System.out.println("Enter Movie Genre (-1 to keep pld value): ");
        String genre = s.next();
        if (!genre.equals("-1")) movie.setGenre(genre);

        System.out.println("Enter Movie Running Time (-1 to keep old value): ");
        int runningTime = s.nextInt();
        if (runningTime!=-1) movie.setRunningTime(runningTime);

        System.out.println("Enter Movie Starring (-1 to keep old value): ");
        String starring = s.next();
        if (!starring.equals("-1")) movie.setStarring(starring);

        System.out.println("Enter Movie Rating (-1 to keep old value): ");
        String rating = s.next();
        if (!rating.equals("-1")) movie.setRating(rating);

        String update = "UPDATE `movies` SET `Name`='"+movie.getName()+"',"
                + "`Language`='"+movie.getLanguage()+"',"
                + "`Genre`='"+movie.getGenre()+"',"
                + "`Running Time`='"+movie.getRunningTime()+"',"
                + "`Starring`='"+movie.getStarring()+"',"
                + "`Rating`='"+movie.getRating()+"'"
                + " WHERE `ID` = "+movie.getID()+" ;";
        try {
            database.getStatement().execute(update);
            System.out.println("Movie updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showMovies(Database database) {
        System.out.println("---------------------------------------------------------------------------------");
        System.out.println("ID\tName\tLanguage\tGenre\tRunning Time\tStarring\tRating");
        for (Movie m : getAllMovies(database)) {
            m.print();
        }
        System.out.println("---------------------------------------------------------------------------------");
    }

    public static Movie getMovie(int ID, Database database) {
        Movie movie = new Movie();
        String select = "SELECT `ID`, `Name`, `Language`, `Genre`, `Running Time`,"
                + " `Starring`, `Rating` FROM `movies` WHERE `ID` = "+ID+" ;";
        try {
            ResultSet rs = database.getStatement().executeQuery(select);
            rs.next();
            movie.setID(rs.getInt("ID"));
            movie.setName(rs.getString("Name"));
            movie.setLanguage(rs.getString("Language"));
            movie.setGenre(rs.getString("Genre"));
            movie.setRunningTime(rs.getInt("Running Time"));
            movie.setStarring(rs.getString("Starring"));
            movie.setRating(rs.getString("Rating"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movie;
    }

    public static void deleteMovie(Database database, Scanner s) {
        System.out.println("Enter Movie ID (-1 to show all movies): ");
        int ID = s.nextInt();
        while (ID==-1) {
            showMovies(database);
            System.out.println("Enter Movie ID (-1 to show all movies): ");
            ID = s.nextInt();
        }

        ArrayList<Show> shows = getAllMovieShows(database, ID);
        for (Show show : shows) {
            if (show.getAvailableSeats()<show.getCapacity()) {
                System.out.println("There is a booking on this movie in show: "+show.getID());
                return;
            }
        }

        String delete = "DELETE FROM `movies` WHERE `ID` = "+ID+" ;";
        String drop = "DROP TABLE `Movie "+ID+" - Shows`;";
        try {
            database.getStatement().execute(delete);
            database.getStatement().execute(drop);
            System.out.println("Movie deleted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addShowTime(Database database, Scanner s) {
        System.out.println("Enter Movie ID (-1 to show all movies): ");
        int movieID = s.nextInt();
        while (movieID==-1) {
            showMovies(database);
            System.out.println("Enter Movie ID (-1 to show all movies): ");
            movieID = s.nextInt();
        }
        System.out.println("Enter Date (yyyy-MM-dd): ");
        String date = s.next();
        System.out.println("Enter Time (HH:mm): ");
        String time = s.next();
        System.out.println("Enter capacity (int): ");
        int capacity = s.nextInt();
        System.out.println("Enter place: ");
        String place = s.next();

        int showID = getNextShowID(database, movieID);

        String insert = "INSERT INTO `movie "+movieID+" - shows`(`ID`, `showTime`, "
                + "`capacity`, `availableSeats`, `place`) VALUES ('"+showID+"',"
                + "'"+date+"  "+time+"','"+capacity+"','"+capacity+"',"
                + "'"+place+"');";

        try {
            database.getStatement().execute(insert);
            System.out.println("Show added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Show> getAllMovieShows(Database database, int movieID) {
        ArrayList<Show> shows = new ArrayList<>();
        String select = "SELECT * FROM `movie "+movieID+" - shows`;";
        try {
            ResultSet rs = database.getStatement().executeQuery(select);
            while (rs.next()) {
                Show show = new Show();
                show.setID(rs.getInt("ID"));
                String showTimeSt = rs.getString("showTime");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm");
                LocalDateTime showTime = LocalDateTime.parse(showTimeSt, formatter);
                show.setShowTime(showTime);
                show.setCapacity(rs.getInt("capacity"));
                show.setAvailableSeats(rs.getInt("availableSeats"));
                show.setPlace(rs.getString("place"));
                shows.add(show);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shows;
    }

    private static int getNextShowID(Database database, int movieID) {
        int ID = 0;
        ArrayList<Show> shows = getAllMovieShows(database, movieID);
        int size = shows.size();
        if (size>0) {
            int lastIndex = size-1;
            Show lastShow = shows.get(lastIndex);
            ID = lastShow.getID()+1;
        }
        return ID;
    }

    public static void showShowTimes(Database database, Scanner s) {
        System.out.println("Enter Movie ID (-1 to show all movies): ");
        int movieID = s.nextInt();
        while (movieID==-1) {
            showMovies(database);
            System.out.println("Enter Movie ID (-1 to show all movies): ");
            movieID = s.nextInt();
        }
        ArrayList<Show> shows = getAllMovieShows(database, movieID);
        System.out.println("\n----------------------------------------------------------------------");
        System.out.println("ID\tDate\t\tTime\tCapacity\tAvailable\tPlace");
        for (Show show : shows) {
            show.print();
        }
        System.out.println("----------------------------------------------------------------------\n");
    }

    public static void editShowTime(Database database, Scanner s) {
        System.out.println("Enter Movie ID (-1 to show all movies): ");
        int movieID = s.nextInt();
        while (movieID==-1) {
            showMovies(database);
            System.out.println("Enter Movie ID (-1 to show all movies): ");
            movieID = s.nextInt();
        }
        System.out.println("Enter Show ID (-1 to show all shows): ");
        int showID = s.nextInt();
        while (showID==-1) {
            showMovieShowTimes(database, movieID);
            System.out.println("Enter Show ID (-1 to show all shows): ");
            showID = s.nextInt();
        }

        Show show = getShowTime(movieID, showID, database);
        System.out.println("Enter Date (yyyy-MM-dd) (-1 to keep old value): ");
        String date = s.next();
        if (date.equals("-1")) {
            date = show.getDate();
        }
        System.out.println("Enter Time (HH:mm): ");
        String time = s.next();
        if (time.equals("-1")) {
            time = show.getTime();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm");
        LocalDateTime showTime = LocalDateTime.parse(date+"  "+time, formatter);
        show.setShowTime(showTime);

        System.out.println("Enter capacity (int): ");
        int capacity = s.nextInt();
        if (capacity!=-1) {
            int oldCapacity = show.getCapacity();
            int newSeats = capacity-oldCapacity;
            int newAvailable = show.getAvailableSeats()+newSeats;
            show.setAvailableSeats(newAvailable);
            show.setCapacity(capacity);
        }
        System.out.println("Enter place: ");
        String place = s.next();
        if (!place.equals("-1")) {
            show.setPlace(place);
        }

        String update = "UPDATE `movie "+movieID+" - shows` SET "
                + "`showTime`='"+show.getDate()+"  "+show.getTime()+"',"
                + "`capacity`='"+show.getCapacity()+"',"
                + "`availableSeats`='"+show.getAvailableSeats()+"',"
                + "`place`='"+show.getPlace()+"' WHERE `ID` = "+show.getID()+" ;";
        try {
            database.getStatement().execute(update);
            System.out.println("Show edited successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showMovieShowTimes(Database database, int movieID) {
        ArrayList<Show> shows = getAllMovieShows(database, movieID);
        System.out.println("\n----------------------------------------------------------------------");
        System.out.println("ID\tDate\t\tTime\tCapacity\tAvailable\tPlace");
        for (Show show : shows) {
            show.print();
        }
        System.out.println("----------------------------------------------------------------------\n");
    }

    public static Show getShowTime(int movieID, int showID, Database database) {
        Show show = new Show();
        String select = "SELECT `ID`, `showTime`, `capacity`, `availableSeats`,"
                + " `place` FROM `movie "+movieID+" - shows` WHERE `ID` = "+showID+" ;";
        try {
            ResultSet rs = database.getStatement().executeQuery(select);
            rs.next();
            show.setID(rs.getInt("ID"));
            String showTimeSt = rs.getString("showTime");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm");
            LocalDateTime showTime = LocalDateTime.parse(showTimeSt, formatter);
            show.setShowTime(showTime);
            show.setCapacity(rs.getInt("capacity"));
            show.setAvailableSeats(rs.getInt("availableSeats"));
            show.setPlace(rs.getString("place"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return show;
    }

    public static void deleteShow(Database database, Scanner s) {
        System.out.println("Enter Movie ID (-1 to show all movies): ");
        int movieID = s.nextInt();
        while (movieID==-1) {
            showMovies(database);
            System.out.println("Enter Movie ID (-1 to show all movies): ");
            movieID = s.nextInt();
        }

        System.out.println("Enter Show ID (-1 to show all shows): ");
        int showID = s.nextInt();
        while (showID==-1) {
            showMovieShowTimes(database, movieID);
            System.out.println("Enter Show ID (-1 to show all shows): ");
            showID = s.nextInt();
        }

        Show show = getShowTime(movieID, showID, database);
        if (show.getAvailableSeats()<show.getCapacity()) {
            System.out.println("There is a booking on this show!");
            return;
        }

        String delete = "DELETE FROM `movie "+movieID+" - shows` "
                + "WHERE `ID` = "+showID+" ;";
        try {
            database.getStatement().execute(delete);
            System.out.println("Show deleted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
