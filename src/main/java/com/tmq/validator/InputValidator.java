package com.tmq.validator;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InputValidator {
    public boolean validate(String name){
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Некорректный ввод");
            return false;
        }
        return true;
    }

    public boolean validateLongString(String number){
        try {
            Long.parseLong(number.trim());
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод");
            return false;
        }
        return true;
    }
}
