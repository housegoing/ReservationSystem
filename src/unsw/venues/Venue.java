package unsw.venues;

import java.util.ArrayList;
import java.time.LocalDate;

public class Venue {
    
    private String name;
    private ArrayList<Room> rooms;

    public Venue(String name){
        this.name = name;
        this.rooms = new ArrayList<Room>();
    }

    public ArrayList<Room> checkAvailability(LocalDate startDate, LocalDate endDate, ArrayList<Room> list, int count, String size){
        // Receive a list which will record the available rooms
        // Count indicates the number of rooms needed for the size
        ArrayList<Room> availableRooms = list;
        for(int i = 0; i < rooms.size(); i++){
            // If the number of room required are met, then exit
            if(count == 0){
                break;
            }
            Room room = rooms.get(i);
            if(room.getSize().equals(size)){
                // Add the room to the list if it has no reservations
                // Reduce count
                if(room.getResvStartDates().size() == 0){
                    availableRooms.add(room);
                    count--;
                }else{
                    // If the room has reservations, check for date collisions
                    // If no collisions then add to list
                    if(room.checkDateCollision(startDate, endDate)){
                        availableRooms.add(room);
                        count--;
                    }
                }
            }
        }
        return availableRooms;
    }

    public boolean findResv(String id){
        // Loop through all the rooms to find target reservation
        for(int i = 0; i < rooms.size(); i++){
            Room room = rooms.get(i);
            if(room.findResv(id)){
                return true;
            }
        }
        return false;
    }
    
    public void removeResv(String id){
        // Remove target reservation
        for(int i = 0; i < rooms.size(); i++){
            rooms.get(i).removeResv(id);
        }
    }

    public Reservation getResv(String id){
        // Get target reservation 
        Room room = null;
        for(int i = 0; i < rooms.size(); i++){
            room = rooms.get(i);
            if(room.findResv(id)){
                break;
            }
        }
        if(room != null){
            return room.getResv(id);
        }
        return null;
    }

    public void addRoom(Room room){
        rooms.add(room);
    }

    public String getName(){
        return name;
    }

    public ArrayList<Room> getRooms(){
        return rooms;
    }
}
