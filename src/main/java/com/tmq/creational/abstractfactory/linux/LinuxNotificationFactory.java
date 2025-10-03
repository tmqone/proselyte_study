package com.tmq.creational.abstractfactory.linux;

import com.tmq.creational.abstractfactory.Notification;
import com.tmq.creational.abstractfactory.NotificationFactory;

public class LinuxNotificationFactory implements NotificationFactory {
    @Override
    public Notification createNotification() {
        return new LinuxNotification();
    }
}
