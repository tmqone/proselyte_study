package com.tmq.creational.factory;

public abstract class NotificationFactory {
    public abstract Notification createNotification(String message, String recipient);
}
