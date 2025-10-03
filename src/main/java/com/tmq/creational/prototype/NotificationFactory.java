package com.tmq.creational.prototype;

public class NotificationFactory {
    Notification notification;

    public NotificationFactory(Notification notification) {
        this.notification = notification;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public Notification cloneNotification(){
        return (Notification) notification.copy();
    }
}
