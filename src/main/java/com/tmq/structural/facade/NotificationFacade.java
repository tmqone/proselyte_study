package com.tmq.structural.facade;

public class NotificationFacade {
    NotificationLogger logger = new NotificationLogger();
    NotificationSender notificationSender = new NotificationSender();
    NotificationValidator notificationValidator = new NotificationValidator();

    public void sendNotification(String subject, String message, String fromUser, String toUser) {
        if (notificationValidator.isInvalid(subject, message, fromUser, toUser)) return;
        Notification notification = new Notification(subject, message, fromUser, toUser);
        logger.logNotification(notification);
        notificationSender.send(notification);

    }
}
