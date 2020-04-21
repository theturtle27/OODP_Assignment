package Controller.EntityManager;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import Model.Stay;

public class StayController {

    private ArrayList<Stay> stays;
    private ArrayList<Reservation> reservations;

    public StayController(ArrayList<Stay> stays, ArrayList<Reservation> reservations)
    {
        this.stays = stays;
        this.reservations = reservations;
    }

    public void manageStay(int option, StayView stayView)
    {
        switch(option)
        {
            case 1: checkIn(stayView);
                break;
            case 2: checkOut(stayView);
                break;
            case 3: exitStayEditor(stayView);
                break;
            default: stayView.displayText("\nThis option is not available. Please enter a number between [1] and [...].\n");

        }
    }

    public void checkIn(StayView stayView)
    {

        // get whether guest has a reservation
        boolean haveReservation = stayView.getInputBoolean("Do you have a reservation ([1] Yes, [0] No)? ");

        // check whether guest has a reservation
        if(haveReservation)
        {
            // get whether reservation should be searched by reservation ID or guest name
            boolean byReservationID = stayView.getInputBoolean("Do you want to search the reservation by reservation ID or by guest name ([1] reservation ID, [0] guest name)? ");

            // declare reservation
            Reservation reservation;

            // search via reservation ID
            if(byReservationID)
            {

                //TODO: We are assuming that a guest has only one reservation
                // get reservation
                reservation = getReservationByID(stayView);

            }
            else
            {

                // get reservation
                reservation = getReservationByName(stayView);
            }

            // cancel check-in process if reservation is not found
            if(reservation == null)
            {
                return;
            }

            // check in the reservation
            Stay stay = new Stay(reservation);

            // add stay to ArrayList of stays
            stays.add(stay);

        }
        // guest has no reservation (walk-in guest)
        else
        {
            //TODO: same procedure as reservation: Think about inheritance or creating a method where we can use both

        }
    }

    public void checkOut(StayView stayView)
    {

    }

    public void exitStayEditor(StayView stayView)
    {
        stayView.displayText("The check-ins/check-outs have been managed successfully");
    }

    public Reservation getReservationByID(StayView stayView)
    {

        // flag to check whether the entry of the reservation id should be tried again
        boolean repeatEntry = false;

        // initialize reservation
        Reservation reservation = null;

        // repeat
        do
        {

            // get reservation ID
            int reservationID = stayView.getInputInteger("Enter the reservation ID: ");

            // iterate through all reservations
            for(Reservation reservationIterator : reservations)
            {

                // check whether reservation IDs are the same
                if(reservationIterator.getReservationID() == reservationID)
                {

                    // reservation found
                    reservation = reservationIterator;

                }

            }

            // check whether reservation was found
            if(reservation == null)
            {

                // check whether the entry of the reservation id should be tried again
                repeatEntry = stayView.getInputBoolean("No reservation was found. Do you want to try again ([1] Yes, [0] No)? ");

            }

        }while(reservation == null && repeatEntry);

        return reservation;
    }

    public Reservation getReservationByName(StayView stayView)
    {
        // flag to check whether the entry of the reservation id should be tried again
        boolean repeatEntry = false;

        // initialize reservation
        Reservation reservation = null;

        // repeat
        do
        {

            // get guest name
            String guestName = stayView.getInputString("Enter the guest name: ");

            // create ArrayList of reservations
            ArrayList<Reservation> potentialReservations = new ArrayList<Reservation>();

            // convert guest name to lower case
            String guestNameLowerCase = guestName.toLowerCase();

            // get words in name
            String[] names = guestNameLowerCase.trim().split("\\s+");

            // iterate through all reservations
            for(Reservation reservationIterator : reservations)
            {
                // get guest who is associated to this reservation
                Guest guest = reservationIterator.getGuest();

                // get name of guest who is associated to this reservation
                String guestNameReservation = guest.getName();

                // flag to check whether all parts of the guests name are part of an existing guest
                boolean isPartOfName = true;

                // iterate through all names
                for (String name : names)
                {

                    // check whether name is part of the guest's name
                    if (!guestNameReservation.toLowerCase().contains(name))
                    {
                        // set flag to false
                        isPartOfName = false;
                        break;
                    }
                }

                // check whether all parts of the name match with the guests name
                if(isPartOfName)
                {

                    // add reservation to potential reservations
                    potentialReservations.add(reservationIterator);
                }

            }

            // check whether reservation was found
            if(potentialReservations.isEmpty())
            {

                // check whether the entry of the guest name should be tried again
                repeatEntry = stayView.getInputBoolean("No reservation was found. Do you want to try again ([1] Yes, [0] No)? ");

            }
            else
            {
                // print all potential reservations
                printReservations(stayView, potentialReservations);

                // get which reservation exactly
                int selectReservation = stayView.getInputInteger("Enter the number of the reservation: ");

                // get reservation
                reservation = potentialReservations.get(selectReservation);
            }

        }while(reservation == null && repeatEntry);

        return reservation;

    }

    public void printReservations(StayView stayView, ArrayList<Reservation> reservations)
    {
        // iterate through all reservations
        for(Reservation reservation : reservations)
        {
            //TODO: iterate through them like through menu, with iterator number
            stayView.displayText(reservation.toString());
        }
    }


}
