package com.tmq.structural.facade;

public class NotificationSender {
    public void send(Notification notification) {
        System.out.printf("Сообщение отправлено\nОт:%s\nКому:%s\nТема:%s\nСодержание:%s\n",
                notification.fromUser(),
                notification.toUser(),
                notification.subject(),
                notification.messsage());
    }
}
