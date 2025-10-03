package com.tmq.creational.factory;

public class EmailNotificationFactory extends NotificationFactory {
    @Override
    public EmailNotification createNotification(String message, String recipient) {
        return new EmailNotification(message,recipient);
    }
}
