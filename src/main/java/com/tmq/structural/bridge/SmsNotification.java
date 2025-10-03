package com.tmq.structural.bridge;

public class SmsNotification implements Notification {
    @Override
    public void deliver(String subject, String message) {
        System.out.println("[SMS]\n" + "[SUBJECT]: " + subject + "\n" + "[MESSAGE]: " + message + "\n");
    }
}
