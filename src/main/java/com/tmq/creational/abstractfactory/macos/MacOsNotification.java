package com.tmq.creational.abstractfactory.macos;

import com.tmq.creational.abstractfactory.Notification;

public class MacOsNotification implements Notification {
    @Override
    public void printNotification() {
        System.out.println("Mac OS Notification is printed");
    }

    @Override
    public void writeNotification() {
        System.out.println("Mac OS Notification is written");
    }
}
