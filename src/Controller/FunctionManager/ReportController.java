package Controller.FunctionManager;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import Controller.PersistenceController;
import Model.Room.Room;
import Persistence.Entity;
import Model.Guest.Guest;
import Model.Room.RoomStatus;
import Model.Room.RoomTypeEnum;
import Persistence.Persistence;
import View.View;

public class ReportController extends PersistenceController {

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
                showRoomStatistic(true);
                break;
            case 1:
                showRoomStatistic(false);
                break;
        }
    }
//

    private HashMap<RoomTypeEnum, Integer> getNumberOfRoomsByRoomType()
    {
        // create a hash map to store the number of rooms of a room type
        HashMap<RoomTypeEnum, Integer> numberOfRoomsByRoomType = new HashMap<RoomTypeEnum, Integer>();

        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        try {

            // get all rooms
            ArrayList<Entity> rooms = persistence.retrieveAll(Room.class);

            // iterate through all room types
            for (RoomTypeEnum roomType : RoomTypeEnum.values()) {
                // iterator to count number of rooms of this room type
                int numberOfRoomsOfRoomType = 0;

                // iterate through all rooms
                for (Entity entity : rooms) {

                    // cast to room object
                    Room room = (Room)entity;

                    //check whether room type of this room is equal to the current room type
                    if (room.getRoomType().getRoomTypeEnum() == roomType) {
                        // increment the number of rooms of this room type
                        numberOfRoomsOfRoomType++;
                    }

                }

                // put the number of rooms of this room type into the hash map
                numberOfRoomsByRoomType.put(roomType, numberOfRoomsOfRoomType);

            }
        }
        catch(Exception e)
        {

        }

        return numberOfRoomsByRoomType;

    }

    private HashMap<RoomTypeEnum, ArrayList<String>> getRoomNumbersByRoomType(RoomStatus roomStatus)
    {
        // create a hash map to store the room numbers of a room type
        HashMap<RoomTypeEnum, ArrayList<String>> roomNumbers = new HashMap<RoomTypeEnum, ArrayList<String>>();

        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        try {

            // get all rooms
            ArrayList<Entity> rooms = persistence.retrieveAll(Room.class);

            // iterate through all room types
            for(RoomTypeEnum roomType : RoomTypeEnum.values())
            {
                // create ArrayList of Strings to store all room numbers of rooms of this room type
                ArrayList<String> roomNumbersOfRoomType = new ArrayList<String>();

                // iterate through all rooms
                for(Entity entity : rooms)
                {

                    // cast to room object
                    Room room = (Room)entity;

                    //check whether room type of this room is equal to the current room type
                    if(room.getRoomType().getRoomTypeEnum() == roomType  && room.getRoomStatus() == roomStatus)
                    {
                        roomNumbersOfRoomType.add(room.getRoomNumber());
                    }

                }

                // put the ArrayList of room numbers of this room type into the hash map
                roomNumbers.put(roomType, roomNumbersOfRoomType);

            }

        }
        catch(Exception e)
        {

        }

        return roomNumbers;

    }

    private HashMap<RoomStatus, ArrayList<String>> getRoomNumbersByRoomStatus()
    {
        // create a hash map to store the room numbers of a room type
        HashMap<RoomStatus, ArrayList<String>> roomNumbers = new HashMap<RoomStatus, ArrayList<String>>();

        // iterate through all room status
        for(RoomStatus roomStatus : RoomStatus.values())
        {

            // create ArrayList of Strings to store all room numbers of rooms of this room status
            ArrayList<String> roomNumbersOfRoomStatus = new ArrayList<String>();

            // get room numbers of this room status by room type
            HashMap<RoomTypeEnum, ArrayList<String>> roomNumbersByRoomType = getRoomNumbersByRoomType(roomStatus);

            //iterate through all room types
            for(RoomTypeEnum roomType : RoomTypeEnum.values())
            {

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

    public void showRoomStatistic(boolean byRoomType)
    {
        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        try {

            // get all rooms
            ArrayList<Entity> rooms = persistence.retrieveAll(Room.class);

            // check whether any rooms exist
            if (rooms.size() == 0) {
                System.out.print("No room exists. Create a room before printing a room status report.\n\n");

                return;
            }


            // show room statistic by room type
            if (byRoomType) {

                // get vacant room numbers by room type
                HashMap<RoomTypeEnum, ArrayList<String>> roomNumbersByRoomType = getRoomNumbersByRoomType(RoomStatus.VACANT);

                // get the number of rooms of each room type
                HashMap<RoomTypeEnum, Integer> numberOfRooms = getNumberOfRoomsByRoomType();

                // iterate through all room types
                for (RoomTypeEnum roomType : RoomTypeEnum.values()) {
                    // get vacant room numbers of this room type
                    ArrayList<String> roomNumbersOfRoomType = roomNumbersByRoomType.get(roomType);

                    // get the number of rooms of this room type
                    int numberOfRoomsOfRoomType = numberOfRooms.get(roomType);

                    // print room type and number of vacant rooms out of number of rooms of this room type
                    System.out.printf("%-9s : Number : %3d out of %-3d\n", capitalizeFirstLetter(roomType.toString()), roomNumbersOfRoomType.size(), numberOfRoomsOfRoomType);

                    //check whether ArrayList contains room numbers
                    if (roomNumbersOfRoomType.size() > 0) {
                        // print
                        System.out.print("            Rooms  :");

                        // iterate through all room numbers until second to last one
                        for (int i = 0; i < roomNumbersOfRoomType.size() - 1; i++) {
                            // print room number
                            System.out.print(" " + roomNumbersOfRoomType.get(i) + ",");
                        }
                        // print last room number in ArrayList
                        System.out.print(" " + roomNumbersOfRoomType.get(roomNumbersOfRoomType.size() - 1) + "\n");
                    }

                }

            }

            // show room statistic by room status
            else {

                // get room numbers by room status
                HashMap<RoomStatus, ArrayList<String>> roomNumbersByRoomStatus = getRoomNumbersByRoomStatus();

                // iterate through all room status
                for (RoomStatus roomStatus : RoomStatus.values()) {
                    // get room numbers of this room status
                    ArrayList<String> roomNumbersOfRoomStatus = roomNumbersByRoomStatus.get(roomStatus);

                    // print room status
                    System.out.printf("%-17s : \n", capitalizeFirstLetter(roomStatus.toString()));

                    //check whether ArrayList contains room numbers
                    if (roomNumbersOfRoomStatus.size() > 0) {
                        // print
                        System.out.print("         Rooms :");

                        // iterate through all room numbers until second to last one
                        for (int i = 0; i < roomNumbersOfRoomStatus.size() - 1; i++) {
                            // print room number
                            System.out.print(" " + roomNumbersOfRoomStatus.get(i) + ",");
                        }
                        // print last room number in ArrayList
                        System.out.print(" " + roomNumbersOfRoomStatus.get(roomNumbersOfRoomStatus.size() - 1) + "\n");
                    }

                }

            }
        }
        catch(Exception e)
        {

        }
    }

    //TODO: method has to go somehere else
    // Method to convert the string
    public String capitalizeFirstLetter(String str) {
        StringBuffer s = new StringBuffer();

        // Declare a character of space
        // To identify that the next character is the starting
        // of a new word
        char ch = ' ';
        for (int i = 0; i < str.length(); i++) {

            // If previous character is space and current
            // character is not space then it shows that
            // current letter is the starting of the word
            if (ch == ' ' && str.charAt(i) != ' ')
            {
                ch = str.charAt(i);
                s.append(Character.toUpperCase(ch));
            }
            else if(str.charAt(i) == '_')
            {
                ch = ' ';
                s.append(ch);
            }
            else
            {
                ch = str.charAt(i);
                s.append(Character.toLowerCase(ch));
            }
        }

        // Return the string with trimming
        return s.toString().trim();
    }


//    /**
//     * Displays room occupancy report for today.
//     * @param view - A view interface that provides input/output.
//     * @throws Exception
//     */
//    private void viewReportForToday(View view) throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
//        view.message("----- Room Occupancy Report For " + sdf.format(new Date()) + " -----");
//
//        int occupiedRoomCount = printRoomsWithStatus(view, RoomStatus.Occupied);
//        int vacantRoomCount = printRoomsWithStatus(view, RoomStatus.Vacant);
//        int maintenanceRoomCount = printRoomsWithStatus(view, RoomStatus.Under_Maintenance);
//
//        view.message("\n--- Room Occupancy By Room Type ---");
//
//        for(RoomTypeEnum roomType: RoomTypeEnum.values())
//            printOccupancyByRoomType(view, roomType);
//
//
//        double occupancyRate = ((double)occupiedRoomCount / (occupiedRoomCount + vacantRoomCount + maintenanceRoomCount)) * 100;
//        view.message("\n----- Summary -----");
//        view.message("Number of occupied room(s): " + occupiedRoomCount);
//        view.message("Number of vacant room(s): " + vacantRoomCount);
//        view.message("Number of room(s) under maintenance: " + maintenanceRoomCount);
//        view.message("Percentage of room occupancy: " +
//                String.format("%.2f", occupancyRate) +
//                "%\n");
//    }
//
//    /**
//     * Displays room occupancy report for the specified date range.
//     * @param view - A view interface that provides input/output.
//     * @param startDate - The start date to generate the report.
//     * @param endDate - The end date to generate the report.
//     */
//    private void viewReportForRange(View view, Date startDate, Date endDate) throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
//        view.message("----- Room Occupancy Report(" + sdf.format(startDate) + " to " + sdf.format(endDate) + ") -----");
//
//        Persistence persistence = this.getPersistenceImpl();
////        long totalRoomCount = persistence.getCount(null, Room.class, false);
////        Iterable<Reservation> fulfilledReservations = persistence.search(new ReservationPredicate(Arrays.asList(ReservationStatus.CheckedIn, ReservationStatus.CheckedOut), startDate, endDate),
////                Reservation.class, false);
////
////        int millis = 0;
////        long startMillis = startDate.getTime();
////        long endMillis = endDate.getTime();
////        for(Reservation reservation: fulfilledReservations)
////            millis += Math.min(endMillis, reservation.getEndDate().getTime()) - Math.max(startMillis, reservation.getStartDate().getTime());
////
////        long interval = endDate.getTime() - startDate.getTime();
////        long totalTime = totalRoomCount * interval;
////        view.message("Percentage of room occupancy: " + String.format("%.2f", ((double) millis / totalTime))+ "%");
////        view.message("Number of no show or expired reservations: " +
////                persistence.getCount(new ReservationPredicate(Arrays.asList(ReservationStatus.Expired), startDate, endDate),
////                        Reservation.class, false));
////        view.message("Number of cancelled reservations: " +
////                persistence.getCount(new ReservationPredicate(Arrays.asList(ReservationStatus.Cancelled), startDate, endDate),
////                        Reservation.class, false));
//
//        RoomTypeEnum popular = getMostPopularRoomType(startDate, endDate);
//        if(popular != null)
////            view.message("Most popular room type: " + popular.getName());
//        view.message("");
//    }
//
//    /**
//     * Prints the rooms with the following status and returns the number of rooms printed.
//     * @param view - A view interface that provides input/output.
//     * @param status - The rooms with this status will be printed out.
//     * @return The number of rooms with the specified status.
//     */
//    private int printRoomsWithStatus(View view, RoomStatus status) throws Exception {
////        Persistence persistence = this.getPersistenceImpl();
////
////        String message = "";
//        int count = 0;
////        Iterable<Room> vacantRooms = persistence.search(new Predicate<Room>() {
////
////            @Override
////            public boolean test(Room item) {
////                return item.getStatus() == status;
////            }
////
////        }, Room.class, false);
////        message = status + " Room(s): ";
////        for(Room room: vacantRooms) {
////            message += room.getNumber() + " ";
////            count++;
////        }
////        if(count == 0)
////            message += "None";
////        view.message(message);
////
//        return count;
//    }
//
//    /**
//     * Prints the rooms with this room type and is occupied.
//     * @param view - A view interface that provides input/output.
//     * @param roomType - The occupied rooms with this {@link RoomTypeEnum} to print.
//     */
//    private void printOccupancyByRoomType(View view, RoomTypeEnum roomType) throws Exception {
////        Persistence persistence = this.getPersistenceImpl();
//////        Iterable<Room> rooms = persistence.search(new Predicate<Room>() {
////
////            @Override
////            public boolean test(Room item) {
////                return item.getType().equals(roomType);
////            }
////
////        }, Room.class, true);
////
////        long occupiedCount = 0;
////        long totalCount = 0;
////        String message = roomType.getName() + " rooms that are occupied: ";
////        for(Room room: rooms) {
////            totalCount++;
////            if(room.getStatus() == RoomStatus.Occupied) {
////                message += room.getNumber() + " ";
////                occupiedCount++;
////            }
////        }
////        if(occupiedCount == 0)
////            message += "None";
////        message += "\nTotal occupancy for " + roomType.getName() + " rooms: " + occupiedCount + " out of " + totalCount + "\n";
////
////        view.message(message);
//    }
//
//    /**
//     * Gets the most popular room type for the specified date range.
//     * @param startDate - Start date to search for most popular room type.
//     * @param endDate - End date to search for most popular room type.
//     * @return A RoomType instance that is the most popular in the specified date range.
//     * @throws Exception
//     */
//    private RoomTypeEnum getMostPopularRoomType(Date startDate, Date endDate) throws Exception {
//        RoomTypeEnum popular = null;
//
//        Map<RoomTypeEnum, Integer> map = new HashMap<RoomTypeEnum, Integer>();
//        Persistence persistence = this.getPersistenceImpl();
////        Iterable<Reservation> reservations = persistence.search(new ReservationPredicate(Arrays.asList(ReservationStatus.values()), startDate, endDate),
////                Reservation.class, false);
//
////        for(Reservation reservation: reservations) {
////            RoomType roomType = reservation.getCriteria().getRoomType();
////            if(roomType != null) {
////                int count = 1;
////                if(map.containsKey(roomType))
////                    count = count + map.get(roomType);
////
////                map.put(roomType, count);
////            }
////        }
//
//        int max = 0;
//        for(RoomTypeEnum roomType: map.keySet()) {
//            int count = map.get(roomType);
//            if(count > max) {
//                max = count;
//                popular = roomType;
//            }
//        }
//
//        return popular;
//    }
}
