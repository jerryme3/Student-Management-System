package com.jerme.sis.services;

import com.jerme.sis.util.*;
import java.util.Arrays;

public class StudentAccountService extends AccountService {

    public static final String[] OFFERED_PROGRAMS = {
            "BSIT", "BSEE", "BSCE",
            "BSP", "BSED", "BSFM",
            "BSCS"
    };

    public StudentAccountService(EmailAuthenticator emailAuth, InputAuthenticator inputAuth) {
        super(emailAuth, inputAuth);
    }

    public boolean hasOfferedProgram(String program) {
        return Arrays.stream(OFFERED_PROGRAMS).
                anyMatch(program::equalsIgnoreCase)
                && !program.isBlank()
                && !inputAuth.hasSpecialChar(program)
                && !inputAuth.hasSpace(program)
                && !inputAuth.hasSpecialUsedChar(program);
    }

    public boolean hasProperGWA(String gwa) {
        try {
            return Double.parseDouble(gwa) >= 1.0
                    && Double.parseDouble(gwa) <= 5.0
                    && hasProperDoubleInput(gwa);
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public void printGWAError(String gwa) {

        try {

            if (Double.parseDouble(gwa) < 1.0 || Double.parseDouble(gwa) > 5.0)
                System.out.println("Your GWA has passed the range.");

        } catch (NumberFormatException e) {
            if (gwa.isBlank())
                System.out.println("Your input is blank.");
            if (gwa.startsWith("."))
                System.out.println("Your input should start with numerical value.");
            if (gwa.endsWith("."))
                System.out.println("Your input should end with decimal value.");
            if (inputAuth.hasLetter(gwa))
                System.out.println("Your input should not have letters.");
            if (inputAuth.hasSpace(gwa))
                System.out.println("Your input should not have spaces.");
            if (inputAuth.hasSpecialChar(gwa))
                System.out.println("Your input should not have special characters.");
            if (!inputAuth.hasNumber(gwa))
                System.out.println("Your input should have numbers.");
        }
    }

    public void printOfferedProgramError(String program) {
        if (program.isBlank())
            System.out.println("Your program input is blank.");
        else {
            if (Arrays.stream(OFFERED_PROGRAMS).noneMatch(program::equalsIgnoreCase))
                System.out.println("PLV is not offering this course or program.");
            if (inputAuth.hasSpecialChar(program) || inputAuth.hasSpecialUsedChar(program))
                System.out.println("Your program input should not contain special characters.");
            if (inputAuth.hasSpace(program))
                System.out.println("Your program input should not contain spaces.");
            if (inputAuth.hasNumber(program))
                System.out.println("Your program input should not contain numbers.");
        }
    }
}
