package com.tmq.structural.decorator;

public class PriorityConsoleNotifier implements Notifier {
    private Notifier notifier;
    private Priority priority;

    public PriorityConsoleNotifier(Notifier notifier, Priority priority) {
        this.notifier = notifier;
        this.priority = priority;
    }

    @Override
    public void send(Message message) {
        System.out.printf("[PRIORITY: %s]\n", priority.name());
        notifier.send(message);
    }
}
