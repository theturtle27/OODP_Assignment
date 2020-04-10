package Model;

import java.util.Arrays;

import Persistence.Entity;

/**
 * BillingInformation is an {@link Entity} that encapsulates informaton about billing.
 * @author YingHao
 */
public class BillingInformation extends Entity {
    private final Address address;
    private String creditCardNo;
    private String cvv;

    public BillingInformation() {
        this.address = new Address();
    }

    public Address getAddress() {
        return address;
    }

    public String getCreditCardNumber() {
        return creditCardNo;
    }

    public void setCreditCardNumber(String creditCardNo) {
        this.creditCardNo = creditCardNo;
    }

    public String getCVV() {
        return cvv;
    }

    public void setCVV(String cvv) {
        this.cvv = cvv;
    }

    public void set(BillingInformation billing) {
        this.getAddress().set(billing.getAddress());
        billing.setCreditCardNumber(this.getCreditCardNumber());
        billing.setCVV(this.getCVV());
    }

    public BillingInformation clone() {
        BillingInformation billing = new BillingInformation();
        set(billing);

        return billing;
    }

    @Override
    public String toString() {
        String ccNumberMask = getCreditCardNumber();

        // Prepare obfuscation mask
        char[] mask = new char[ccNumberMask.length() - 4];
        Arrays.fill(mask, '*');
        ccNumberMask = new String(mask) + ccNumberMask.substring(ccNumberMask.length() - 4, ccNumberMask.length());

        return "----- Billing Information -----\n" +
                "Credit card number: " + ccNumberMask + "\n" +
                getAddress().toString();
    }
}
