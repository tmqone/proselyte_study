package com.tmq.structural.bridge;

public class Main {
    void main() {
        NotificationSender highPriorityEmailNotification =
                new HighPriorityNotificationSender(new EmailNotification(), "Job", "Job to be done");
        NotificationSender midPrioritySmsNotification =
                new MidPriorityNotificationSender(new SmsNotification(), "Job", "Job to be done");
        NotificationSender lowPriorityPushNotification =
                new LowPriorityNotificationSender(new PushNotification(), "Job", "Job to be done");
        highPriorityEmailNotification.sendNotification();
        midPrioritySmsNotification.sendNotification();
        lowPriorityPushNotification.sendNotification();
    }
}
