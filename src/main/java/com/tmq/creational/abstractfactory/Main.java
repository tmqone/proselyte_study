package com.tmq.creational.abstractfactory;


import com.tmq.creational.abstractfactory.linux.LinuxNotificationFactory;
import com.tmq.creational.abstractfactory.macos.MacOsNotificationFactory;
import com.tmq.creational.abstractfactory.windows.WindowsNotificationFactory;

public class Main {
    static void main() {
        NotificationFactory notificationFactory = new LinuxNotificationFactory();
        Notification notification = notificationFactory.createNotification();
        notification.printNotification();
        notification.writeNotification();
    }
}
