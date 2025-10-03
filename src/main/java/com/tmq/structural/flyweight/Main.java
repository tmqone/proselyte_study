package com.tmq.structural.flyweight;

public class Main {
    public static void main(String[] args) {
        Notification lowPriority = PriorityColorFactory.getPriority(Priority.LOW);
        Notification mediumPriority = PriorityColorFactory.getPriority(Priority.MEDIUM);
        Notification highPriority = PriorityColorFactory.getPriority(Priority.HIGH);

        lowPriority.render("hello");
        mediumPriority.render("hello");
        highPriority.render("hello");
    }
}
