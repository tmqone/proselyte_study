package com.tmq.structural.bridge;

public class HighPriorityNotificationSender extends NotificationSender {
    private static final String RED = "\u001B[31m";

    public HighPriorityNotificationSender(Notification notification, String subject, String message) {
        super(notification, subject, message);
    }

    @Override
    public void sendNotification() {
        notification.deliver(subject, RED + message + RESET);
    }
}
