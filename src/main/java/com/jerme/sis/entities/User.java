package com.jerme.sis.entities;

public abstract class User {

    private final int id;
    private final String firstName;
    private final String middleName;
    private final String lastName;
    private final String email;
    private final String password;

    public User(int id,
                String firstName,
                String middleName,
                String lastName,
                String email,
                String password
    ) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
