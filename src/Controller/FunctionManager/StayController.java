package Controller.FunctionManager;

import Controller.EntityController;
import Controller.EntityManager.GuestController;
import Model.Guest.Guest;
import Model.Payment.DiscountType;
import Model.Payment.Payment;
import Model.Payment.PaymentType;
import Model.Room.RoomStatus;
import Model.Stay.Stay;
import Model.Stay.StayStatus;
import Model.reservation.Reservation;
import Model.reservation.ReservationStatus;
import Persistence.Persistence;
import View.View;
import View.Options;
import Persistence.Entity;

import java.time.LocalDate;
import java.util.*;

public class StayController extends EntityController<Stay> {
    public final static String KEY_NAME = "Reservation name";
    public final static String KEY_IDENTIFICATION = "identification number";
    public final static String KEY_NATIONALITY = "nationality";
    public final static String KEY_GENDER = "gender";
    public final static String KEY_CONTACT_NUMBER = "contact number";
    public final static String KEY_EMAIL_ADDRESS = "email address";
    public final static String KEY_DISCOUNT_VALUE = "value for discount";
    public final static String KEY_ID = "ID of stay or 'Search' to search for guest ID by name";

    private final GuestController guestController;
    private final ReservationController reservationController;

    public StayController(Persistence persistence ,GuestController guestController, ReservationController reservationController) {
        super(persistence);
        this.guestController = guestController;
        this.reservationController = reservationController;
    }

    @Override
    public List<String> getOptions() {
        return Arrays.asList("Check in(reservation)",
                "Check in(walked in)",
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
                performchechInReservation(view);
                break;
            case 1:
                performchechInWalkedin(view);
                break;
            case 2:
                performCheckOut(view);
                break;
        }
    }


    private void performchechInReservation(View view) throws Exception {
        Guest guest = guestController.select(view);


        if(guest != null) {
            Persistence persistence = this.getPersistenceImpl();
            ArrayList<Reservation> reservations = getReservationsAvalible(guest);

            long count = reservations.size();

            List<Options> ynOptionList = Arrays.asList(Options.Yes, Options.No);
            if(count == 0) {
                // There are no reservations, ask user to make reservation
                view.message("You have no reservations for today, do you want to make one?");
                if(view.options(ynOptionList) == Options.Yes) {
                    performchechInWalkedin(view);
                }
            }
            else {
                view.message("You have " + count + " reservations eligible for check-in");
                view.message("Do you wish to add check-in all your reservations? (Select no to inspect each reservation to decide which ones to check-in)");

                Options selectedOption = view.options(ynOptionList);

                for(Reservation reservation: reservations) {
                    if(selectedOption == Options.Yes) {
                        reservations.add(reservation);
                    }
                    else {
                        view.display(reservation);
                        view.message("Do you wish to check-in the above reservation?");
                        if(view.options(ynOptionList) == Options.Yes)
                            reservations.add(reservation);
                    }
                }
            }

            // Proceed with check-in
            if(reservations.size() > 0) {
                view.message("You have selected " + reservations.size() + " reservation(s), do you wish to proceed to check-in?");
                if(view.options(ynOptionList) == Options.Yes)
                    checkin(view, reservations);
            }
            else {
                view.message("You have no reservations selected for check-in");
            }
        }
    }

    private void performCheckOut(View view) throws Exception {
        Guest guest = guestController.select(view);

        if(guest != null) {
            ArrayList<Stay> stays = getStaysAvalibleCheckout(guest);
            long count = stays.size();
            if(count == 0) {
                view.message("No checked-in room that is available for check-out.");
            }
            else {
                view.message("You have " + count + " rooms eligible for check-out");
                view.message("Do you wish to add check-out all your rooms (Select no to inspect each reservation to decide which ones to check-out)?");

                ArrayList<Stay> checkoutStays = new ArrayList<Stay>();
                List<Options> ynOptionList = Arrays.asList(Options.Yes, Options.No);
                Options selectedOption = view.options(ynOptionList);

                for(Stay stay: stays) {
                    if(selectedOption == Options.Yes) {
                        checkoutStays.add(stay);
                    }
                    else {
                        view.message("Room number: " + stay.getRoom().getRoomNumber());
                        view.display(stay);
                        view.message("Do you wish to check-out of the above room?");
                        if(view.options(ynOptionList) == Options.Yes)
                            checkoutStays.add(stay);
                    }
                }

                if(checkoutStays.size() > 0) {
                    view.message("You have selected " + checkoutStays.size() + " room(s) to check out, do you wish to proceed?");
                    if(view.options(ynOptionList) == Options.Yes)
                        checkOut(view, checkoutStays);
                }
                else {
                    view.message("You have no rooms selected for check-out");
                }
            }
        }
    }

    private void checkin(View view,ArrayList<Reservation> reservations) throws Exception {
        Persistence persistence = this.getPersistenceImpl();

        ArrayList<Entity> stays = persistence.retrieveAll(Stay.class);
        for(Reservation reservation: reservations) {
            String message;
            if(reservation.getStatus() == ReservationStatus.CONFIRMED) {
                if(reservation.getRoom().getRoomStatus() == RoomStatus.VACANT) {
                    reservation.setStatus(ReservationStatus.CHECKED_IN);
                    Stay stay = new Stay(reservation);
                    stays.add(stay);
                    message = "The above reservation has been checked-in successfully, the room number assigned is " + stay.getRoom().getRoomNumber();
                }
                else {
                    message = "The hotel is still preparing your hotel room, please come back in an hour time, we apologise for any inconvenience caused.";
                }
            }
            else {
                message = "We are unable to check-in for the reservation above as it is still in the wait list and there are no available rooms for the specified room requirements.";
            }

            view.display(reservation);
            view.message(message);
        }
    }

    private void checkOut(View view, ArrayList<Stay> stays) throws Exception {
        Persistence persistence = this.getPersistenceImpl();

        LocalDate today = LocalDate.now();
        for(Stay stay: stays) {
            // Update the end date of the reservations to today's date;
            stay.setCheckOutDate(today);
        }

        Payment payment = new Payment(stays);
        view.display(payment);

        view.message("Do you have a discount?");
        if(view.options(Arrays.asList(Options.Yes, Options.No)) == Options.Yes) {
            Map<String, String> inputMap = new LinkedHashMap<String, String>();
            inputMap.put(KEY_DISCOUNT_VALUE, null);

            view.message("Select a discount type");
            DiscountType dType = view.options(Arrays.asList(DiscountType.values()));
            double value = 0;
            boolean valid;
            do {
                view.input(inputMap);
                try {
                    value = Double.parseDouble(inputMap.get(KEY_DISCOUNT_VALUE));
                    valid = true;
                } catch(NumberFormatException e) {
                    view.error(Arrays.asList(KEY_DISCOUNT_VALUE));
                    valid = false;
                }
            } while(!valid);

            payment.setDiscount(dType, value);
            view.display(payment);
        }

        view.message("Which method of payment to use?");
        payment.setPaymentType(view.options(Arrays.asList(PaymentType.values())));

        // Create payment
        ArrayList<Entity> payments = persistence.retrieveAll(Payment.class);
        payments.add(payment);
        view.message("Your payment is successful, thank you for staying with us, we hope to see you again!");
        view.message("Please take not of your receipt below");
        view.display(payment);
    }


    private void performchechInWalkedin(View view) throws Exception {
        Persistence persistence = this.getPersistenceImpl();

        ArrayList<Entity> stays = persistence.retrieveAll(Stay.class);

        Guest guest = guestController.select(view);
    }

    private ArrayList<Stay> getStaysAvalibleCheckout(Guest guest) throws Exception{
        Persistence persistence = getPersistenceImpl();
        ArrayList<Entity> stays = persistence.retrieveAll(Stay.class);
        ArrayList<Stay> relatedStays = new ArrayList<>();
        for (Entity entity: stays) {
            Stay stay = (Stay)entity;
            if ((stay.getGuest() == guest)&&(stay.getStatus()== StayStatus.CHECKEDIN)){
                relatedStays.add(stay);
            }
        }
        return relatedStays;
    }

    private ArrayList<Reservation> getReservationsAvalible(Guest guest) throws Exception{
        Persistence persistence = getPersistenceImpl();
        ArrayList<Entity> reservations = persistence.retrieveAll(Reservation.class);
        ArrayList<Reservation> relatedReservations = new ArrayList<>();
        for (Entity entity: reservations) {
            Reservation reservation = (Reservation) entity;
            if ((reservation.getGuest() == guest)&&(reservation.getStatus()== ReservationStatus.CONFIRMED)){
                relatedReservations.add(reservation);
            }
        }
        return relatedReservations;
    }

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
        Stay stay = null;

        Map<String, String> inputMap = new LinkedHashMap<String, String>();
        inputMap.put(KEY_ID, null);

        Persistence persistence = this.getPersistenceImpl();
        do {
            boolean retry;
            do {
                retry = false;
                // Retrieve user input for ID
                view.input(inputMap);

                try {
                    String input = inputMap.get(KEY_ID);
                    if(input.toLowerCase().equals("search")) {
                        retrieve(view);
                        retry = true;
                    }
                    else {
                        stay = persistence.retrieveByID(Long.parseLong(input), Stay.class);
                        if(stay == null)
                            view.error(Arrays.asList(KEY_ID));
                    }
                } catch(NumberFormatException e) {
                    view.error(Arrays.asList(KEY_ID));
                }
            } while(retry);
        } while(stay == null && !view.bailout());

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
}
