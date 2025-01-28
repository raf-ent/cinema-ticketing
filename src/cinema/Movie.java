package cinema;


import java.util.ArrayList;

public class Movie {

    private int ID;
    private String Name;
    private String Language;
    private String Genre;
    private int RunningTime;
    private String Starring;
    private String Rating;
    private ArrayList<Show> shows;

    public Movie() {
        shows = new ArrayList<>();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String Language) {
        this.Language = Language;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String Genre) {
        this.Genre = Genre;
    }

    public int getRunningTime() {
        return RunningTime;
    }

    public void setRunningTime(int RunningTime) {
        this.RunningTime = RunningTime;
    }

    public String getStarring() {
        return Starring;
    }

    public void setStarring(String Starring) {
        this.Starring = Starring;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String Rating) {
        this.Rating = Rating;
    }

    public ArrayList<Show> getShows() {
        return shows;
    }

    public void setShows(ArrayList<Show> shows) {
        this.shows = shows;
    }

    public void print() {
        System.out.print(ID+"\t");
        System.out.print(Name+"\t");
        System.out.print(Language+"\t\t");
        System.out.print(Genre+"\t");
        System.out.print(RunningTime+" Minutes\t");
        System.out.print(Starring+"\t\t");
        System.out.print(Rating+"\n");
    }

}
