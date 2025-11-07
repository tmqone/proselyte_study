package com.tmq.validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InputValidator {
    private static final InputValidator INSTANCE = new InputValidator();

    public static InputValidator getInstance() {
        return INSTANCE;
    }

    public boolean validate(String name){
        return name == null || name.trim().isEmpty();
    }

    public boolean validateLongString(String number){
        try {
            if (number == null || number.isEmpty()) return true;
            number = number.trim();
            Long.parseLong(number);
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }

    public boolean validateInput(String... input) {
        for (String s : input) {
            if (validate(s)) return true;
        }
        return false;
    }
}
