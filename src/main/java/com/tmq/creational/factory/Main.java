package com.tmq.creational.factory;

public class Main {
    static void main() {
        NotificationFactory smsNotificationFactory = createNotificationFactory("SMS");
        Notification notification = smsNotificationFactory.createNotification("hello", "Ivan");
        notification.sendNotification();
    }

    static NotificationFactory createNotificationFactory(String type) {
        switch (type.toLowerCase()) {
            case "sms" : return new SmsNotificationFactory();
            case "email" : return new EmailNotificationFactory();
            case "push" : return new PushNotificationFactory();
            default: throw new RuntimeException(type + " is not existing notification");
        }
    }

}
