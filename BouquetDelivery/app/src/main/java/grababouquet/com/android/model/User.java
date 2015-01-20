package grababouquet.com.android.model;

/**
 * Created by Sivly on 1/20/2015.
 */
public class User {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String zipCode;


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getAddress() {
        return addressLine1 + " " + addressLine2;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}


