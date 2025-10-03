package com.tmq.structural.facade;

public class NotificationValidator {
    public boolean isInvalid(String... content) {
        for (String s : content) {
            if (s == null || s.isEmpty()) {
                System.out.printf("Параметр %s невалиден", s);
                return true;
            }
        }

        return false;
    }
}
