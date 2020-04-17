package Controller.FunctionManager;

import Controller.EntityController;
import Model.reservation.Reservation;
import Persistence.Persistence;
import View.View;
import Persistence.Entity;

import java.util.*;
import java.util.regex.Pattern;

public class ReservationController extends EntityController<Reservation> {


    public final static String KEY_NAME = "Reservation name";
    public final static String KEY_IDENTIFICATION = "identification number";
    public final static String KEY_NATIONALITY = "nationality";
    public final static String KEY_GENDER = "gender";
    public final static String KEY_CONTACT_NUMBER = "contact number";
    public final static String KEY_EMAIL_ADDRESS = "email address";
    public final static String KEY_SEARCH = "name of the guest to search for";
    public final static String KEY_ID = "ID of guest or 'Search' to search for guest ID by name";


    public ReservationController(Persistence persistence) {
        super(persistence);
    }


    @Override
    protected String getEntityName() {
        return "Reservation";
    }

    @Override
    protected void safeOnOptionSelected(View view, int option) throws Exception {
        switch (option) {
            case 0:
                create(view);
                break;
            case 1:
                retrieve(view);
                break;
            case 2:
                update(view);
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
//        Map<String, String> inputMap = new LinkedHashMap<String, String>();
//        inputMap.put(KEY_NAME, null);
//        inputMap.put(KEY_IDENTIFICATION, null);
//        inputMap.put(KEY_NATIONALITY, null);
//        inputMap.put(KEY_GENDER, null);
//        inputMap.put(KEY_CONTACT_NUMBER, null);
//        inputMap.put(KEY_EMAIL_ADDRESS, null);
//
//        boolean valid = false;
//        do {
//            view.input(inputMap);
//
//            try {
//                Reservation reservation = new Reservation(inputMap.get(KEY_IDENTIFICATION), inputMap.get(KEY_NATIONALITY));
//                reservation.setName(inputMap.get(KEY_NAME));
//                reservation.setGender(Character.toUpperCase(inputMap.get(KEY_GENDER).charAt(0)));
//                reservation.setContactNo(inputMap.get(KEY_CONTACT_NUMBER));
//                reservation.setEmailAddress(inputMap.get(KEY_EMAIL_ADDRESS));
//
//                // Validate all fields
//                List<String> invalids = new ArrayList<String>();
//                if(reservation.getName().length() == 0)
//                    invalids.add(KEY_NAME);
//                if(reservation.getIdentification().length() == 0)
//                    invalids.add(KEY_IDENTIFICATION);
//                if(reservation.getNationality().length() == 0)
//                    invalids.add(KEY_NATIONALITY);
//                if(reservation.getGender() != 'M' && reservation.getGender() != 'F')
//                    invalids.add(KEY_GENDER);
//                if(!Pattern.matches("\\+?(\\d|-|\\s|\\(|\\))+", reservation.getContactNo()))
//                    invalids.add(KEY_CONTACT_NUMBER);
//                if(!Pattern.matches("^.+@.+\\..+$", reservation.getEmailAddress()))
//                    invalids.add(KEY_EMAIL_ADDRESS);
//
//                if(invalids.size() == 0) {
//                    // Ensure no duplicate guest record, with same identification and nationality
//                    Persistence persistence = this.getPersistenceImpl();
//
//                    persistence.create(reservation, Reservation.class);
//
//                    view.message("Guest profile successfully added!");
//                    valid = true;
//
//                }
//                else {
//                    view.error(invalids);
//                }
//            } catch(IndexOutOfBoundsException e) {
//                view.error(Arrays.asList(KEY_GENDER));
//            }
//        } while(!valid && !view.bailout());
    }

    @Override
    protected boolean retrieve(View view) throws Exception {
//        Map<String, String> inputMap = new LinkedHashMap<String, String>();
//        inputMap.put(KEY_SEARCH, null);
//
//        view.input(inputMap);
//
//        Persistence persistence = this.getPersistenceImpl();
//
//        List entityList = new ArrayList();
//
//        Iterable<Reservation> guests = persistence.search(Reservation.class);
//
//        for(Reservation entity: guests) {
//            if (entity.getName().equals(inputMap.get(KEY_SEARCH)))
//                entityList.add(entity);
//        }
//        // Display guests
//        view.display(entityList);
//
//        return entityList.size() > 0;
        return true;
    }

    @Override
    protected void update(View view) throws Exception {
//        Reservation reservation = select(view);
//
//        if(reservation != null) {
//            Persistence persistence = this.getPersistenceImpl();
//            Map<String, String> inputMap = new LinkedHashMap<String, String>();
//            inputMap.put(KEY_NAME, null);
//            inputMap.put(KEY_GENDER, null);
//            inputMap.put(KEY_CONTACT_NUMBER, null);
//            inputMap.put(KEY_EMAIL_ADDRESS, null);
//
//            boolean valid = false;
//            do {
//                // Retrieve user input for updateable fields
//                view.input(inputMap);
//
//                try {
//                    reservation.setName(inputMap.get(KEY_NAME));
//                    reservation.setGender(Character.toUpperCase(inputMap.get(KEY_GENDER).charAt(0)));
//                    reservation.setContactNo(inputMap.get(KEY_CONTACT_NUMBER));
//                    reservation.setEmailAddress(inputMap.get(KEY_EMAIL_ADDRESS));
//
//                    // Validate all fields
//                    List<String> invalids = new ArrayList<String>();
//                    if(reservation.getName().length() == 0)
//                        invalids.add(KEY_NAME);
//                    if(reservation.getGender() != 'M' && reservation.getGender() != 'F')
//                        invalids.add(KEY_GENDER);
//                    if(!Pattern.matches("\\+?(\\d|-|\\s|\\(|\\))+", reservation.getContactNo()))
//                        invalids.add(KEY_CONTACT_NUMBER);
//                    if(!Pattern.matches("^.+@.+\\..+$", reservation.getEmailAddress()))
//                        invalids.add(KEY_EMAIL_ADDRESS);
//
//                    // Attempts to update entity
//                    if(invalids.size() == 0 && persistence.update(reservation, Reservation.class)) {
//                        valid = true;
//                        view.message("Guest profile successfully updated!");
//                    }
//                    else {
//                        view.error(invalids);
//                    }
//                } catch(IndexOutOfBoundsException e) {
//                    view.error(Arrays.asList(KEY_GENDER));
//                }
//            } while(!valid && !view.bailout());
//        }
    }

    @Override
    protected void delete(View view) throws Exception {
//        Reservation reservation = select(view);
//
//        Persistence persistence = this.getPersistenceImpl();
//        if(reservation != null && persistence.delete(reservation, Reservation.class))
//            view.message("Guest profile deleted successfully!");
    }

    @Override
    public Reservation select(View view) throws Exception {
        Reservation guest = null;

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
                        guest = persistence.retrieveByID(Long.parseLong(input), Reservation.class);
                        if(guest == null)
                            view.error(Arrays.asList(KEY_ID));
                    }
                } catch(NumberFormatException e) {
                    view.error(Arrays.asList(KEY_ID));
                }
            } while(retry);
        } while(guest == null && !view.bailout());

        return guest;
    }


    @Override
    public void show(View view) throws Exception{
        Persistence persistence = this.getPersistenceImpl();
        List entityList = new ArrayList();
        // Provide a predicate to search for matching items
        Iterable<Reservation> guests = persistence.search(Reservation.class);

        // Loop through results and add it into the list
        for(Entity entity: guests)
            entityList.add(entity);

        view.display(entityList);
    }
}
