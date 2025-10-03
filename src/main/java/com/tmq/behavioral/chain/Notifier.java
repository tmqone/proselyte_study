package com.tmq.behavioral.chain;

public abstract class Notifier {
    protected Priority priority;
    protected Notifier nextNotifier;
    protected static final String RESET = "\u001B[0m";
    protected int levelOfPriorityToHandle;

    protected Notifier(Priority priority, int levelOfPriorityToHandle) {
        this.priority = priority;
        this.levelOfPriorityToHandle = levelOfPriorityToHandle;
    }

    protected void setNextNotifier(Notifier nextNotifier) {
        this.nextNotifier = nextNotifier;
    }

    public abstract void handleMessage(Message message);
}
