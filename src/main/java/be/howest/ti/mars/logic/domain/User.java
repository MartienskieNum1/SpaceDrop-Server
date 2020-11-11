package be.howest.ti.mars.logic.domain;

public class User {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;

    public User(String firstName, String lastName, String phoneNumber, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }
}
