package com.tmq.processor;

import lombok.experimental.UtilityClass;
import org.hibernate.exception.ConstraintViolationException;

@UtilityClass
public class ConstraintExceptionProccesor {
    public static String process(ConstraintViolationException e) {
        return switch (e.getConstraintName()) {
            case "users_username_key" -> "Пользователь с таким именем уже существует";
            case "events_user_id_fkey" -> {
                if (e.getMessage().contains("is not present in table \"users\"")) {
                    yield "Пользователь не найден";
                } else {
                    yield "Пользователь не может быть удалён, т.к. у него имеются связанные сущности";
                }
            }
            case "events_file_id_fkey" -> {
                if (e.getMessage().contains("is not present in table \"files\"")) {
                    yield "Файл не найден";
                } else {
                    yield "\"Файл не может быть удалён т.к. у него имеются связанные сущности\"";
                }
            }
            default -> "Ошибка при сохранении в БД";
        };
    }
}
