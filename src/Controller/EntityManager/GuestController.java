package Controller.EntityManager;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import Controller.EntityController;
import Model.Guest.CreditCard;
import Model.Guest.Gender;
import Model.Guest.Guest;
import Model.Guest.IdentityType;
import Persistence.Entity;
import Persistence.Persistence;
import View.ConsoleView;
import View.View;


public class GuestController extends EntityController<Guest> {

    private static final String REGEX_NUMBERS = "[0-9]+";
    private static final String REGEX_ONE_ALPHA_NUMERIC_CHARACTER = "^.*[a-zA-Z0-9]+.*$";
    private static final String REGEX_EMAIL = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private static final String REGEX_PHONE_NUMBER = "^\\+(?:[0-9] ?){6,14}[0-9]$";

    private static final String GUEST_NAME = "guest name";
    private static final String STREET_NAME = "street name";
    private static final String CITY_NAME = "city name";
    private static final String POSTAL_CODE = "postal code";
    private static final String COUNTRY = "name of the country";
    private static final String GENDER = "gender";
    private static final String NATIONALITY = "nationality";
    private static final String IDENTITY_TYPE = "identity type";
    private static final String IDENTITY_NUMBER = "identity number";
    private static final String EMAIL_ADDRESS = "eMail address";
    private static final String PHONE_NUMBER = "phone number";
    private static final String GUEST = "guest";
    private static final String NUMBER_GUEST = "number of the guest";


    private static final String NOT_FOUND = "not found";

    private CreditCardController creditCardController;

    public GuestController(Persistence persistence, CreditCardController creditCardController) {
        super(persistence);
        this.creditCardController = creditCardController;
    }

    @Override
    protected String getEntityName() {
        return "Guest Profile";
    }

    @Override
    public List<String> getOptions() {

        return Arrays.asList("Create a new guest",
                        "Update guest details",
                        "Search guest details",
                        "Remove guest details",
                        "Print all guest details");

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
                retrieve(view);
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
        // get name of the guest
        String guestName = view.getInputRegex(GUEST_NAME, REGEX_ONE_ALPHA_NUMERIC_CHARACTER);

        // break out of function
        if(guestName == null)
        {
            return;
        }

        // get street name
        String streetName = view.getInputRegex(STREET_NAME, REGEX_ONE_ALPHA_NUMERIC_CHARACTER);

        // break out of function
        if(streetName == null)
        {
            return;
        }

        // get city name
        String cityName = view.getInputRegex(CITY_NAME, REGEX_ONE_ALPHA_NUMERIC_CHARACTER);

        // break out of function
        if(cityName == null)
        {
            return;
        }

        // get postal code
        String postalCode = view.getInputRegex(POSTAL_CODE, REGEX_ONE_ALPHA_NUMERIC_CHARACTER);

        // break out of function
        if(postalCode == null)
        {
            return;
        }

        // get country
        String country = view.getInputRegex(COUNTRY, REGEX_ONE_ALPHA_NUMERIC_CHARACTER);

        // break out of function
        if(country == null)
        {
            return;
        }

        // get gender
        Gender gender = (Gender)view.getInputEnum(Gender.class, GENDER, REGEX_NUMBERS);

        // break out of function
        if(gender == null)
        {
            return;
        }

        // get nationality
        String nationality = view.getInputRegex(NATIONALITY, REGEX_ONE_ALPHA_NUMERIC_CHARACTER);

        // break out of function
        if(nationality == null)
        {
            return;
        }

        //get identity type
        IdentityType identityType = (IdentityType)view.getInputEnum(IdentityType.class, IDENTITY_TYPE, REGEX_NUMBERS);

        // break out of function
        if(identityType == null)
        {
            return;
        }

        // get identity number
        String identityNumber = view.getInputRegex(IDENTITY_NUMBER, REGEX_ONE_ALPHA_NUMERIC_CHARACTER);

        // break out of function
        if(identityNumber == null)
        {
            return;
        }

        // check whether guest already exists
        if(guestExists(view, identityType, identityNumber, nationality))
        {
            return;
        }

        // get email
        String eMail = view.getInputRegex(EMAIL_ADDRESS, REGEX_EMAIL);

        // break out of function
        if(eMail == null)
        {
            return;
        }

        // get phoneNumber
        String phoneNumber = view.getInputRegex(PHONE_NUMBER, REGEX_PHONE_NUMBER);

        // break out of function
        if(phoneNumber == null)
        {
            return;
        }

        CreditCard creditCard = (CreditCard)creditCardController.createCreditCard(view);

        // break out of function
        if(creditCard == null)
        {
            return;
        }

        // create ArrayList of credit cards
        ArrayList<CreditCard> creditCards = new ArrayList<CreditCard>();
        creditCards.add(creditCard);

        // create guest
        Guest guest = new Guest(guestName, streetName, cityName, postalCode, country, gender, nationality, identityType, identityNumber, eMail, phoneNumber, creditCards);

        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        // add guest to ArrayList of guests
        persistence.createCache(guest, Guest.class);

        // print guest with credit card information
        view.displayText(guest.toString() + guest.getCreditCards().get(0).toString());

        // display text
        view.displayText("\nThe guest has been added to the system.\n\n");
    }

    private boolean guestExists(View view, IdentityType identitytType, String identityNumber, String nationality)
    {
        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        try {

            // get all guests
            ArrayList<Entity> guests = persistence.retrieveAll(Guest.class);

            // iterate through all existing guests
            for (Entity entity : guests) {

                // cast to guest object
                Guest guest = (Guest)entity;

                // check whether identity type, identity number and nationality are the same
                if (guest.getIdentityType().equals(identitytType) && guest.getIdentityNumber().equals(identityNumber) && guest.getNationality().equals(nationality)) {
                    view.displayText("\nA guest with these credentials already exists.\nPlease update the guest's information instead of creating a duplicate guest.\n\n");

                    return true;
                }
            }

        }
        catch(Exception e)
        {

        }

        return false;
    }

    @Override
    protected boolean retrieve(View view) throws Exception {
        return false;
    }

    @Override
    protected void update(View view) throws Exception {

        // search for guest via name
        Guest guest = getGuest(view);

        //check whether guest was found
        if(guest == null)
        {
            return;
        }

        // get name of the guest
        String guestName = view.getInputRegex(GUEST_NAME, REGEX_ONE_ALPHA_NUMERIC_CHARACTER);

        // break out of function
        if(guestName == null)
        {
            return;
        }

        // get street name
        String streetName = view.getInputRegex(STREET_NAME, REGEX_ONE_ALPHA_NUMERIC_CHARACTER);

        // break out of function
        if(streetName == null)
        {
            return;
        }

        // get city name
        String cityName = view.getInputRegex(CITY_NAME, REGEX_ONE_ALPHA_NUMERIC_CHARACTER);

        // break out of function
        if(cityName == null)
        {
            return;
        }

        // get postal code
        String postalCode = view.getInputRegex(POSTAL_CODE, REGEX_ONE_ALPHA_NUMERIC_CHARACTER);

        // break out of function
        if(postalCode == null)
        {
            return;
        }

        // get country
        String country = view.getInputRegex(COUNTRY, REGEX_ONE_ALPHA_NUMERIC_CHARACTER);

        // break out of function
        if(country == null)
        {
            return;
        }

        // get gender
        Gender gender = (Gender)view.getInputEnum(Gender.class, GENDER, REGEX_NUMBERS);

        // break out of function
        if(gender == null)
        {
            return;
        }

        // get nationality
        String nationality = view.getInputRegex(NATIONALITY, REGEX_ONE_ALPHA_NUMERIC_CHARACTER);

        // break out of function
        if(nationality == null)
        {
            return;
        }

        //get identity type
        IdentityType identityType = (IdentityType)view.getInputEnum(IdentityType.class, IDENTITY_TYPE, REGEX_NUMBERS);

        // break out of function
        if(identityType == null)
        {
            return;
        }

        // get identity number
        String identityNumber = view.getInputRegex(IDENTITY_NUMBER, REGEX_ONE_ALPHA_NUMERIC_CHARACTER);

        // break out of function
        if(identityNumber == null)
        {
            return;
        }

        // get email
        String eMail = view.getInputRegex(EMAIL_ADDRESS, REGEX_EMAIL);

        // break out of function
        if(eMail == null)
        {
            return;
        }

        // get phoneNumber
        String phoneNumber = view.getInputRegex(PHONE_NUMBER, REGEX_PHONE_NUMBER);

        // break out of function
        if(phoneNumber == null)
        {
            return;
        }

        // set everything if there were no errors
        // set name of the guest
        guest.setName(guestName);

        // set street name
        guest.setStreetName(streetName);

        // set city name
        guest.setCityName(cityName);

        // set postal code
        guest.setPostalCode(postalCode);

        // set country
        guest.setCountry(country);

        // set gender
        guest.setGender(gender);

        // set nationality
        guest.setNationality(nationality);

        // set identity type
        guest.setIdentityType(identityType);

        // set identity number
        guest.setIdentityNumber(identityNumber);

        // set eMail
        guest.setEMail(eMail);

        // set phone number
        guest.setPhoneNumber(phoneNumber);

        // print guest
        view.displayText(guest.toString());

        // display text
        view.displayText("\n\nThe guest's details have been updated.\n\n");
    }

    private Guest getGuest(View view)
    {
        // initialize guest
        Guest guest = null;

        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        try {

            // get all guests
            ArrayList<Entity> guests = persistence.retrieveAll(Guest.class);

            // check whether any guests exist
            if (guests.size() == 0) {
                view.displayText("No guest exists. Create a guest before searching for the guest.\n\n");

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

                // create ArrayList of guests
                ArrayList<Guest> potentialGuests = new ArrayList<Guest>();

                // convert guest name to lower case
                String guestNameLowerCase = guestName.toLowerCase();

                // get words in name
                String[] names = guestNameLowerCase.trim().split("\\s+");

                // iterate through all guests
                for (Entity entity : guests) {

                    // cast to guest object
                    Guest guestIterator = (Guest)entity;

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
                        potentialGuests.add(guestIterator);
                    }
                }

                // check whether a guest was found
                if (potentialGuests.isEmpty()) {

                    // check whether the entry of the guest name should be tried again
                    repeatEntry = view.repeatEntry(GUEST, NOT_FOUND);

                } else if (potentialGuests.size() == 1) {
                    // print guest details
                    System.out.println(potentialGuests.get(0).toString() + "\n");

                    // get guest
                    guest = potentialGuests.get(0);
                } else {

                    // select guest from potential guests
                    guest = view.getInputArray(potentialGuests, NUMBER_GUEST, REGEX_NUMBERS);

                    // break out of method
                    if (guest == null) {
                        return null;
                    }

                    // print guest details
                    view.displayText(guest.toString() + "\n\n");
                }

            } while (guest == null && repeatEntry);
        }
        catch(Exception e)
        {

        }

        return guest;
    }

    @Override
    protected void delete(View view) throws Exception {

        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        try {

            // get all guests
            ArrayList<Entity> guests = persistence.retrieveAll(Guest.class);

            // search for guest via name
            Guest guest = getGuest(view);

            //check whether guest was found
            if (guest == null) {
                return;
            }

            // remove guest
            guests.remove(guest);

            //TODO: guest needs to be deleted
            //persistence.deleteCache(guests, )

            view.displayText("The guest's information has been removed.\n\n");
        }
        catch(Exception e)
        {

        }
    }

    @Override
    public Guest select(View view) throws Exception {
        return null;
    }


    @Override
    public void show(View view) throws Exception{

        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        try {

            // get all guests
            ArrayList<Entity> guests = persistence.retrieveAll(Guest.class);

            // check whether any guests exist
            if (guests.size() == 0) {
                view.displayText("No guest exists. Create a guest before printing the guest details.\n\n");

                return;
            }

            view.displayText("The following guests are on file:\n");

            // iterate through all guests
            for (Entity entity : guests) {

                // cast to guest object
                Guest guest = (Guest)entity;

                //print guest
                view.displayText(guest.toString() + "\n");

            }

            view.displayText("\n");
        }
        catch(Exception e)
        {

        }
    }
}
