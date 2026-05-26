package com.jerme.sis.services;

import com.jerme.sis.util.*;
import java.util.Arrays;

public class TeacherAccountService extends AccountService {

    private static final String[] DEPARTMENTS = {"CEIT", "COED", "CABA", "CPAG", "CAS"};

    public TeacherAccountService(EmailAuthenticator emailAuth, InputAuthenticator inputAuth) {
        super(emailAuth, inputAuth);
    }

    public boolean hasProperDepartment(String department) {
        return Arrays.stream(DEPARTMENTS).anyMatch(department::equalsIgnoreCase)
                && !inputAuth.hasNumber(department)
                && !inputAuth.hasSpecialChar(department)
                && !inputAuth.hasSpecialUsedChar(department)
                && !department.isBlank();
    }

    public boolean hasProperTeacherLevel(String teacherLevel) {
        if (hasProperIntInput(teacherLevel))
            return Integer.parseInt(teacherLevel) >= 1 && Integer.parseInt(teacherLevel) <= 3;

        return false;
    }

    public void printDepartmentError(String department) {
        if (department.isBlank()) {
            System.out.println("Your input is blank.");
        } else {
            if (Arrays.stream(DEPARTMENTS).noneMatch(department::equalsIgnoreCase))
                System.out.println("Your department does not exists.");
            if (inputAuth.hasNumber(department))
                System.out.println("Your input should not contain a number.");
            if (inputAuth.hasSpecialChar(department) || inputAuth.hasSpecialUsedChar(department))
                System.out.println("Your input should not contain a special character.");
        }
    }

    public void printTeacherLevelError(String teacherLevel) {
        if (hasProperIntInput(teacherLevel)) {
            if (!hasProperTeacherLevel(teacherLevel))
                System.out.println("Your input has exceeded level limit.");
        } else {
            System.out.println("Your input has non-numerical characters.");
        }
    }
}