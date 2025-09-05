package com.tmq.validator;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InputValidator {
    public boolean validate(String name){
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean validateLongString(String number){
        try {
            if (number == null || number.isEmpty()) return false;
            number = number.trim();
            Long.parseLong(number);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
