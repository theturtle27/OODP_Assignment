package Model;

public class Guest {
    String guestID;
    String creditNo;
    int CVV;
    String address;
    String country;
    char gender;
    String identityNo;
    String identityType;
    String nationality;
    String contact;
    String email;

    public String getGuestID() {
        return guestID;
    }

    public void setGuestID(String guestID) {
        this.guestID = guestID;
    }

    public String getCreditNo() {
        return creditNo;
    }

    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo;
    }

    public int getCVV() {
        return CVV;
    }

    public void setCVV(int CVV) {
        this.CVV = CVV;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getIdentityNo() {
        return identityNo;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Guest(String guestID, String creditNo, int CVV, String address, String country, char gender, String identityNo, String identityType, String nationality, String contact, String email) {
        this.guestID = guestID;
        this.creditNo = creditNo;
        this.CVV = CVV;
        this.address = address;
        this.country = country;
        this.gender = gender;
        this.identityNo = identityNo;
        this.identityType = identityType;
        this.nationality = nationality;
        this.contact = contact;
        this.email = email;
    }
}

