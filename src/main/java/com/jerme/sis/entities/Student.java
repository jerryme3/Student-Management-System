package com.jerme.sis.entities;

public class Student extends User {

    private final String program;
    private final double gwa;

    public Student(int id,
                   String firstName,
                   String middleName,
                   String lastName,
                   String email,
                   String password,
                   String program,
                   double gwa) {
        super(id, firstName, middleName, lastName, email, password);
        this.program = program;
        this.gwa = gwa;
    }

    public String getProgram() {
        return program;
    }

    public double getGwa() {
        return gwa;
    }
}
