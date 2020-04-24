package Controller.FunctionManager;

import Controller.EntityController;
import Controller.EntityManager.CreditCardController;
import Controller.EntityManager.GuestController;
import Controller.EntityManager.RoomController;
import Model.Guest.CreditCard;
import Model.Guest.Guest;
import Model.Room.BedType;
import Model.Room.Room;
import Model.Room.RoomType;
import Model.reservation.Reservation;
import Persistence.Persistence;
import View.View;
import Persistence.Entity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ReservationController extends EntityController<Reservation> {

    private static final String REGEX_NUMBERS = "[0-9]+";
    private static final String PATTERN_VALID_DATE = "d.MM.yyyy";
    private static final String PATTERN_PRINT_VALID_DATE = "dd.mm.yyyy";
    private static final String REGEX_VALID_DATE = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
    private static final String REGEX_BOOLEAN = "^(?:(0|1))$";
    private static final String REGEX_ONE_ALPHA_NUMERIC_CHARACTER = "^.*[a-zA-Z0-9]+.*$";

    private static final String CHECK_IN_DATE = "check in date";
    private static final String CHECK_OUT_DATE = "check out date";
    private static final String NUMBER_OF_ADULTS = "number of adults";
    private static final String NUMBER_OF_CHILDREN = "number of children";
    private static final String BED_TYPE = "bed type";
    private static final String ENABLED_WIFI = "enabled wifi ([1] Yes, [0] No)";
    private static final String WITH_VIEW = "with view  ([1] Yes, [0] No)";
    private static final String SMOKING = "smoking ([1] Yes, [0] No)";
    private static final String GUEST_NAME = "guest name";
    private static final String CONTINUE_RESERVATION = "whether you want to continue the reservation ([1] Yes, [0] No)";
    private static final String ABORT_RESERVATION = "The reservation is aborted.\n";
    private static final String CREATE_GUEST = "Create a guest before making a reservation with these guest details.\n\n";
    private static final String SEARCH_RESERVATION = "whether the reservation should be search by [1] reservation ID or [0] guest name";
    private static final String RESERVATION_ID = "reservation id";
    private static final String RESERVATION = "reservation";
    private static final String NUMBER_RESERVATION = "number of the reservation";

    private static final String NOT_FOUND = "not found";

    private final CreditCardController creditCardController;
    private final RoomController roomController;
    private final GuestController guestController;

    public ReservationController(Persistence persistence, CreditCardController creditCardController, RoomController roomController, GuestController guestController) {
        super(persistence);
        this.creditCardController = creditCardController;
        this.roomController = roomController;
        this.guestController = guestController;
    }

    @Override
    protected String getEntityName() {
        return "Reservation";
    }

    @Override
    public List<String> getOptions() {

        return Arrays.asList("Create a new reservation",
                "Update a reservation",
                "Search a reservation",
                "Remove a reservation",
                "Print all reservations");

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
                select(view);
                break;
            case 3:
                delete(view);
                break;
            case 4:
                show(view);
                break;
        }
    }

    @Override
    protected void create(View view) throws Exception {

        // get valid check-in date
        LocalDate checkInDate = view.getValidDate(CHECK_IN_DATE, PATTERN_VALID_DATE, PATTERN_PRINT_VALID_DATE, REGEX_VALID_DATE, LocalDate.now().minusDays(1));

        // break out of method
        if(checkInDate == null)
        {
            return;
        }

        // get valid check-out date
        LocalDate checkOutDate = view.getValidDate(CHECK_OUT_DATE, PATTERN_VALID_DATE, PATTERN_PRINT_VALID_DATE, REGEX_VALID_DATE, checkInDate);

        // break out of method
        if(checkOutDate == null)
        {
            return;
        }

        // get number of adults
        String stringNumberOfAdults = view.getInputRegex(NUMBER_OF_ADULTS, REGEX_NUMBERS);

        // break out of method
        if(stringNumberOfAdults == null)
        {
            return;
        }

        // convert number of adults to short
        short numberOfAdults = Short.parseShort(stringNumberOfAdults);

        // get number of adults
        String stringNumberOfChildren = view.getInputRegex(NUMBER_OF_CHILDREN, REGEX_NUMBERS);

        // break out of method
        if(stringNumberOfChildren == null)
        {
            return;
        }

        // convert number of children to short
        short numberOfChildren = Short.parseShort(stringNumberOfChildren);

        // get room type
        RoomType roomType = roomController.getRoomType(view);

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

        // get room meting the requirements
        Room room = roomController.getAvailableRoom(checkInDate, checkOutDate, roomType.getRoomTypeEnum(), bedType, enabledWifi, withView, smoking);

        // check whether a room is available
        if(room == null)
        {

            view.displayText("\nNo room with the selected requirements is available for the selected dates.\n\n");

            return;
        }

        // get room rate
        double roomRate = room.getRoomType().getRoomRate();

        // get the number of days of the stay
        long numberOfDays = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

        // format string
        String stringPrintCost = String.format("The total room cost will be %7.2f SGD (%5.2f SGD per night) for the selected dates.\n", roomRate*numberOfDays, roomRate);

        // print out expected cost for this room
        view.displayText(stringPrintCost);

        // check whether reservation should be continued
        String stringContinueReservation = view.getInputRegex(CONTINUE_RESERVATION, REGEX_BOOLEAN);

        // break out of function
        if(stringContinueReservation == null)
        {
            return;
        }

        // declare reservation boolean
        boolean continueReservation;

        // convert String to boolean
        continueReservation = "1".equals(stringContinueReservation);

        if(!continueReservation)
        {
            view.displayText(ABORT_RESERVATION);

            return;
        }

        // get guest
        Guest guest = guestController.select(view);

        // check whether guest was found
        if(guest == null)
        {
            view.displayText(CREATE_GUEST);

            return;

        }

        // get credit card of guest
        CreditCard creditCard = creditCardController.getCreditCard(view, guest);

        // create the reservation
        Reservation reservation = new Reservation(guest, creditCard, room, checkInDate, checkOutDate, numberOfAdults, numberOfChildren);

        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        // add guest to ArrayList of guests
        persistence.createCache(reservation, Reservation.class);

        // print reservation
        view.displayText(reservation.toString() + "\n\n");

        // display text
        view.displayText("\nThe reservation has been added to the system.\n\n\n");

    }

    @Override
    protected void update(View view) throws Exception {

        // search for guest via name
        Reservation reservation = select(view);

        //check whether guest was found
        if(reservation == null)
        {
            return;
        }

        // get valid check-in date
        LocalDate checkInDate = view.getValidDate(CHECK_IN_DATE, PATTERN_VALID_DATE, PATTERN_PRINT_VALID_DATE, REGEX_VALID_DATE, LocalDate.now().minusDays(1));

        // break out of method
        if(checkInDate == null)
        {
            return;
        }

        // get valid check-out date
        LocalDate checkOutDate = view.getValidDate(CHECK_OUT_DATE, PATTERN_VALID_DATE, PATTERN_PRINT_VALID_DATE, REGEX_VALID_DATE, checkInDate);

        // break out of method
        if(checkOutDate == null)
        {
            return;
        }

        // get number of adults
        String stringNumberOfAdults = view.getInputRegex(NUMBER_OF_ADULTS, REGEX_NUMBERS);

        // break out of method
        if(stringNumberOfAdults == null)
        {
            return;
        }

        // convert number of adults to short
        short numberOfAdults = Short.parseShort(stringNumberOfAdults);

        // get number of children
        String stringNumberOfChildren = view.getInputRegex(NUMBER_OF_CHILDREN, REGEX_NUMBERS);

        // break out of method
        if(stringNumberOfChildren == null)
        {
            return;
        }

        // convert number of children to short
        short numberOfChildren = Short.parseShort(stringNumberOfChildren);

        // get room type
        RoomType roomType = roomController.getRoomType(view);

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

        // get room meeting the requirements
        Room room = roomController.getAvailableRoom(checkInDate, checkOutDate, roomType.getRoomTypeEnum(), bedType, enabledWifi, withView, smoking);

        // check whether a room is available
        if(room == null)
        {

            view.displayText("\nNo room with the selected requirements is available for the selected dates.\n\n");

            return;
        }

        // get room rate
        double roomRate = room.getRoomType().getRoomRate();

        // get the number of days of the stay
        long numberOfDays = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

        // format string
        String stringPrintCost = String.format("The total room cost will be %7.2f SGD (%5.2f SGD per night) for the selected dates.\n", roomRate*numberOfDays, roomRate);

        // print out expected cost for this room
        view.displayText(stringPrintCost);

        // check whether reservation should be continued
        String stringContinueReservation = view.getInputRegex(CONTINUE_RESERVATION, REGEX_BOOLEAN);

        // break out of function
        if(stringContinueReservation == null)
        {
            return;
        }

        // declare reservation boolean
        boolean continueReservation;

        // convert String to boolean
        continueReservation = "1".equals(stringContinueReservation);

        if(!continueReservation)
        {
            view.displayText(ABORT_RESERVATION);

            return;
        }

        // get guest
        Guest guest = guestController.select(view);

        // check whether guest was found
        if(guest == null)
        {
            view.displayText(CREATE_GUEST);

            return;

        }

        // get credit card of guest
        CreditCard creditCard = creditCardController.getCreditCard(view, guest);

        // set everything if there were no errors
        // set check in date
        reservation.setCheckInDate(checkInDate);

        // set check out date
        reservation.setCheckOutDate(checkOutDate);

        // set number of adults
        reservation.setNumberOfAdults(numberOfAdults);

        // set number of children
        reservation.setNumberOfChildren(numberOfChildren);

        // set room
        reservation.setRoom(room);

        // set guest
        reservation.setGuest(guest);

        // set credit card
        reservation.setCreditCard(creditCard);

        // print reservation
        view.displayText(reservation.toString() + "\n\n");

        // display text
        view.displayText("\nThe reservation has been updated.\n\n\n");
    }

    @Override
    protected void delete(View view) throws Exception {
        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        try {

            // get all reservations
            ArrayList<Entity> reservations = persistence.retrieveAll(Reservation.class);

            // search for reservation
            Reservation reservation = select(view);

            //check whether reservation was found
            if (reservation == null) {
                return;
            }

            // remove reservation
            reservations.remove(reservation);

            view.displayText("The reservation has been removed.\n\n");
        }
        catch(Exception e)
        {

        }
    }

    @Override
    public Reservation select(View view) throws Exception {

        // initialize reservation
        Reservation reservation = null;

        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        try {

            // get all guests
            ArrayList<Entity> reservations = persistence.retrieveAll(Reservation.class);

            // check whether any guests exist
            if (reservations.size() == 0) {
                view.displayText("No reservation exists. Create a reservation before searching for the reservation.\n\n");

                return null;
            }

            // flag to check whether the entry of the guest name should be tried again
            boolean repeatEntry;

            //repeat
            do {
                // initialize repeat flag to false
                repeatEntry = false;

                // check whether reservation should be searched via reservation number of guest name
                String stringSearchReservation = view.getInputRegex(SEARCH_RESERVATION, REGEX_BOOLEAN);

                // break out of function
                if(stringSearchReservation == null)
                {
                    return null;
                }

                // declare search reservation boolean
                boolean searchReservation;

                // convert String to boolean
                searchReservation = "1".equals(stringSearchReservation);

                if(searchReservation)
                {
                    // get the reservation ID
                    String stringReservationID = view.getInputRegex(RESERVATION_ID, REGEX_NUMBERS);

                    // break out of method
                    if (stringReservationID == null) {
                        return null;
                    }

                    // convert String to long
                    long reservationID = Long.parseLong(stringReservationID);

                    // iterate through all reservations
                    for (Entity entity : reservations) {

                        // cast to room object
                        Reservation reservationIterator = (Reservation)entity;

                        // check whether room Number exists
                        if (reservationIterator.getIdentifier() == reservationID) {

                            // print reservation details
                            System.out.println(reservationIterator.toString() + "\n");

                            // get reservation
                            reservation = reservationIterator;

                            break;
                        }

                    }

                    // check whether a room was found
                    if (reservation == null) {

                        // check whether the entry of the room number should be tried again
                        repeatEntry = view.repeatEntry(RESERVATION_ID, NOT_FOUND);

                    }
                }
                else
                {
                    // get name of the guest
                    String guestName = view.getInputRegex(GUEST_NAME, REGEX_ONE_ALPHA_NUMERIC_CHARACTER);

                    // break out of function
                    if (guestName == null) {
                        return null;
                    }

                    // create ArrayList of reservations
                    ArrayList<Reservation> potentialReservations = new ArrayList<>();

                    // convert guest name to lower case
                    String guestNameLowerCase = guestName.toLowerCase();

                    // get words in name
                    String[] names = guestNameLowerCase.trim().split("\\s+");

                    // iterate through all reservations
                    for (Entity entity : reservations) {

                        // cast to reservation object
                        Reservation reservationIterator = (Reservation)entity;

                        // get guest of reservation
                        Guest guestIterator = reservationIterator.getGuest();

                        // flag to check whether all parts of the guests name are part of an existing guest
                        boolean isPartOfName = true;

                        // iterate through all names
                        for (String name : names) {

                            // check whether name is part of the guest's name
                            if (!guestIterator.getName().toLowerCase().contains(name)) {
                                // set flag to false
                                isPartOfName = false;
                                break;
                            }
                        }

                        // check whether all parts of the name match with the guests name
                        if (isPartOfName) {

                            // add guest to potential guests
                            potentialReservations.add(reservationIterator);
                        }
                    }

                    // check whether a reservation was found
                    if (potentialReservations.isEmpty()) {

                        // check whether the entry of the reservation's guest name should be tried again
                        repeatEntry = view.repeatEntry(RESERVATION, NOT_FOUND);

                    } else if (potentialReservations.size() == 1) {

                        // print reservation details
                        view.displayText(potentialReservations.get(0).toString() + "\n\n");

                        // get reservation
                        reservation = potentialReservations.get(0);
                    } else {

                        // select reservation from potential reservations
                        reservation = view.getInputArray(potentialReservations, NUMBER_RESERVATION, REGEX_NUMBERS);

                        // break out of method
                        if (reservation == null) {
                            return null;
                        }

                        // print reservation details
                        view.displayText(reservation.toString() + "\n\n");
                    }

                }

            }while(reservation == null && repeatEntry);

        }
        catch(Exception e)
        {

        }

        return reservation;
    }


    @Override
    public void show(View view) throws Exception{

        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        try {

            // get all reservations
            ArrayList<Entity> reservations = persistence.retrieveAll(Reservation.class);

            // check whether any reservation exist
            if (reservations.size() == 0) {
                view.displayText("No reservation exists. Create a reservation before printing the reservation details.\n\n");

                return;
            }

            view.displayText("The following reservations are on file:\n");

            // iterate through all reservations
            for (Entity entity : reservations) {

                // cast to reservation object
                Reservation reservation = (Reservation)entity;

                //print guest
                view.displayText(reservation.toString() + "\n");

            }

            view.displayText("\n");
        }
        catch(Exception e)
        {

        }

    }

    @Override
    protected boolean retrieve(View view) throws Exception {
        return true;
    }
}
