package unsw.venues;

import java.time.LocalDate;

public class Reservation {
    
    private String id;
    private LocalDate startDate;
    private LocalDate endDate;
    private int small;
    private int medium;
    private int large;

    public Reservation(String id, LocalDate startDate, LocalDate endDate, int small, int medium, int large){
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.small = small;
        this.medium = medium;
        this.large = large;
    }

    public int getSmall(){
        return small;
    }

    public int getMedium(){
        return medium;
    }

    public int getLarge(){
        return large;
    }

    public String getID(){
        return id;
    }

    public LocalDate getStartDate(){
        return startDate;
    }

    public LocalDate getEndDate(){
        return endDate;
    }
}
