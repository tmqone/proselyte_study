package com.tmq.creational.factory;

public class PushNotification implements Notification {
    private final String message;
    private final String recipient;

    public PushNotification(String message, String recipient) {
        this.message = message;
        this.recipient = recipient;
    }
    @Override
    public void sendNotification() {
        System.out.printf("""
                Recipient: %s
                Push notification: %s\n
                """,  recipient, message);
    }
}
