package com.tmq.structural.bridge;

public class MidPriorityNotificationSender extends NotificationSender {
    private static final String YELLOW = "\u001B[33m";

    public MidPriorityNotificationSender(Notification notification, String subject, String message) {
        super(notification, subject, message);
    }

    @Override
    public void sendNotification() {
        notification.deliver(subject , YELLOW + message + RESET);
    }
}
