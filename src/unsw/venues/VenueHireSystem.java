/**
 *
 */
package unsw.venues;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Venue Hire System for COMP2511.
 *
 * A basic prototype to serve as the "back-end" of a venue hire system. Input
 * and output is in JSON format.
 *
 * @author Robert Clifton-Everest
 *
 */
public class VenueHireSystem {

    /**
     * Constructs a venue hire system. Initially, the system contains no venues,
     * rooms, or bookings.
     */
    private ArrayList<Venue> venues;

    public VenueHireSystem() {
        // TODO Auto-generated constructor stub
        this.venues = new ArrayList<Venue>();
    }

    private void processCommand(JSONObject json) {
        switch (json.getString("command")) {

        case "room":
            String venue = json.getString("venue");
            String room = json.getString("room");
            String size = json.getString("size");
            addRoom(venue, room, size);
            break;

        case "request":
            String id = json.getString("id");
            LocalDate start = LocalDate.parse(json.getString("start"));
            LocalDate end = LocalDate.parse(json.getString("end"));
            int small = json.getInt("small");
            int medium = json.getInt("medium");
            int large = json.getInt("large");

            JSONObject result = request(id, start, end, small, medium, large);

            System.out.println(result.toString(2));
            break;

        // TODO Implement other commands
        case "change":
            id = json.getString("id");
            start = LocalDate.parse(json.getString("start"));
            end = LocalDate.parse(json.getString("end"));
            small = json.getInt("small");
            medium = json.getInt("medium");
            large = json.getInt("large");

            result = change(id, start, end, small, medium, large);

            System.out.println(result.toString(2));
            break;
        
        case "cancel":
            id = json.getString("id");
            cancel(id);
            break;

        case "list":
            venue = json.getString("venue");  
            JSONArray output = list(venue);

            System.out.println(output.toString(2));
            break;
        }
    }

    private void addRoom(String venue, String room, String size) {
        // TODO Process the room command
        // Instantize the room
        Room newRoom = new Room(room, size);
        // Loop through all recorded venues to check for  match
        for(int i = 0; i < venues.size(); i++){
            Venue v = venues.get(i);
            // If match is found, add the new room into the venue
            if(v.getName().equals(venue)){
                v.addRoom(newRoom);
                return;
            }
        }
        // If the match is not found, create new venue and add the room
        Venue newVenue = new Venue(venue);
        newVenue.addRoom(newRoom);
        venues.add(newVenue);
    }

    public JSONObject request(String id, LocalDate start, LocalDate end,
            int small, int medium, int large) {
        JSONObject result = new JSONObject();

        // TODO Process the request commmand
        int total = small + medium + large;
        ArrayList<Room> availableRooms = new ArrayList<Room>();
        // Go through all the available venues
        for(int i = 0; i < venues.size(); i++){
            Venue v = venues.get(i);
            availableRooms = new ArrayList<Room>();
            // Check if venue have enough rooms
            if(v.getRooms().size() >= total){
                // Check available small, medium, large rooms
                availableRooms = v.checkAvailability(start, end, availableRooms, small, "small");
                availableRooms = v.checkAvailability(start, end, availableRooms, medium, "medium");
                availableRooms = v.checkAvailability(start, end, availableRooms, large, "large");
                /*
                for(int j = 0; j < availableRooms.size(); j++){
                    Room r = availableRooms.get(j);
                    System.out.println(r.getName() + " is availeble");
                }*/
                
                if(availableRooms.size() < total){
                    // Continue the loop there are not enough rooms
                    continue;
                }else{
                    // Reservations are added to each room
                    Reservation resv = new Reservation(id, start, end, small, medium, large);
                    JSONArray rooms = new JSONArray();
                    for(int k = 0; k < availableRooms.size(); k++){
                        Room room = availableRooms.get(k);
                        rooms.put(availableRooms.get(k).getName());
                        room.addDates(start, end);
                        room.addResv(resv);
                    }
                    // Edit result
                    result.put("venue", v.getName());
                    result.put("rooms", rooms);
                    result.put("status", "success");
                    
                    return result;
                }
            }
        }
        result.put("status", "rejected");
        return result;
    }

    public JSONObject change (String id, LocalDate start, LocalDate end, int small, int medium, int large){
        JSONObject result = new JSONObject();
        // Store original reservation
        Reservation original = null;
        Venue v = null;
        for(int i = 0; i < venues.size(); i++){
            v = venues.get(i);
            if(v.findResv(id)){
                break;
            }
        }
        // Cancel the original reservation
        // Attempt to find suitable rooms
        // If failed then restore the original
        // If success, update
        if(v != null){
            original = v.getResv(id);
            if(original != null){
                cancel(id);
                result = request(id, start, end, small, medium, large);
                if(result.length() == 1){
                    JSONObject temp = request(original.getID(), original.getStartDate(), original.getEndDate(), original.getSmall(),
                                              original.getMedium(), original.getLarge());
                    result.put("status", "rejected");
                }
            }else{
                System.out.println("Original reservation not found");
            }
        }else{
            System.out.println("Venue not found");
        }

        return result;
    }

    public void cancel(String id){
        Venue v = null;
        for(int i = 0; i < venues.size(); i++){
            v = venues.get(i);
            if(v.findResv(id)){
                break;
            }
        }
        if(v != null){
            v.removeResv(id);
        }else{
            System.out.println("Reservation not found");
        }
    }

    public JSONArray list(String venue){
        JSONArray result = new JSONArray();
        Venue v = null;
        for(int i = 0; i < venues.size(); i++){
            if(venues.get(i).getName().equals(venue)){
                v = venues.get(i);
            }
        }

        if(v != null){
            ArrayList<Room> rooms = v.getRooms();
            for(int j = 0; j < rooms.size(); j++){
                JSONObject reservations = new JSONObject();
                Room room = rooms.get(j);
                ArrayList<Reservation> resv = room.getResv();
                JSONArray listofResv = new JSONArray();
                for(int k = 0; k < resv.size(); k++){
                    JSONObject eachResv = new JSONObject();
                    eachResv.put("start", resv.get(k).getStartDate());
                    eachResv.put("end", resv.get(k).getEndDate());
                    eachResv.put("id", resv.get(k).getID());
                    listofResv.put(eachResv);
                }
                reservations.put("reservations", listofResv);
                reservations.put("room", room.getName());
                result.put(reservations);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        VenueHireSystem system = new VenueHireSystem();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (!line.trim().equals("")) {
                JSONObject command = new JSONObject(line);
                system.processCommand(command);
            }
        }
        sc.close();
    }

}
