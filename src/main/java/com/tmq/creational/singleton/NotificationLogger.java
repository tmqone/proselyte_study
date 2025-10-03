package com.tmq.creational.singleton;

public class NotificationLogger {
    private static NotificationLogger INSTANCE;
    private static String logNotification = "Notification Log\n";

    private NotificationLogger() {
    }

    public static synchronized NotificationLogger getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NotificationLogger();
        }
        return INSTANCE;
    }

    public void addLogInfo(String logInfo) {
        logNotification = logNotification + logInfo + "\n";
    }

    public String getLogInfo() {
        return logNotification;
    }
}
