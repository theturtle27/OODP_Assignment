package Model.Guest;

import java.util.ArrayList;
import Persistence.Entity;

public class Guest extends Entity{
    private String name;
    private String streetName;
    private String cityName;
    private String postalCode;
    private String country;
    private Gender gender;
    private String nationality;
    private IdentityType identityType;
    private String identityNumber;
    private String eMail;
    private String phoneNumber;
    private ArrayList<CreditCard> creditCards;

    public Guest(String name, String streetName, String cityName, String postalCode, String country, Gender gender, String nationality, IdentityType identityType, String identityNumber, String eMail, String phoneNumber, ArrayList<CreditCard> creditCards) {
        this.name = name;
        this.streetName = streetName;
        this.cityName = cityName;
        this.postalCode = postalCode;
        this.country = country;
        this.gender = gender;
        this.nationality = nationality;
        this.identityType = identityType;
        this.identityNumber = identityNumber;
        this.eMail = eMail;
        this.phoneNumber = phoneNumber;
        this.creditCards = creditCards;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setStreetName(String streetName)
    {
        this.streetName = streetName;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public void setGender(Gender gender)
    {
        this.gender = gender;
    }

    public String getNationality()
    {
        return nationality;
    }

    public void setNationality(String nationality)
    {
        this.nationality = nationality;
    }

    public IdentityType getIdentityType()
    {
        return this.identityType;
    }

    public void setIdentityType(IdentityType identityType)
    {
        this.identityType = identityType;
    }

    public String getIdentityNumber()
    {
        return this.identityNumber;
    }

    public void setIdentityNumber(String identityNumber)
    {
        this.identityNumber = identityNumber;
    }

    public void setEMail(String eMail)
    {
        this.eMail = eMail;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<CreditCard> getCreditCards()
    {
        return this.creditCards;
    }

    public String toString()
    {
        // convert gender to String
        String stringGender = capitalizeFirstLetter(gender.toString());

        // convert identity type to String
        String stringIdentityType = capitalizeFirstLetter(identityType.toString());

        //TODO: change to regular String
        // String for guest information
        StringBuffer stringGuest = new StringBuffer();
        stringGuest.append("\n===============Guest==============="
                + "\nName             : " + name
                + "\n-----------Identification----------"
                + "\nGender           : " + stringGender
                + "\nNationality      : " + nationality
                + "\nIdentity Type    : " + stringIdentityType
                + "\nIdentity Number  : " + identityNumber
                + "\n--------------Address--------------"
                + "\nStreet Name      : " + streetName
                + "\nCity Name        : " + cityName
                + "\nPostal Code      : " + postalCode
                + "\nCountry          : " + country
                + "\n--------------Contact--------------"
                + "\nEmail Address    : " + eMail
                + "\nPhone Number     : " + phoneNumber);

        return stringGuest.toString();

    }

}
