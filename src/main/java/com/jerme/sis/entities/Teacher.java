package com.jerme.sis.entities;

public class Teacher extends User {

    private final String department;
    private final int teacherLevel;

    public Teacher(int teacherId,
                   String firstName,
                   String middleName,
                   String lastName,
                   String email,
                   String password,
                   String department,
                   int teacherLevel) {
        super(teacherId, firstName, middleName, lastName, email, password);
        this.department = department;
        this.teacherLevel = teacherLevel;
    }

    public String getDepartment() {
        return department;
    }

    public int getTeacherLevel() {
        return teacherLevel;
    }
}