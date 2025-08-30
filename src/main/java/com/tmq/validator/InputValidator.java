package com.tmq.validator;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InputValidator {
    public boolean validate(Long number) {
        if (number == null) {
            System.out.println("Некорректный ввод");
            return false;
        }
        return true;
    }

    public boolean validate(String name){
        if (name == null || name.isEmpty()) {
            System.out.println("Некорректный ввод");
            return false;
        }
        return true;
    }

    public boolean validate(Long number, String name) {
        return validate(number) && validate(name);
    }
}
