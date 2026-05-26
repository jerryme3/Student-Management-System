package com.jerme.sis.util;

public class InputAuthenticator {

    private boolean contains(String input, Checker<Character> auth) {
        return input.chars()
                .mapToObj(c -> (char) c)
                .anyMatch(auth::check);
    }

    public boolean hasLetter(String input) {
        return contains(input, Character::isLetter);
    }

    public boolean hasNumber(String input) {
        return contains(input, Character::isDigit);
    }

    public boolean hasSpace(String input) {
        return contains(input, Character::isWhitespace);
    }

    public boolean hasSpecialChar(String input) {
        return contains(input, ch -> !Character.isLetterOrDigit(ch) && !Character.isWhitespace(ch));
    }

    public boolean hasSpecialUsedChar(String input) {
        return contains(input, ch -> ch == '.'  || ch == '\\' || ch == '`');
    }
}