package Controller.EntityManager;

import Controller.EntityController;
import Model.Guest.*;
import Persistence.Persistence;
import View.View;

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
    private static final String REGEX_BOOLEAN = "^(?:(0|1))$";

    private static final String CARDHOLDER_NAME = "the cardholder name";
    private static final String CREDIT_CARD_NUMBER = "the credit card number without spaces";
    private static final String EXPIRATION_DATE = "the expiration date (format: mm/yy)";
    private static final String CVC = "the CVC/CVV (card validation code/value)";

    private static final String EXISTING_CREDIT_CARD = "whether you want to use an existing credit card :\n1) Yes\n0) No\n\nPlease select an option";


    private GuestController guestController;

    public CreditCardController(Persistence persistence) {
        super(persistence);

    }

    public void addGuestController(GuestController guestController)
    {
        this.guestController = guestController;
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
        Guest guest = guestController.select(view);

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

        // add credit card to ArrayList of creditCards
        creditCards.add(creditCard);

        // print credit card
        view.displayText(creditCard.toString());

        // display text
        view.displayText("\nThe credit card has been added to this guest.\n\n");

    }

    public CreditCard createCreditCard(View view)
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

        view.displayText("The payment network of this credit card is: " + view.capitalizeFirstLetter(paymentNetwork.toString()) + ".\n");

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

    protected void delete(View view) throws Exception {
        // search for guest via name
        Guest guest = guestController.select(view);

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

    protected void show(View view) throws Exception {
        // search for guest via name
        Guest guest = guestController.select(view);

        //check whether guest was found
        if(guest == null)
        {
            return;
        }

        // get credit cards of guest
        ArrayList<CreditCard> creditCards = guest.getCreditCards();

        printCreditCards(view, creditCards);
    }

    public void printCreditCards(View view, ArrayList<CreditCard> creditCards)
    {

        view.displayText("The following credit cards are on file for this guest:\n");

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

    public CreditCard getCreditCard(View view, Guest guest)
    {
        // get credit cards of guest
        ArrayList<CreditCard> creditCards = guest.getCreditCards();

        printCreditCards(view, creditCards);

        // get whether an existing credit card should be used
        String stringExistingCreditCard = view.getInputRegex(EXISTING_CREDIT_CARD, REGEX_BOOLEAN);

        // break out of function
        if(stringExistingCreditCard == null)
        {
            return null;
        }

        // convert String to boolean
        boolean existingCreditCard = "1".equals(stringExistingCreditCard);

        CreditCard creditCard;

        // select from existing credit cards
        if(existingCreditCard == true) {

            // get credit card
            creditCard = view.getInputArray(creditCards, "number of the credit card", REGEX_NUMBERS);

            //check whether credit card was found
            if (creditCard == null) {
                return null;
            }

            // print credit card
            view.displayText(creditCard.toString());
        }
        else
        {
            // create new credit card
            creditCard = createCreditCard(view);

            // break out of method
            if(creditCard == null)
            {
                return null;
            }

            // check whether this credit card already exists
            // iterate through all existing guests
            for(CreditCard creditCardIterator : creditCards)
            {

                // check whether the credit card number is the same
                if(creditCardIterator.getCardNumber() == creditCard.getCardNumber())
                {
                    view.displayText("\nA credit card with these credentials already exists in this guest's profile.\n\n");
                    return null;
                }
            }

            // add credit card to ArrayLIst
            creditCards.add(creditCard);

            // print credit card
            view.displayText(creditCard.toString());

            // display text
            view.displayText("\nThe credit card has been added to this guest.\n\n");
        }

        return creditCard;
    }

}
