package cinema;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Show {

    private int ID;
    private LocalDateTime showTime;
    private int capacity;
    private int availableSeats;
    private String place;

    public Show() {}

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public LocalDateTime getShowTime() {
        return showTime;
    }

    public void setShowTime(LocalDateTime showTime) {
        this.showTime = showTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDate() {
        DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return showTime.format(dateformatter);
    }

    public String getTime() {
        DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("HH:mm");
        return showTime.format(timeformatter);
    }

    public void print() {
        System.out.print(ID+"\t");
        System.out.print(getDate()+"\t");
        System.out.print(getTime()+"\t");
        System.out.print(capacity+"\t\t");
        System.out.print(availableSeats+"\t\t");
        System.out.print(place+"\n");
    }

}
