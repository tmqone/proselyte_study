package com.tmq.creational.singleton;

public class Main {
    static void main() {
        NotificationLogger notificationLogger = NotificationLogger.getInstance();
        notificationLogger.addLogInfo("Test1");
        notificationLogger.addLogInfo("Test2");
        notificationLogger.addLogInfo("Test3");

        System.out.println(notificationLogger.getLogInfo());
    }
}
