package com.tmq.structural.bridge;

public class EmailNotification implements Notification {
    @Override
    public void deliver(String subject, String message) {
        System.out.println("[EMAIL]\n" + "[SUBJECT]: " + subject + "\n" + "[MESSAGE]: " + message + "\n");
    }
}
