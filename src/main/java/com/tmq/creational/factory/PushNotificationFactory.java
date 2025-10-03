package com.tmq.creational.factory;

public class PushNotificationFactory extends NotificationFactory {
    @Override
    public Notification createNotification(String message, String recipient) {
        return new PushNotification(message,recipient);
    }
}
