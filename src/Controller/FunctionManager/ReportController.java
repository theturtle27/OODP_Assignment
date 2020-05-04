package Controller.FunctionManager;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import Controller.PersistenceController;
import Model.Room.BedType;
import Model.Room.Room;
import Persistence.Entity;
import Model.Guest.Guest;
import Model.Room.RoomStatus;
import Model.Room.RoomTypeEnum;
import Persistence.Persistence;
import View.View;

public class ReportController extends PersistenceController {

    private static final String REGEX_NUMBERS = "[0-9]+";

    private static final String ROOM_TYPE = "room type";

    public ReportController(Persistence persistence) {
        super(persistence);
    }

    @Override
    public List<String> getOptions() {
        return Arrays.asList("Print the room status statistic report by room type occupancy rate",
                "Print the room status statistic report by room status");
    }

    @Override
    protected void safeOnOptionSelected(View view, int option) throws Exception {

        switch (option) {
            case 0:
                showRoomStatistic(view, true);
                break;
            case 1:
                showRoomStatistic(view, false);
                break;
        }
    }
//

    private HashMap<RoomTypeEnum, Integer> getNumberOfRoomsByRoomType(ArrayList<Entity> rooms) {
        // create a hash map to store the number of rooms of a room type
        HashMap<RoomTypeEnum, Integer> numberOfRoomsByRoomType = new HashMap<RoomTypeEnum, Integer>();

            // iterate through all room types
            for (RoomTypeEnum roomType : RoomTypeEnum.values()) {

                // iterator to count number of rooms of this room type
                int numberOfRoomsOfRoomType = 0;

                // iterate through all rooms
                for (Entity entity : rooms) {

                    // cast to room object
                    Room room = (Room) entity;

                    //check whether room type of this room is equal to the current room type
                    if (room.getRoomType().getRoomTypeEnum() == roomType) {
                        // increment the number of rooms of this room type
                        numberOfRoomsOfRoomType++;
                    }

                }

                // put the number of rooms of this room type into the hash map
                numberOfRoomsByRoomType.put(roomType, numberOfRoomsOfRoomType);

            }

        return numberOfRoomsByRoomType;

    }

    private HashMap<RoomTypeEnum, ArrayList<String>> getRoomNumbersByRoomType(ArrayList<Entity> rooms, RoomStatus roomStatus) {

        // create a hash map to store the room numbers of a room type
        HashMap<RoomTypeEnum, ArrayList<String>> roomNumbers = new HashMap<RoomTypeEnum, ArrayList<String>>();

            // iterate through all room types
            for (RoomTypeEnum roomType : RoomTypeEnum.values()) {

                // create ArrayList of Strings to store all room numbers of rooms of this room type
                ArrayList<String> roomNumbersOfRoomType = new ArrayList<String>();

                // iterate through all rooms
                for (Entity entity : rooms) {

                    // cast to room object
                    Room room = (Room) entity;

                    //check whether room type of this room is equal to the current room type
                    if (room.getRoomType().getRoomTypeEnum() == roomType && room.getStatus() == roomStatus) {
                        roomNumbersOfRoomType.add(room.getRoomNumber());
                    }

                }

                // put the ArrayList of room numbers of this room type into the hash map
                roomNumbers.put(roomType, roomNumbersOfRoomType);

            }

        return roomNumbers;

    }

    private HashMap<RoomStatus, ArrayList<String>> getRoomNumbersByRoomStatus(ArrayList<Entity> rooms) {

        // create a hash map to store the room numbers of a room type
        HashMap<RoomStatus, ArrayList<String>> roomNumbers = new HashMap<RoomStatus, ArrayList<String>>();

        // iterate through all room status
        for (RoomStatus roomStatus : RoomStatus.values()) {

            // create ArrayList of Strings to store all room numbers of rooms of this room status
            ArrayList<String> roomNumbersOfRoomStatus = new ArrayList<String>();

            // get room numbers of this room status by room type
            HashMap<RoomTypeEnum, ArrayList<String>> roomNumbersByRoomType = getRoomNumbersByRoomType(rooms, roomStatus);

            //iterate through all room types
            for (RoomTypeEnum roomType : RoomTypeEnum.values()) {

                // get room numbers of this room type with this room status
                ArrayList<String> roomNumbersOfRoomType = roomNumbersByRoomType.get(roomType);

                // add room numbers of this room status to ArrayList
                roomNumbersOfRoomStatus.addAll(roomNumbersOfRoomType);

            }

            // put the ArrayList of room numbers of this room status into the hash map
            roomNumbers.put(roomStatus, roomNumbersOfRoomStatus);

        }

        return roomNumbers;
    }

    public void showRoomStatistic(View view, boolean byRoomType) {

        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        try {

            // get all rooms
            ArrayList<Entity> rooms = persistence.retrieveAll(Room.class);

            // check whether any rooms exist
            if (rooms.size() == 0) {
                view.displayText("No room exists. Create a room before printing a room status report.\n\n");

                return;
            }


            // show room statistic by room type
            if (byRoomType) {

                // get bed type
                RoomStatus roomstatus = (RoomStatus) view.getInputEnum(RoomStatus.class, ROOM_TYPE, REGEX_NUMBERS);

                // break out of method
                if (roomstatus == null) {
                    return;
                }

                // get vacant room numbers by room type
                HashMap<RoomTypeEnum, ArrayList<String>> roomNumbersByRoomType = getRoomNumbersByRoomType(rooms, roomstatus);

                // get the number of rooms of each room type
                HashMap<RoomTypeEnum, Integer> numberOfRooms = getNumberOfRoomsByRoomType(rooms);

                System.out.println();

                // iterate through all room types
                for (RoomTypeEnum roomType : RoomTypeEnum.values()) {
                    // get vacant room numbers of this room type
                    ArrayList<String> roomNumbersOfRoomType = roomNumbersByRoomType.get(roomType);

                    // get the number of rooms of this room type
                    int numberOfRoomsOfRoomType = numberOfRooms.get(roomType);

                    // create String
                    String reportPrintHeader = String.format("%-9s : Number : %3d out of %-3d\n", view.capitalizeFirstLetter(roomType.toString()), roomNumbersOfRoomType.size(), numberOfRoomsOfRoomType);

                    // print room type and number of vacant rooms out of number of rooms of this room type
                    view.displayText(reportPrintHeader);
                    //System.out.printf("%-9s : Number : %3d out of %-3d\n", capitalizeFirstLetter(roomType.toString()), roomNumbersOfRoomType.size(), numberOfRoomsOfRoomType);

                    //check whether ArrayList contains room numbers
                    if (roomNumbersOfRoomType.size() > 0) {

                        // print
                        view.displayText("            Rooms  :");

                        // iterate through all room numbers until second to last one
                        for (int i = 0; i < roomNumbersOfRoomType.size() - 1; i++) {
                            // print room number
                            view.displayText(" " + roomNumbersOfRoomType.get(i) + ",");
                        }
                        // print last room number in ArrayList
                        view.displayText(" " + roomNumbersOfRoomType.get(roomNumbersOfRoomType.size() - 1) + "\n");
                    }

                }
                System.out.println();

            }

            // show room statistic by room status
            else {

                // get room numbers by room status
                HashMap<RoomStatus, ArrayList<String>> roomNumbersByRoomStatus = getRoomNumbersByRoomStatus(rooms);

                // iterate through all room status
                for (RoomStatus roomStatusIterator : RoomStatus.values()) {
                    // get room numbers of this room status
                    ArrayList<String> roomNumbersOfRoomStatus = roomNumbersByRoomStatus.get(roomStatusIterator);

                    // create String
                    String reportPrintHeader = String.format("%-17s : \n", view.capitalizeFirstLetter(roomStatusIterator.toString()));

                    // print room status
                    System.out.printf(reportPrintHeader);

                    //check whether ArrayList contains room numbers
                    if (roomNumbersOfRoomStatus.size() > 0) {
                        // print
                        view.displayText("         Rooms :");

                        // iterate through all room numbers until second to last one
                        for (int i = 0; i < roomNumbersOfRoomStatus.size() - 1; i++) {
                            // print room number
                            view.displayText(" " + roomNumbersOfRoomStatus.get(i) + ",");
                        }
                        // print last room number in ArrayList
                        view.displayText(" " + roomNumbersOfRoomStatus.get(roomNumbersOfRoomStatus.size() - 1) + "\n");
                    }

                }
                System.out.println();

            }
        } catch (Exception e) {

        }
    }
}

