package com.tmq.creational.factory;

public class SmsNotificationFactory extends NotificationFactory {
    @Override
    public Notification createNotification(String message, String recipient) {
        return new SmsNotification(message, recipient);
    }
}
