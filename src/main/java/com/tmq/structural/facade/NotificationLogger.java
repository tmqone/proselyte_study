package com.tmq.structural.facade;

public class NotificationLogger {
    public void logNotification(Notification notification) {
        System.out.printf("Идёт отправка сообщения от пользователя %s пользователю %s с темой %s\n",
                notification.fromUser(),
                notification.toUser(),
                notification.subject());
    }
}
