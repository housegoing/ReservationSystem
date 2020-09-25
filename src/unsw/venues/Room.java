package unsw.venues;

import java.util.ArrayList;

import java.time.LocalDate;

public class Room {
    
    private String name;
    private String size;
    private ArrayList<LocalDate> resvStartDates;
    private ArrayList<LocalDate> resvEndDates;
    private ArrayList<Reservation> resv;

    public Room(String name, String size){
        this.name = name;
        this.size = size;
        this.resvStartDates = new ArrayList<LocalDate>();
        this.resvEndDates = new ArrayList<LocalDate>();
        this.resv = new ArrayList<Reservation>();
    }

    public void addResv(Reservation resv){
        // Add reservation in order of dates
        for(int i = 0; i < this.resv.size(); i++){
            Reservation r = this.resv.get(i);
            if(r.getStartDate().compareTo(resv.getStartDate()) < 0){ continue; }
            this.resv.add(i, resv);
            return;
        }
        this.resv.add(resv);
    }

    public void addDates(LocalDate start, LocalDate end){
        // Add dates in order from earliest and latest
        for(int i = 0; i < resvStartDates.size(); i++){
            if(resvStartDates.get(i).compareTo(start) < 0){ continue; }
            resvStartDates.add(i, start);
            resvEndDates.add(i, end);
            return;
        }
        this.resvStartDates.add(start);
        this.resvEndDates.add(end);
    }

    public boolean checkDateCollision(LocalDate start, LocalDate end){
        // Ensure the algorithm works
        if(resvStartDates.size() != resvEndDates.size()){ return false; }
        // Check if given end date is earlier than first start date
        if(end.compareTo(resvStartDates.get(0)) < 0){
            return true;
        }
        for(int i = 0; i < resvStartDates.size(); i++){
            // Try to insert the date behind, continue if start date is smaller than end date
            if(start.compareTo(resvEndDates.get(i)) <= 0){ continue; }
            // If the end date is not the last date in array
            if(i != resvStartDates.size() - 1){
                // If the end date is earlier than next start date
                if(end.compareTo(resvStartDates.get(i+1)) < 0){
                    return true;
                }
            }else{
                // If the start date is later than the last end date
                return true;
            }
        }
      
        return false;
    }
    
    public boolean findResv(String id){
        for(int i = 0; i < resv.size(); i++){
            if(resv.get(i).getID().equals(id)){
                return true;
            }
        }
        return false;
    }

    public void removeResv(String id){
        for(int i = 0; i < resv.size(); i++){
            if(resv.get(i).getID().equals(id)){
                resvStartDates.remove(resv.get(i).getStartDate());
                resvEndDates.remove(resv.get(i).getEndDate());
                resv.remove(resv.get(i));
            }
        }
    }

    public String getName(){
        return name;
    }

    public String getSize(){
        return size;
    }

    public ArrayList<LocalDate> getResvStartDates(){
        return resvStartDates;
    }

    public ArrayList<LocalDate> getResvEndDates(){
        return resvEndDates;
    }

    public ArrayList<Reservation> getResv(){
        return resv;
    }

    public Reservation getResv(String id){
        Reservation r = null;
        for(int i = 0; i < resv.size(); i++){
            if(resv.get(i).getID().equals(id)){
                r = resv.get(i);
                break;
            }
        }
        return r;
    }
}
