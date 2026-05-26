package com.jerme.sis.util;

public class EmailAuthenticator {

    private static final InputAuthenticator INPUT_AUTH = new InputAuthenticator();

    private static final String ONLY_ACCEPTED_DOMAIN = "@plv.edu.ph";

    public boolean hasProperEmailDomain(String email) {
        return email.endsWith(ONLY_ACCEPTED_DOMAIN);
    }

    public boolean hasSpecCharBeforeDomain(String email) {
        if (email.length() < ONLY_ACCEPTED_DOMAIN.length())
            return false;

        return INPUT_AUTH.hasSpecialChar(email.substring(0, email.length() - ONLY_ACCEPTED_DOMAIN.length()));
    }

    public boolean hasSpaceBeforeDomain(String email) {
        if (email.length() < ONLY_ACCEPTED_DOMAIN.length())
            return false;

        return INPUT_AUTH.hasSpace(email.substring(0, email.length() - ONLY_ACCEPTED_DOMAIN.length()));
    }
}