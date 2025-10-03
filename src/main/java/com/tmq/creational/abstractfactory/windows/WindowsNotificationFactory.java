package com.tmq.creational.abstractfactory.windows;

import com.tmq.creational.abstractfactory.Notification;
import com.tmq.creational.abstractfactory.NotificationFactory;

public class WindowsNotificationFactory implements NotificationFactory {
    @Override
    public Notification createNotification() {
        return new  WindowsNotification();
    }
}
