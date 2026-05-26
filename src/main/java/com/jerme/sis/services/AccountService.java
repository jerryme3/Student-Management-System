package com.jerme.sis.services;

import com.jerme.sis.util.*;

public abstract class AccountService {

    protected EmailAuthenticator emailAuth;
    protected InputAuthenticator inputAuth;

    public AccountService(EmailAuthenticator emailAuth,
                          InputAuthenticator inputAuth) {
        this.emailAuth = emailAuth;
        this.inputAuth = inputAuth;
    }

    public boolean hasProperDoubleInput(String stringDouble) {
        return !stringDouble.startsWith(".")
                && !stringDouble.endsWith(".")
                && !inputAuth.hasLetter(stringDouble)
                && !inputAuth.hasSpace(stringDouble)
                && inputAuth.hasNumber(stringDouble)
                && !stringDouble.isBlank();
    }

    public boolean hasProperIntInput(String stringInt) {
        return !inputAuth.hasLetter(stringInt)
                && !inputAuth.hasSpecialChar(stringInt)
                && !inputAuth.hasSpace(stringInt)
                && !inputAuth.hasSpecialUsedChar(stringInt)
                && inputAuth.hasNumber(stringInt)
                && !stringInt.isBlank();
    }

    public boolean hasProperEmail(String email) {
        if (email.length() > 13)
            return !emailAuth.hasSpecCharBeforeDomain(email)
                && !emailAuth.hasSpaceBeforeDomain(email)
                && emailAuth.hasProperEmailDomain(email)
                && !email.isBlank();

        return false;
    }

    public boolean hasProperName(String name) {
        return inputAuth.hasLetter(name)
                && !inputAuth.hasNumber(name)
                && !inputAuth.hasSpecialChar(name)
                && !name.isBlank();
    }

    public boolean hasProperPassword(String password) {
        return !inputAuth.hasSpace(password)
                && inputAuth.hasLetter(password)
                && inputAuth.hasNumber(password)
                && inputAuth.hasSpecialChar(password)
                && password.length() >= 8
                && !password.isBlank();
    }

    public void printPasswordError(String password) {
        if (password.isBlank()) {
            System.out.println("Your password is blank.");
        } else if (password.length() < 8) {
            System.out.println("Your password is too short.");
        } else {
            if (inputAuth.hasSpace(password))
                System.out.println("Your password should not contains spaces.");
            if(!inputAuth.hasLetter(password))
                System.out.println("Your password should contains letters.");
            if (!inputAuth.hasNumber(password))
                System.out.println("Your password should contains numbers.");
            if (!inputAuth.hasSpecialChar(password))
                System.out.println("Your password should contains special characters.");
        }
    }

    public void printEmailError(String email) {
        if (email.isBlank())
            System.out.println("Email is blank.");
        else if (email.length() < 13 && email.length() > 0)
            System.out.println("Your email length is too short! Have you typed your domain? (min: 13)");
        else {
            if (emailAuth.hasSpecCharBeforeDomain(email))
                System.out.println("Email has special character before the domain.");
            if (emailAuth.hasSpaceBeforeDomain(email))
                System.out.println("Email has space before the domain.");
            if (!emailAuth.hasProperEmailDomain(email))
                System.out.println("Email has different domain.");
        }
    }

    public void printNameError(String name) {
        if (name.isBlank()) {
            System.out.println("Your name is blank.");
        } else {
            if (!inputAuth.hasLetter(name))
                System.out.println("Your name does not contains any letters.");
            if (inputAuth.hasNumber(name))
                System.out.println("Your name contains numerical characters.");
            if (inputAuth.hasSpecialChar(name))
                System.out.println("Your name contains unacceptable special characters.");
        }
    }
}