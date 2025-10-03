package com.tmq.structural.flyweight;

import java.util.HashMap;
import java.util.Map;

public class PriorityColorFactory {
    private static final Map<Priority, Notification> PRIORITY_NOTIFICATION_MAP = new HashMap();

    public static Notification getPriority(Priority priority){
        if (!PRIORITY_NOTIFICATION_MAP.containsKey(priority)){
            switch (priority){
                case LOW -> PRIORITY_NOTIFICATION_MAP.put(Priority.LOW, new Notification("\u001B[32m"));
                case MEDIUM -> PRIORITY_NOTIFICATION_MAP.put(Priority.MEDIUM, new Notification("\u001B[33m"));
                case HIGH -> PRIORITY_NOTIFICATION_MAP.put(Priority.HIGH, new Notification("\u001B[31m"));
            }
        }
        return PRIORITY_NOTIFICATION_MAP.get(priority);
    }
}
