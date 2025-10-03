package com.tmq.structural.bridge;

public abstract class NotificationSender {
    protected static final String RESET = "\u001B[0m";
    protected final Notification notification;
    protected final String subject;
    protected final String message;

    public NotificationSender(Notification notification, String subject, String message) {
        this.notification = notification;
        this.subject = subject;
        this.message = message;
    }

    public abstract void sendNotification();
}
