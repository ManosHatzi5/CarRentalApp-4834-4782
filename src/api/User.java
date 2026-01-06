package api;

public class User {

    String[] userData = new String[5];

    public User(String firstName, String lastName, String username, String email, String password) {
        this.userData[0] = firstName.trim();
        this.userData[1] = lastName.trim();
        this.userData[2] = username.trim();
        this.userData[3] = email.trim();
        this.userData[4] = password.trim();
    }

    @Override
    public String toString() {
        return getUserData()[0] + "," + getUserData()[1] + "," + getUserData()[2] + "," + getUserData()[3] +
                "," + getUserData()[4];
    }

    public String[] getUserData() { return userData; }

    public String getEmail() { return userData[3]; }
}

