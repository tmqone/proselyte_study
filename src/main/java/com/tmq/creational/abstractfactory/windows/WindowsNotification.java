package com.tmq.creational.abstractfactory.windows;

import com.tmq.creational.abstractfactory.Notification;

public class WindowsNotification implements Notification {
    @Override
    public void printNotification() {
        System.out.println("Windows Notification is printed");
    }

    @Override
    public void writeNotification() {
        System.out.println("Windows Notification is written");
    }
}
