package com.tmq.validator;

import com.tmq.exception.NotCorrectInputException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;

public class InputValidator {
    public void validate(String name){
        if (name == null || name.trim().isEmpty()) throw new NotCorrectInputException("Ошибка валидации полей");
    }

    public void validateLongString(String number){
        try {
            if (number == null || number.isEmpty()) throw new NotCorrectInputException("Ошибка валидации полей");
            number = number.trim();
            Long.parseLong(number);
        } catch (NumberFormatException e) {
            throw new NotCorrectInputException("Ошибка валидации полей");
        }
    }

    public void validateInput(String... input) {
        Arrays.stream(input).forEach(this::validate);
    }
}
