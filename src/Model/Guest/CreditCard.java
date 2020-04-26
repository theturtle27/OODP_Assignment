package Model.Guest;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import Persistence.Entity;

public class CreditCard extends Entity {

    private String cardHolderName;
    private long cardNumber;
    private YearMonth expirationDate;
    private PaymentNetwork paymentNetwork;
    private short cardValidationCode;

    public CreditCard(String cardHolderName, long cardNumber, YearMonth expirationDate, PaymentNetwork paymentNetwork, short cardValidationCode)
    {
        this.cardHolderName = cardHolderName;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.paymentNetwork = paymentNetwork;
        this.cardValidationCode = cardValidationCode;
    }

    public long getCardNumber()
    {
        return cardNumber;
    }

    public String toString()
    {

        // create a string with 12 stars
        StringBuffer stringBuffer = new StringBuffer(12);
        for (int i = 0; i < 12; i++)
        {
            stringBuffer.append("*");
        }

        // convert card number to String
        String stringCardNumber = String.valueOf(cardNumber);

        // get card number in the format: <12*><last four digits of credit card number>
        String stringPrintCardNumber = stringBuffer.toString() + stringCardNumber.substring(stringCardNumber.length()-4, stringCardNumber.length());

        // convert payment network to String
        String stringPaymentNetwork = capitalizeFirstLetter(paymentNetwork.toString());

        // create a formatter for date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");

        String stringExpirationDate = expirationDate.format(formatter);

        return    "\n------------Credit Card------------"
                + "\nCard Holder Name : " + cardHolderName
                + "\nCard Number      : " + stringPrintCardNumber
                + "\nPayment Network  : " + stringPaymentNetwork
                + "\nExpiration Date  : " + stringExpirationDate + "\n";

    }

}

