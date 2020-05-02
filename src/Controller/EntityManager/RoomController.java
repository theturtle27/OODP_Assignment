package Controller.EntityManager;

import Model.Room.*;
import Model.Stay.Stay;
import Model.Reservation.Reservation;
import Model.Reservation.ReservationStatus;
import Persistence.Entity;
import Controller.EntityController;
import Persistence.Persistence;
import View.View;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoomController extends EntityController<Room> {

    private static final String REGEX_NUMBERS = "[0-9]+";
    private static final String REGEX_BOOLEAN = "^(?:(0|1))$";
    private static final String REGEX_VALID_ROOM_NUMBER = "^(?:(0[1-9]|[1-9][0-9])-(0[1-9]|[1-9][0-9]))$";
    private static final String PATTERN_VALID_DATE = "d.MM.yyyy";
    private static final String REGEX_VALID_DATE = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";

    private static final String ROOM_NUMBER = "the room number (format: <2 digit floor level from 01>-<2 digit running room number from 01> [e.g. 02-07])";
    private static final String BED_TYPE = "bed type";
    private static final String ENABLED_WIFI = "whether Wifi is enabled :\n1) Yes\n0) No\n\nPlease select an option";
    private static final String WITH_VIEW = "whether the room has a view :\n1) Yes\n0) No\n\nPlease select an option";
    private static final String SMOKING = "whether this is a smoking friendly room :\n1) Yes\n0) No\n\nPlease select an option";
    private static final String START_DATE = "the start date (format: dd.mm.yyyy)";
    private static final String END_DATE = "the end date (format: dd.mm.yyyy)";
    private static final String MAINTENANCE_END_DATE = "the maintenance end date (format: dd.mm.yyyy)";

    private static final String NOT_UNIQUE = "The selected option is not unique.";
    private static final String NOT_FOUND = "The selected option cannot be found.";

    private final RoomTypeController roomTypeController;

    public RoomController(Persistence persistence, RoomTypeController roomTypeController)
    {
        super(persistence);
        this.roomTypeController = roomTypeController;
    }


    @Override
    protected String getEntityName() {
        return "Room";
    }

    @Override
    public List<String> getOptions() {

        return Arrays.asList("Create a new room",
                "Update a room",
                "Maintain a room",
                "Search a room",
                "Remove a room",
                "Check the availability of a room",
                "Print all rooms");

    }

    @Override
    protected void safeOnOptionSelected(View view, int option) throws Exception {
        switch (option) {
            case 0:
                create(view);
                break;
            case 1:
                update(view);
                break;
            case 2:
                updateRoomStatus(view);
                break;
            case 3:
                select(view);
                break;
            case 4:
                delete(view);
                break;
            case 5:
                checkRoomAvailability(view);
                break;
            case 6: show(view);
                break;
        }
    }

    @Override
    protected void create(View view) throws Exception {

        // get room number
        String roomNumber = uniqueRoomNumber(view);

        // break out of method
        if(roomNumber == null)
        {
            return;
        }

        // get room type
        RoomType roomType = roomTypeController.select(view);

        //check whether guest was found
        if(roomType == null)
        {
            return;
        }

        // get bed type
        BedType bedType = (BedType)view.getInputEnum(BedType.class, BED_TYPE, REGEX_NUMBERS);

        // break out of method
        if(bedType == null)
        {
            return;
        }

        // get enabled wifi
        String stringEnabledWifi = view.getInputRegex(ENABLED_WIFI, REGEX_BOOLEAN);

        // break out of function
        if(stringEnabledWifi == null)
        {
            return;
        }

        // declare enabled Wifi boolean
        boolean enabledWifi;

        // convert String to boolean
        enabledWifi = "1".equals(stringEnabledWifi);

        // get with view
        String stringWithView = view.getInputRegex(WITH_VIEW, REGEX_BOOLEAN);

        // break out of function
        if(stringWithView == null)
        {
            return;
        }

        // declare with view boolean
        boolean withView;

        // convert String to boolean
        withView = "1".equals(stringWithView);

        // get smoking
        String stringSmoking = view.getInputRegex(SMOKING, REGEX_BOOLEAN);

        // break out of function
        if(stringSmoking == null)
        {
            return;
        }

        // declare smoking boolean
        boolean smoking;

        // convert String to boolean
        smoking = "1".equals(stringSmoking);

        // create room
        Room room = new Room(roomNumber, roomType, bedType, enabledWifi, withView, smoking);

        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        // add room to ArrayList of rooms
        persistence.createCache(room, Room.class);

        // print room
        view.displayText(room.toString());

        // display text
        view.displayText("\n\nThe room has been added to the system.\n\n");

    }

    private String uniqueRoomNumber(View view)
    {

        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        // declare room number
        String roomNumber = null;

        try {

            // flag to check whether the entry of the guest name should be tried again
            boolean repeatEntry;

            // flag to check whether room number is unique
            boolean uniqueRoomNumber;

            // repeat as long as room number is not unique
            do {

                // initialize repeat flag to false
                repeatEntry = false;

                // flag to check whether room number is unique
                uniqueRoomNumber = true;

                // get the room number
                String testRoomNumber = view.getInputRegex(ROOM_NUMBER, REGEX_VALID_ROOM_NUMBER);

                // break out of function
                if (testRoomNumber == null) {
                    return null;
                }

                // get all rooms
                ArrayList<Entity> rooms = persistence.retrieveAll(Room.class);

                // iterate through all rooms to check whether the room number is unique
                for (Entity entity : rooms) {

                    // cast to guest object
                    Room room = (Room)entity;

                    // check whether room Number exists already
                    if (room.getRoomNumber().equals(testRoomNumber)) {

                        // room number is not unique
                        uniqueRoomNumber = false;

                        break;
                    }
                }

                // print error message if room number already exists and try again
                if (!uniqueRoomNumber) {

                    // check whether the entry of the room number should be tried again
                    repeatEntry = view.repeatEntry(NOT_UNIQUE);

                } else {
                    roomNumber = testRoomNumber;
                }


            } while (roomNumber == null && repeatEntry);

        }
        catch(Exception e)
        {

        }

        return roomNumber;
    }

    @Override
    protected void update(View view) throws Exception {

        // search for room via room number
        Room room = select(view);

        //check whether guest was found
        if(room == null)
        {
            return;
        }

        // get room type
        RoomType roomType = roomTypeController.select(view);

        //check whether room type was found
        if(roomType == null)
        {
            return;
        }

        // get bed type
        BedType bedType = (BedType)view.getInputEnum(BedType.class, BED_TYPE, REGEX_NUMBERS);

        // break out of method
        if(bedType == null)
        {
            return;
        }

        // get enabled wifi
        String stringEnabledWifi = view.getInputRegex(ENABLED_WIFI, REGEX_BOOLEAN);

        // break out of function
        if(stringEnabledWifi == null)
        {
            return;
        }

        // declare enabled Wifi boolean
        boolean enabledWifi;

        // convert String to boolean
        enabledWifi = "1".equals(stringEnabledWifi);

        // get with view
        String stringWithView = view.getInputRegex(WITH_VIEW, REGEX_BOOLEAN);

        // break out of function
        if(stringWithView == null)
        {
            return;
        }

        // declare with view boolean
        boolean withView;

        // convert String to boolean
        withView = "1".equals(stringWithView);

        // get smoking
        String stringSmoking = view.getInputRegex(SMOKING, REGEX_BOOLEAN);

        // break out of function
        if(stringSmoking == null)
        {
            return;
        }

        // declare smoking boolean
        boolean smoking;

        // convert String to boolean
        smoking = "1".equals(stringSmoking);

        // set everything if there were no errors
        // set room type
        room.setRoomType(roomType);

        // set bed type
        room.setBedType(bedType);

        // set enabled wifi
        room.setEnabledWifi(enabledWifi);

        // set with view
        room.setWithView(withView);

        // set smoking
        room.setSmoking(smoking);

        // print room
        view.displayText(room.toString());

        // display text
        view.displayText("\n\nThe room's details have been updated.\n\n");

    }

    private void updateRoomStatus(View view) throws Exception
    {

        // search for room via room number
        Room room = select(view);

        //check whether room was found
        if(room == null)
        {
            return;
        }

        // get start date
        LocalDate startDate = LocalDate.now();

        // get valid end date
        LocalDate endDate = view.getValidDate(MAINTENANCE_END_DATE, PATTERN_VALID_DATE, REGEX_VALID_DATE, LocalDate.now());

        // break out of method
        if(endDate == null)
        {
            return;
        }

        // get room availability
        Boolean roomAvailable = getRoomAvailability(room, startDate, endDate);

        // break out of method
        if(roomAvailable == null)
        {
            return;
        }

        // check whether room is available
        if(roomAvailable.booleanValue())
        {

            // set maintenance end date
            room.setMaintenanceEndDate(endDate);

            // set room status
            room.setRoomStatus(RoomStatus.UNDER_MAINTENANCE);

            // print room
            view.displayText(room.toString());

            // print
            view.displayText("\n\nThe room's status has been set to under maintenance.\n\n");

        }
        else
        {

            // print
            view.displayText("\nThe room's status cannot be set to under maintenance since the room is not available up to the maintenance end date.\n\n");

        }

    }

    @Override
    protected boolean retrieve(View view) throws Exception {

        return false;

    }

    private void checkRoomAvailability(View view) throws Exception
    {

        // search for room via room number
        Room room = select(view);

        //check whether room was found
        if(room == null)
        {
            return;
        }

        // get valid start date
        LocalDate startDate = view.getValidDate(START_DATE, PATTERN_VALID_DATE, REGEX_VALID_DATE, LocalDate.now().minusDays(1));

        // break out of method
        if(startDate == null)
        {
            return;
        }

        // get valid end date
        LocalDate endDate = view.getValidDate(END_DATE, PATTERN_VALID_DATE, REGEX_VALID_DATE, startDate);

        // break out of method
        if(endDate == null)
        {
            return;
        }

        // check whether room is available
        if(getRoomAvailability(room, startDate, endDate))
        {

            // print
            view.displayText("\nThe room is available for the selected dates.\n\n");

        }
        else
        {

            // print
            view.displayText("\nThe room is not available for the selected dates.\n\n");

        }

    }

    public boolean getRoomAvailability(Room room, LocalDate startDate, LocalDate endDate)
    {

        // get maintenance end date
        LocalDate maintenanceEndDate = room.getMaintenanceEndDate();

        // if maintenance end date exists
        if(!(maintenanceEndDate == null))
        {

            // check whether start date would clash with maintenance end date
            boolean maintenanceAfter = !(startDate.isBefore(maintenanceEndDate));

            // if the start would clash with the maintenance
            if(!maintenanceAfter)
            {
                return false;
            }

        }

        // get current stay for this room
        Stay stay = room.getStay();

        // if whether stay exists
        if(!(stay == null))
        {

            // get check-out date of the current stay
            LocalDate stayCheckOutDate = stay.getCheckOutDate();

            // check whether start date would clash with stay check out
            boolean stayAfter = !(startDate.isBefore(stayCheckOutDate));

            // if start date would clash with stay check out
            if(!stayAfter)
            {
                return false;
            }

        }

        // get existing reservations for this room
        ArrayList<Reservation> reservations = room.getReservations();

        // if reservations exist
        if(!(reservations == null))
        {

            // iterate through all reservations
            for(Reservation reservation : reservations)
            {
                // check only for reservations that are confirmed or checked-in
                if((reservation.getStatus() == ReservationStatus.CONFIRMED) || (reservation.getStatus() == ReservationStatus.CHECKED_IN)) {

                    // get check-in date of the reservation
                    LocalDate reservationCheckInDate = reservation.getCheckInDate();

                    // get check-out date of the reservation
                    LocalDate reservationCheckOutDate = reservation.getCheckOutDate();

                    // check whether end date would clash with existing reservation
                    boolean reservationBefore = !(endDate.isAfter(reservationCheckInDate));

                    // check whether start date of current reservation would clash with existing reservation
                    boolean reservationAfter = !(startDate.isBefore(reservationCheckOutDate));

                    // if the current reservation would clash with a different reservation then set vacancy flag of this room to false
                    if (!(reservationBefore || reservationAfter)) {
                        return false;
                    }
                }

            }
        }

        return true;

    }

    @Override
    protected void delete(View view) throws Exception
    {

        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        try {

            // get all guests
            ArrayList<Entity> rooms = persistence.retrieveAll(Room.class);

            // search for room via room number
            Room room = select(view);

            //check whether guest was found
            if (room == null) {
                return;
            }

            // remove room
            rooms.remove(room);

            view.displayText("The room has been removed.\n\n");
        }
        catch(Exception e)
        {

        }

    }

    @Override
    public Room select(View view) throws Exception {

        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        // initialize room
        Room room = null;

        try {

            // get all rooms
            ArrayList<Entity> rooms = persistence.retrieveAll(Room.class);

            // check whether any rooms exist
            if (rooms.size() == 0) {
                view.displayText("No room exists. Create a room before searching for the room's details.\n\n");

                return null;
            }

            // flag to check whether the entry of the guest name should be tried again
            boolean repeatEntry;

            //repeat
            do {
                // initialize repeat flag to false
                repeatEntry = false;

                // get the room number
                String roomNumber = view.getInputRegex(ROOM_NUMBER, REGEX_VALID_ROOM_NUMBER);

                // break out of method
                if (roomNumber == null) {
                    return null;
                }

                // iterate through all rooms
                for (Entity entity : rooms) {

                    // cast to room object
                    Room roomIterator = (Room)entity;

                    // check whether room Number exists
                    if (roomIterator.getRoomNumber().equals(roomNumber)) {

                        // print room details
                        System.out.println(roomIterator.toString() + "\n");

                        // get room
                        room = roomIterator;

                        break;
                    }

                }

                // check whether a room was found
                if (room == null) {

                    // check whether the entry of the room number should be tried again
                    repeatEntry = view.repeatEntry(NOT_FOUND);

                }


            } while (room == null && repeatEntry);
        } catch (Exception e) {

        }

        return room;
    }

    @Override
    public void show(View view) throws Exception {

        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        try {

            // get all rooms
            ArrayList<Entity> rooms = persistence.retrieveAll(Room.class);

            // check whether any rooms exist
            if (rooms.size() == 0) {
                view.displayText("No room exists. Create a room before printing the room's details.\n\n");

                return;
            }

            view.displayText("The following rooms are on file:\n");

            // iterate through all rooms
            for (Entity entity : rooms) {

                Room room = (Room)entity;

                //print room
                view.displayText(room.toString() + "\n");

            }

            view.displayText("\n");
        } catch (Exception e)
        {

        }
    }

    public Room getAvailableRoom(LocalDate checkInDate, LocalDate checkOutDate, RoomTypeEnum roomTypeEnum, BedType bedType, boolean enabledWifi, boolean withView, boolean smoking, boolean isWaitlist)
    {
        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        try {

            // get all rooms
            ArrayList<Entity> rooms = persistence.retrieveAll(Room.class);

            // iterate through all rooms
            for (Entity entity : rooms) {

                // cast to room object
                Room room = (Room)entity;

                // check whether room is available
                if (getRoomAvailability(room, checkInDate, checkOutDate) || isWaitlist) {

                    // check the room type
                    boolean isRoomType = room.getRoomType().getRoomTypeEnum() == roomTypeEnum;

                    // check bed type
                    boolean isBedType = room.getBedType() == bedType;

                    // check enabled wifi
                    boolean isEnabledWifi = room.getEnabledWifi() == enabledWifi;

                    // check with view
                    boolean isWithView = room.getWithView() == withView;

                    // check smoking
                    boolean isSmoking = room.getSmoking() == smoking;

                    // check whether all requirements are met
                    if (isRoomType && isBedType && isEnabledWifi && isWithView && isSmoking) {

                        return room;
                    }

                }

            }
        }
        catch(Exception e)
        {
            System.out.println("Exception");
        }

        return null;
    }

}
