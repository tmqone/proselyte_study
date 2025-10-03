package com.tmq.structural.bridge;

public class PushNotification implements Notification {
    @Override
    public void deliver(String subject, String message) {
        System.out.println("[PUSH]\n" + "[SUBJECT]: " + subject + "\n" + "[MESSAGE]: " + message + "\n");
    }
}
