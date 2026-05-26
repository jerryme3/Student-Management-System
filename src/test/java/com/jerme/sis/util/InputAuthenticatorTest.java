package com.jerme.sis.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputAuthenticatorTest {

    static InputAuthenticator ia = new InputAuthenticator();



    @Test
    void hasLetter() {
        assertTrue(ia.hasLetter("jerme"));
    }

    @Test
    void hasNumber() {
        assertTrue(ia.hasNumber("jerme123"));
    }

    @Test
    void hasSpace() {
        assertTrue(ia.hasSpace("jer me"));
    }

    @Test
    void hasSpecialChar() {
        assertTrue(ia.hasSpecialChar("jerme!"));
    }

    @Test
    void hasSpecialUsedChar() {
        assertTrue(ia.hasSpecialUsedChar("jerme.',"));
    }

    @Test
    void hasCharactersThatAreForNameOnly() {
        var name = "Jerme Cyrus C. San Diego";
        var toBeTest = !(ia.hasNumber(name) && ia.hasSpecialChar(name));

        assertTrue(toBeTest);

    }
}