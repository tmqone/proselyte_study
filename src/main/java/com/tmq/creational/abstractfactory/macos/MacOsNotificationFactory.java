package com.tmq.creational.abstractfactory.macos;

import com.tmq.creational.abstractfactory.Notification;
import com.tmq.creational.abstractfactory.NotificationFactory;

public class MacOsNotificationFactory implements NotificationFactory {
    @Override
    public Notification createNotification() {
        return new MacOsNotification();
    }
}
