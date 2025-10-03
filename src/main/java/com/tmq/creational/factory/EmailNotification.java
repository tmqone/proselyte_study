package com.tmq.creational.factory;

public class EmailNotification implements Notification {
    private String message;
    private String recipient;

    public EmailNotification(String message, String recipient) {
        this.message = message;
        this.recipient = recipient;
    }
    @Override
    public void sendNotification() {
        System.out.printf("""
                Recipient: %s
                Email notification: %s\n
                """,  recipient, message);
    }
}
