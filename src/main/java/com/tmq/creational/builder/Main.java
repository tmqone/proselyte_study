package com.tmq.creational.builder;

import java.time.LocalDateTime;

public class Main {
    static void main() {
        Notification notification = Notification.builder()
                .title("TEST")
                .content("Test")
                .status(Status.UNREAD)
                .priority(Priority.HIGH)
                .time(LocalDateTime.now())
                .build();

        System.out.println(notification);
    }
}
