package com.tmq.creational.prototype;

import com.tmq.creational.builder.Priority;
import com.tmq.creational.builder.Status;

import java.time.LocalDateTime;

public class Main {
    static void main() {
        Notification mainNotification = new Notification("not", "not", LocalDateTime.now(), Status.UNREAD,
                Priority.HIGH);
        NotificationFactory notificationFactory = new NotificationFactory(mainNotification);
        Notification copy = notificationFactory.cloneNotification();

        System.out.println(copy == mainNotification);
    }
}
