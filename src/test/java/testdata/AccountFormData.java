package testdata;

import java.util.function.Consumer;

public class AccountFormData {

    private final String email;
    private final String firstName;
    private final String lastName;
    private final String password;
    private final String city;
    private final String country;
    private final String state;
    private final String postalCode;
    private final String address;
    private final String mobilePhone;
    private final String alias;

    public AccountFormData(String email, String firstName, String lastName, String password, String city, String country, String state, String postalCode, String address, String mobilePhone, String alias) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.city = city;
        this.country = country;
        this.state = state;
        this.postalCode = postalCode;
        this.address = address;
        this.mobilePhone = mobilePhone;
        this.alias = alias;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getAddress() {
        return address;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public String getAlias() {
        return alias;
    }

    public static class Builder {
        public String email;
        public String firstName;
        public String lastName;
        public String password;
        public String city;
        public String country;
        public String state;
        public String postalCode;
        public String address;
        public String mobilePhone;
        public String alias;

        public Builder with(Consumer<Builder> builderFunction) {
            builderFunction.accept(this);
            return this;
        }

        public AccountFormData create() {
            return new AccountFormData(email, firstName, lastName, password, city, country, state, postalCode, address, mobilePhone, alias);
        }
    }

}
