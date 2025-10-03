package com.tmq.creational.abstractfactory.linux;

import com.tmq.creational.abstractfactory.Notification;

public class LinuxNotification implements Notification {

    @Override
    public void printNotification() {
        System.out.println("Linux Notification is printed");
    }

    @Override
    public void writeNotification() {
        System.out.println("Linux Notification is written");
    }
}
