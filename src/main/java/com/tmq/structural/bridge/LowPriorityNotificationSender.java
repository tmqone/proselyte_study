package com.tmq.structural.bridge;

public class LowPriorityNotificationSender extends NotificationSender {
    private static final String GREEN = "\u001B[32m";

    public LowPriorityNotificationSender(Notification notification, String subject, String message) {
        super(notification, subject, message);
    }

    @Override
    public void sendNotification() {
        notification.deliver(subject, GREEN + message + RESET);
    }
}
