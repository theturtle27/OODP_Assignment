package Controller.FunctionManager;

import Controller.EntityController;
import Controller.EntityManager.CreditCardController;
import Controller.EntityManager.GuestController;
import Controller.EntityManager.RoomController;
import Controller.EntityManager.RoomTypeController;
import Model.Guest.CreditCard;
import Model.Guest.Guest;
import Model.Payment.DiscountType;
import Model.Payment.Payment;
import Model.Payment.PaymentType;
import Model.Room.BedType;
import Model.Room.Room;
import Model.Room.RoomStatus;
import Model.Room.RoomType;
import Model.Stay.Stay;
import Model.Stay.StayStatus;
import Model.Reservation.Reservation;
import Model.Reservation.ReservationStatus;
import Persistence.Persistence;
import View.View;
import View.Options;
import Persistence.Entity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class StayController extends EntityController<Stay> {
    private static final String REGEX_NUMBERS = "[0-9]+";
    private static final String PATTERN_VALID_DATE = "d.MM.yyyy";
    private static final String REGEX_VALID_DATE = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
    private static final String REGEX_BOOLEAN = "^(?:(0|1))$";
    private static final String REGEX_ONE_ALPHA_NUMERIC_CHARACTER = "^.*[a-zA-Z0-9]+.*$";
    //private static final String REGEX_PERCENTAGE = "(^(100([.]0{1,2})?)$)|(^[0-9]{1,2}([.][0-9]{1,2})?$)";

    private static final String CHECK_OUT_DATE = "the check out date (format: dd.mm.yyyy)";
    private static final String NUMBER_OF_ADULTS = "the number of adults";
    private static final String NUMBER_OF_CHILDREN = "the number of children";
    private static final String BED_TYPE = "bed type";
    private static final String ENABLED_WIFI = "whether Wifi should enabled :\n1) Yes\n0) No\n\nPlease select an option";
    private static final String WITH_VIEW = "whether the room should have a view :\n1) Yes\n0) No\n\nPlease select an option";
    private static final String SMOKING = "whether this should be a smoking friendly room :\n1) Yes\n0) No\n\nPlease select an option";
    private static final String GUEST_NAME = "guest name";
    private static final String CONTINUE_STAY = "whether you want to continue the booking : \n1) Yes\n0) No\n\nPlease select an option";
    private static final String ABORT_STAY = "The booking is aborted.\n";
    private static final String CREATE_GUEST = "Create a guest before making a reservation with these guest details.\n\n";
    private static final String NUMBER_RESERVATION = "number of the reservation";

    private static final String HAVE_DISCOUNT = "whether you have a discount :\n1) Yes\n0) No\n\nPlease select an option";
    private static final String DISCOUNT_TYPE = "discount type";
    private static final String DISCOUNT_AMOUNT = "the discount amount";
    private static final String PAYMENT_TYPE = "payment type";
    private static final String WEEKEND_SURCHARGE = "the weekend surcharge in %%";
    private static final String TAX = "the GST in %%";

    private static final String CONTINUE_CHECK_IN = "whether you want to continue the check in of this reservation : \n1) Yes\n0) No\n\nPlease select an option";
    private static final String NOT_FOUND = "The selected option cannot be found.";

    private final GuestController guestController;
    private final CreditCardController creditCardController;
    private final ReservationController reservationController;
    private final RoomTypeController roomTypeController;
    private final RoomController roomController;

    public StayController(Persistence persistence , GuestController guestController, CreditCardController creditCardController, ReservationController reservationController, RoomController roomController, RoomTypeController roomTypeController) {
        super(persistence);
        this.guestController = guestController;
        this.creditCardController = creditCardController;
        this.reservationController = reservationController;
        this.roomController = roomController;
        this.roomTypeController = roomTypeController;
    }

    @Override
    public List<String> getOptions() {
        return Arrays.asList("Check in (Reservation)",
                "Check in (Walked In)",
                "Check out");
    }

    @Override
    protected String getEntityName() {
        return "Reservation Controller";
    }

    @Override
    protected void safeOnOptionSelected(View view, int option) throws Exception {
        switch (option) {
            case 0:
                checkInReservation(view);
                break;
            case 1:
                performCheckInWalkIn(view);
                break;
            case 2:
                performCheckOut(view);
                break;
        }
    }

    private void checkInReservation(View view) throws Exception{

        // get reservation
        Reservation reservation = reservationController.select(view);

        //check whether reservation was found
        if(reservation == null)
        {
            return;
        }

        if(LocalDate.now().isBefore(reservation.getCheckInDate()))
        {
            view.displayText("This reservation cannot be cecked in yet.\n\n");

            return;
        }

        // check whether check in should be continued with being put on waitlist
        String stringContinueCheckIn = view.getInputRegex(CONTINUE_CHECK_IN, REGEX_BOOLEAN);

        // break out of function
        if(stringContinueCheckIn == null)
        {
            return;
        }

        // declare reservation boolean
        boolean continueCheckIn;

        // convert String to boolean
        continueCheckIn = "1".equals(stringContinueCheckIn);

        if(continueCheckIn) {

            // get persistence
            Persistence persistence = this.getPersistenceImpl();

            try {

                // get all stays
                ArrayList<Entity> stays = persistence.retrieveAll(Stay.class);

                String message;

                if (reservation.getStatus() == ReservationStatus.CONFIRMED) {

                    // create stay
                    Stay stay = new Stay(reservation);

                    // add stay to persistence
                    stays.add(stay);

                    view.display(reservation);

                    message = "The above reservation has been checked-in successfully.";

                } else {

                    message = "We are unable to check-in for the reservation above as it is still in the wait list and there are no available rooms for the specified room requirements.";

                }

                //view.display(reservation);
                view.message("\n" + message + "\n");

            } catch (Exception e) {

            }
        }


    }

    private void performCheckOut(View view) throws Exception {

        Stay stay = select(view);

        //check whether reservation was found
        if(stay == null)
        {
            view.message("No checked-in room exists that is available for check-out.");

            return;
        }

        // check out
        checkOut(view, stay);

        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        try {
            // get all stays
            ArrayList<Entity> stays = persistence.retrieveAll(Stay.class);

            // set room to vacant
            stay.checkOut();

            // remove stay
            stays.remove(stay);
        }
        catch(Exception e)
        {

        }


    }

    private void checkOut(View view, Stay stay) throws Exception {

        // set check out date to today
        stay.setCheckOutDate(LocalDate.now());

        // get discount
        String stringHaveDiscount = view.getInputRegex(HAVE_DISCOUNT, REGEX_BOOLEAN);

        // break out of function
        if(stringHaveDiscount == null)
        {
            return;
        }

        // declare enabled Wifi boolean
        boolean haveDiscount;

        // convert String to boolean
        haveDiscount = "1".equals(stringHaveDiscount);

        // initialize discount type
        DiscountType discountType = null;

        // initialize discount amount
        double discountAmount = 0.;

        // if discount available
        if(haveDiscount)
        {
            // get discount type
            discountType = (DiscountType) view.getInputEnum(DiscountType.class, DISCOUNT_TYPE, REGEX_NUMBERS);

            // break out of method
            if(discountType == null)
            {
                return;
            }

            // get discount amount
            String stringDiscountAmount = view.getInputRegex(DISCOUNT_AMOUNT, REGEX_NUMBERS);

            // break out of function
            if(stringDiscountAmount == null)
            {
                return;
            }

            // convert String to double
            discountAmount = Double.parseDouble(stringDiscountAmount);

        }

        // get weekend surcharge
        String stringWeekendSurcharge = view.getInputRegex(WEEKEND_SURCHARGE, REGEX_NUMBERS);

        // break out of function
        if(stringWeekendSurcharge == null)
        {
            return;
        }

        // convert String to double
        double weekendSurcharge = Double.parseDouble(stringWeekendSurcharge);

        // get weekend surcharge
        String stringTax = view.getInputRegex(TAX, REGEX_NUMBERS);

        // break out of function
        if(stringTax == null)
        {
            return;
        }

        // convert String to double
        double tax = Double.parseDouble(stringTax);

        // create payment
        Payment payment = new Payment(discountType, discountAmount, weekendSurcharge, tax, stay);

        // print payment receipt
        view.displayText(payment.toString());

        // get payment method
        PaymentType paymentType = (PaymentType) view.getInputEnum(PaymentType.class, PAYMENT_TYPE, REGEX_NUMBERS);

        // break out of method
        if(paymentType == null)
        {
            return;
        }

        // check whether payment type is credit card
        if(paymentType == PaymentType.CREDIT_CARD)
        {
            // get credit card of guest
            CreditCard creditCard = creditCardController.getCreditCard(view, stay.getGuest());
        }

        view.displayText("\nThe payment has been successful.\n\n");

    }


    private void performCheckInWalkIn(View view) throws Exception {
        Persistence persistence = this.getPersistenceImpl();

        ArrayList<Entity> stays = persistence.retrieveAll(Stay.class);

        LocalDate checkOutDate = view.getValidDate(CHECK_OUT_DATE, PATTERN_VALID_DATE, REGEX_VALID_DATE, LocalDate.now());

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

        // get room meeting the requirements
        Room room = roomController.getAvailableRoom(LocalDate.now(), checkOutDate, roomType.getRoomTypeEnum(), bedType, enabledWifi, withView, smoking, false);

        if(room == null) {

            view.display("This room is not available for the selected dates");
        }
        else {

            // get room rate
            double roomRate = room.getRoomType().getRoomRate();

            // get the number of days of the stay
            long numberOfDays = ChronoUnit.DAYS.between(LocalDate.now(), checkOutDate);

            // format string
            String stringPrintCost = String.format("The total room cost will be %7.2f SGD (%5.2f SGD per night) for the selected dates.\n", roomRate*numberOfDays, roomRate);

            // print out expected cost for this room
            view.displayText(stringPrintCost);

            // check whether reservation should be continued
            String stringContinueReservation = view.getInputRegex(CONTINUE_STAY, REGEX_BOOLEAN);

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
                view.displayText(ABORT_STAY);

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

            // create stays
            Stay stay = new Stay(guest,creditCard, room,LocalDate.now(), checkOutDate, numberOfAdults,numberOfChildren);

            // add stay to ArrayList of stays
            persistence.createCache(stay,Stay.class);

            // print reservation
            view.displayText(stay.toString() + "\n\n");

            view.display("\nThe booking has been added to the system.\n");
        }
    }

    /*
    private ArrayList<Reservation> getReservationsAvalible(Guest guest) throws Exception{
        Persistence persistence = getPersistenceImpl();
        ArrayList<Entity> reservations = persistence.retrieveAll(Reservation.class);
        ArrayList<Reservation> relatedReservations = new ArrayList<>();

        System.out.println(reservations.size());

        for (Entity entity: reservations) {
            Reservation reservation = (Reservation) entity;

            System.out.println((reservation.getGuest().equals(guest)));
            System.out.println(reservation.getGuest());
            System.out.println(guest);

            if ((reservation.getGuest() == guest)&&(reservation.getStatus()== ReservationStatus.CONFIRMED)){
                relatedReservations.add(reservation);
            }
        }
        return relatedReservations;
    }*/

    @Override
    protected void create(View view) throws Exception {
    }

    @Override
    protected boolean retrieve(View view) throws Exception {
        return true;
    }

    @Override
    protected void update(View view) throws Exception {
    }

    @Override
    protected void delete(View view) throws Exception {
    }

    @Override
    public Stay select(View view) throws Exception {

        // initialize reservation
        Stay stay = null;

        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        try {

            // get all stays
            ArrayList<Entity> stays = persistence.retrieveAll(Stay.class);

            // check whether any stay exists
            if (stays.size() == 0) {
                view.displayText("No stay exists. Create a stays before searching for the stay.\n\n");

                return null;
            }

            // flag to check whether the entry of the guest name should be tried again
            boolean repeatEntry;

            //repeat
            do {
                // initialize repeat flag to false
                repeatEntry = false;

                    // get name of the guest
                    String guestName = view.getInputRegex(GUEST_NAME, REGEX_ONE_ALPHA_NUMERIC_CHARACTER);

                    // break out of function
                    if (guestName == null) {
                        return null;
                    }

                    // create ArrayList of stays
                    ArrayList<Stay> potentialStays = new ArrayList<>();

                    // convert guest name to lower case
                    String guestNameLowerCase = guestName.toLowerCase();

                    // get words in name
                    String[] names = guestNameLowerCase.trim().split("\\s+");

                    // iterate through all stays
                    for (Entity entity : stays) {

                        // cast to stay object
                        Stay stayIterator = (Stay)entity;

                        // get guest of stay
                        Guest guestIterator = stayIterator.getGuest();

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

                            // add stay to potential stays
                            potentialStays.add(stayIterator);
                        }
                    }

                    // check whether a stay was found
                    if (potentialStays.isEmpty()) {

                        // check whether the entry of the reservation's guest name should be tried again
                        repeatEntry = view.repeatEntry(NOT_FOUND);

                    } else if (potentialStays.size() == 1) {

                        // print stay details
                        view.displayText(potentialStays.get(0).toString() + "\n\n");

                        // get reservation
                        stay = potentialStays.get(0);

                    } else {

                        // select stay from potential stays
                        stay = view.getInputArray(potentialStays, NUMBER_RESERVATION, REGEX_NUMBERS);

                        // break out of method
                        if (stay == null) {
                            return null;
                        }

                        // print stay details
                        view.displayText(stay.toString() + "\n\n");
                    }

            }while(stay == null && repeatEntry);
        }
        catch(Exception e)
        {

        }

        return stay;
    }


    @Override
    public void show(View view) throws Exception{
        Persistence persistence = this.getPersistenceImpl();
        List entityList = new ArrayList();
        // Provide a predicate to search for matching items
        Iterable<Stay> stays = persistence.search(Stay.class);

        // Loop through results and add it into the list
        for(Entity entity: stays)
            entityList.add(entity);

        view.display(entityList);
    }

    public Entity getStayByRoomNumber(String roomNumber) throws Exception{

        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        Entity result = null;

        try {

            ArrayList<Entity> entities = persistence.retrieveAll(Stay.class);

            for (Entity entity : entities) {

                Stay stay = (Stay) entity;

                if (stay.getRoom().getRoomNumber().equals(roomNumber)) {  //was before: stay.getRoom().getRoomNumber().equals(roomNumber) && stay.getStatus().equals(StayStatus.CHECKEDIN)
                    result = entity;
                    break;
                }
            }
        }
        catch(Exception e)
        {

        }
        return result;
    }
}
