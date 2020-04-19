package Controller.EntityManager;

import Controller.EntityController;
import Model.Guest.*;
import Persistence.Persistence;
import View.View;
import Persistence.Entity;

import java.lang.ref.SoftReference;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CreditCardController extends EntityController<CreditCard> {

    private static final String REGEX_NUMBERS = "[0-9]+";
    private static final String REGEX_ONE_ALPHA_NUMERIC_CHARACTER = "^.*[a-zA-Z0-9]+.*$";
    private static final String REGEX_CREDIT_CARD = "^(?:(?<visa>4[0-9]{12}(?:[0-9]{3})?)|(?<mastercard>5[1-5][0-9]{14})|(?<discover>6(?:011|5[0-9]{2})[0-9]{12})|(?<amex>3[47][0-9]{13})|(?<diners>3(?:0[0-5]|[68][0-9])?[0-9]{11})|(?<jcb>(?:2131|1800|35[0-9]{3})[0-9]{11}))$";
    private static final String REGEX_CREDIT_CARD_VISA = "^(?:(?<visa>4[0-9]{12}(?:[0-9]{3})?))$";
    private static final String REGEX_CREDIT_CARD_MASTERCARD = "^(?:(?<mastercard>5[1-5][0-9]{14}))$";
    private static final String REGEX_CREDIT_CARD_DISCOVER = "^(?:(?<discover>6(?:011|5[0-9]{2})[0-9]{12}))$";
    private static final String REGEX_CREDIT_CARD_AMEX = "^(?:(?<amex>3[47][0-9]{13}))$";
    private static final String REGEX_CREDIT_CARD_DINERS = "^(?:(?<diners>3(?:0[0-5]|[68][0-9])?[0-9]{11}))$";
    private static final String REGEX_CREDIT_CARD_JCB = "^(?:(?<jcb>(?:2131|1800|35[0-9]{3})[0-9]{11}))$";
    private static final String REGEX_CREDIT_CARD_CVC = "^[0-9]{3}$";
    private static final String REGEX_CREDIT_CARD_AMEX_CVC = "^[0-9]{4}$";
    private static final String PATTERN_VALID_DATE = "MM/yy";
    private static final String REGEX_VALID_DATE = "^(?:(0[1-9]|1[0-2])/[0-9]{2})$";

    private static final String GUEST_NAME = "guest name";
    private static final String CARDHOLDER_NAME = "cardholder name";
    private static final String CREDIT_CARD_NUMBER = "credit card number";
    private static final String EXPIRATION_DATE = "expiration date";
    private static final String CVC = "CVC/CVV (card validation code/value)";
    private static final String GUEST = "guest";
    private static final String NUMBER_GUEST = "number of the guest";


    private static final String NOT_FOUND = "not found";

    public CreditCardController(Persistence persistence) {
        super(persistence);

    }

    @Override
    protected String getEntityName() {
        return "Credit Card";
    }

    @Override
    public List<String> getOptions() {

        return Arrays.asList("Add a credit card",
                "Remove a credit card",
                "Print all credit cards");

    }

    @Override
    protected void safeOnOptionSelected(View view, int option) throws Exception {
        switch (option) {
            case 0:
                create(view);
                break;
            case 1:
                delete(view);
                break;
            case 2:
                show(view);
                break;
        }
    }

    @Override
    protected void create(View view) throws Exception {

        // search for guest via name
        Guest guest = getGuest(view);

        // check whether guest was found
        if(guest == null)
        {
            return;
        }

        // get credit cards of guest
        ArrayList<CreditCard> creditCards = guest.getCreditCards();

        // print credit cards
        printCreditCards(view, creditCards);

        // create new credit card
        CreditCard creditCard = createCreditCard(view);

        // break out of method
        if(creditCard == null)
        {
            return;
        }

        // check whether this credit card already exists
        // iterate through all existing guests
        for(CreditCard creditCardIterator : creditCards)
        {

            // check whether the credit card number is the same
            if(creditCardIterator.getCardNumber() == creditCard.getCardNumber())
            {
                view.displayText("\nA credit card with these credentials already exists in this guest's profile.\n\n");
                return;
            }
        }

        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        // add guest to ArrayList of guests
        persistence.createCache(creditCard, CreditCard.class);

        // print credit card
        view.displayText(creditCard.toString());

        // display text
        view.displayText("\nThe credit card has been added to this guest.\n\n");

    }

    protected CreditCard createCreditCard(View view)
    {
        // get card holder name
        String cardHolderName = view.getInputRegex(CARDHOLDER_NAME, REGEX_ONE_ALPHA_NUMERIC_CHARACTER);

        // break out of function
        if(cardHolderName == null)
        {
            return null;
        }

        // get card number
        String stringCardNumber = view.getInputRegex(CREDIT_CARD_NUMBER, REGEX_CREDIT_CARD);

        // break out of function
        if(stringCardNumber == null)
        {
            return null;
        }

        // convert String to long
        long cardNumber = Long.parseLong(stringCardNumber);

        //declare payment network
        PaymentNetwork paymentNetwork = null;

        // get payment network
        if(Pattern.compile(REGEX_CREDIT_CARD_VISA).matcher(stringCardNumber).matches())
        {
            paymentNetwork = PaymentNetwork.VISA;
        }
        else if(Pattern.compile(REGEX_CREDIT_CARD_MASTERCARD).matcher(stringCardNumber).matches())
        {
            paymentNetwork = PaymentNetwork.MASTERCARD;
        }
        else if(Pattern.compile(REGEX_CREDIT_CARD_DISCOVER).matcher(stringCardNumber).matches())
        {
            paymentNetwork = PaymentNetwork.DISCOVER;
        }
        else if(Pattern.compile(REGEX_CREDIT_CARD_AMEX).matcher(stringCardNumber).matches())
        {
            paymentNetwork = PaymentNetwork.AMEX;
        }
        else if(Pattern.compile(REGEX_CREDIT_CARD_DINERS).matcher(stringCardNumber).matches())
        {
            paymentNetwork = PaymentNetwork.DINERS;
        }
        else if(Pattern.compile(REGEX_CREDIT_CARD_JCB).matcher(stringCardNumber).matches())
        {
            paymentNetwork = PaymentNetwork.JCB;
        }

        view.displayText("\nThe payment network of this credit card is: " + paymentNetwork.toString() + ".\n\n");

        // get expiration date
        YearMonth expirationDate = view.getValidDate(EXPIRATION_DATE, PATTERN_VALID_DATE, REGEX_VALID_DATE);

        // break out of function
        if(expirationDate == null)
        {
            return null;
        }

        // regex for CVC
        String regexCreditCardCVC = REGEX_CREDIT_CARD_CVC;

        // check whether credit card is an AMEX card
        if(paymentNetwork == PaymentNetwork.AMEX)
        {
            // regex for AMEX credit card
            regexCreditCardCVC = REGEX_CREDIT_CARD_AMEX_CVC;
        }

        // get card validation code
        String stringCardValidationCode = view.getInputRegex(CVC, regexCreditCardCVC);

        // break out of function
        if(stringCardValidationCode == null)
        {
            return null;
        }

        // convert card validation code to short
        short cardValidationCode = Short.parseShort(stringCardValidationCode);

        // create credit card
        return new CreditCard(cardHolderName, cardNumber, expirationDate, paymentNetwork, cardValidationCode);
    }

    protected void delete(View view)
    {
        // search for guest via name
        Guest guest = getGuest(view);

        //check whether guest was found
        if(guest == null)
        {
            return;
        }

        // get credit cards of guest
        ArrayList<CreditCard> creditCards = guest.getCreditCards();

        //check whether only one credit card is on file
        if(creditCards.size() <= 1)
        {
            view.displayText("Only one credit card is on file for this guest.\nThis credit card cannot be removed since one credit card is needed to pay for the expenses at the hotel.\n\n");
            return;
        }

        // get credit card
        CreditCard creditCard = view.getInputArray(creditCards, "number of the credit card", REGEX_NUMBERS);

        //check whether credit card was found
        if(creditCard == null)
        {
            return;
        }

        // print credit card
        view.displayText(creditCard.toString());

        // remove credit card from ArrayList
        creditCards.remove(creditCard);

        view.displayText("\nThe credit card of this guest has been removed.\n\n");
    }

    private Guest getGuest(View view)
    {
        // initialize guest
        Guest guest = null;

        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        try {

            // get all guests
            ArrayList<SoftReference<Entity>> guests = persistence.retrieveAll(Guest.class);

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
                for (SoftReference<Entity> softReference : guests) {

                    // cast to guest object
                    Guest guestIterator = (Guest)softReference.get();

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

    protected void show(View view)
    {
        // search for guest via name
        Guest guest = getGuest(view);

        //check whether guest was found
        if(guest == null)
        {
            return;
        }

        // get credit cards of guest
        ArrayList<CreditCard> creditCards = guest.getCreditCards();

        printCreditCards(view, creditCards);
    }

    private void printCreditCards(View view, ArrayList<CreditCard> creditCards)
    {

        view.displayText("\nThe following credit cards are on file for this guest:\n");

        // iterate through all credit cards of this guest
        for(CreditCard creditCard : creditCards)
        {

            //print credit card
            view.displayText(creditCard.toString());

        }

        view.displayText("\n");

    }

    protected boolean retrieve(View view)
    {
        return false;
    }

    protected void update(View view)
    {

    }

    public CreditCard select(View view) throws Exception {
        return null;
    }

}
