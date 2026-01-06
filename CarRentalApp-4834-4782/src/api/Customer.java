package api;

public class Customer {
    private String afm;
    private String firstName;
    private String lastName;
    private String telephone;
    private String email;
    private String[] customerData = new String[5];

    public Customer(String afm, String firstName, String lastName, String telephone, String email) {
        this.afm = afm;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephone = telephone;
        this.email = email;

        customerData[0] = afm;
        customerData[1] = firstName;
        customerData[2] = lastName;
        customerData[3] = telephone;
        customerData[4] = email;
    }

    public void refreshCustomerData(){
        customerData[0] = afm;
        customerData[1] = firstName;
        customerData[2] = lastName;
        customerData[3] = telephone;
        customerData[4] = email;
    }

    public String[] getCustomerData(){
        return customerData;
    }

    @Override
    public String toString() {
        return afm + "," + firstName + "," + lastName + "," + telephone + "," + email;
    }

    public String getAfm() { return afm; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getTelephone() { return telephone; }

    public String getEmail() { return email; }

    public void setAfm(String afm) { this.afm = afm; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public void setLastName(String lastName) {  this.lastName = lastName; }

    public void setTelephone(String telephone) { this.telephone = telephone; }

    public void setEmail(String email) { this.email = email; }

}
