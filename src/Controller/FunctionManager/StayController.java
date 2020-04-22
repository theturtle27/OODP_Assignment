package Controller.FunctionManager;

import Controller.EntityController;
import Controller.EntityManager.GuestController;
import Model.Guest.Guest;
import Model.Payment.Payment;
import Model.Room.RoomStatus;
import Model.Stay.Stay;
import Model.reservation.Reservation;
import Model.reservation.ReservationStatus;
import Persistence.Persistence;
import View.View;
import View.Options;
import Persistence.Entity;

import java.text.SimpleDateFormat;
import java.util.*;

public class StayController extends EntityController<Stay> {
    public final static String KEY_NAME = "Reservation name";
    public final static String KEY_IDENTIFICATION = "identification number";
    public final static String KEY_NATIONALITY = "nationality";
    public final static String KEY_GENDER = "gender";
    public final static String KEY_CONTACT_NUMBER = "contact number";
    public final static String KEY_EMAIL_ADDRESS = "email address";
    public final static String KEY_SEARCH = "name of the guest to search for";
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
                PerformchechInReservation(view);
                break;
            case 1:
                chechInWalkedin(view);
                break;
            case 2:
                checkOut(view);
                break;
        }
    }


    private void PerformchechInReservation(View view) throws Exception {
        Guest guest = guestController.select(view);


        if(guest != null) {
            Persistence persistence = this.getPersistenceImpl();
            ArrayList<Reservation> reservations = reservationController.getReservationsAvalible(guest);

            long count = reservations.size();

            List<Options> ynOptionList = Arrays.asList(Options.Yes, Options.No);
            if(count == 0) {
                // There are no reservations, ask user to make reservation
                view.message("You have no reservations for today, do you want to make one?");
                if(view.options(ynOptionList) == Options.Yes) {
                    chechInWalkedin(view);
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
            ArrayList<Stay> stays = reservationController.getStaysAvalibleCheckout(guest);
            long count = stays.size();
            if(count == 0) {
                view.message("No checked-in room that is available for check-out.");
            }
            else {
                view.message("You have " + count + " rooms eligible for check-out");
                view.message("Do you wish to add check-out all your rooms (Select no to inspect each reservation to decide which ones to check-out)?");

                List<Stay> checkoutStays = new ArrayList<Stay>();
                List<Options> ynOptionList = Arrays.asList(Options.Yes, Options.No);
                Options selectedOption = view.options(ynOptionList);

                for(Stay stay: stays) {
                    if(selectedOption == Options.Yes) {
                        checkoutStays.add(stay);
                    }
                    else {
                        view.message("Room number: " + stay.getAssignedRoom().getNumber());
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
            if(reservation.getStatus() == ReservationStatus.IN_WAITLIST)
                reservation.setAssignedRoom(findVacantAndAvailableRoom(reservation));

            if(reservation.getStatus() == ReservationStatus.CONFIRMED) {
                if(reservation.getAssignedRoom().getStatus() == RoomStatus.VACANT) {
                    reservation.setStatus(ReservationStatus.CHECKED_IN);
                    Stay stay = new Stay(reservation);
                    stays.add(stay);
                    message = "The above reservation has been checked-in successfully, the room number assigned is " + stay.getAssignedRoom().getNumber();
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

    private void checkOut(View view, List<Stay> stays) throws Exception {
        Persistence persistence = this.getPersistenceImpl();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date today = sdf.parse(sdf.format(new Date()));
        for(Stay stay: stays) {
            // Update the end date of the reservations to today's date;
            stay.setEndDate(today);
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
        persistence.create(payment, Payment.class);
        view.message("Your payment is successful, thank you for staying with us, we hope to see you again!");
        for(Reservation reservation: reservations) {
            // Update status and save changes to file
            reservation.setStatus(ReservationStatus.CheckedOut);
            reservation.setPayment(payment);
            persistence.update(reservation, Reservation.class);

            view.message("Successfully checked out from room " + reservation.getAssignedRoom().getNumber() + ".");
        }
        view.message("Please take not of your receipt below");
        view.display(payment);
    }

    private void chechInWalkedin(View view) throws Exception {
        Persistence persistence = this.getPersistenceImpl();

        ArrayList<Entity> stays = persistence.retrieveAll(Stay.class);

        Guest guest = guestController.select(view);
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
