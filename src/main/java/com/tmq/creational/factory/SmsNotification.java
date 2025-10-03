package com.tmq.creational.factory;

public class SmsNotification implements Notification {
    private final String message;
    private final String recipient;

    public SmsNotification(String message, String recipient) {
        this.message = message;
        this.recipient = recipient;
    }

    @Override
    public void sendNotification() {
        System.out.printf("""
                Recipient: %s
                SMS notification: %s\n
                """,  recipient, message);
    }
}
