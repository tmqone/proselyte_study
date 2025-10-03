package com.tmq.behavioral.chain;

public class HighPriorityMessageNotifier extends Notifier{
    public static final String RED = "\u001B[31m";

    public HighPriorityMessageNotifier(Priority priority, int levelOfPriorityToHandle) {
        super(priority, levelOfPriorityToHandle);
    }

    @Override
    public void handleMessage(Message message) {
        if (levelOfPriorityToHandle <= message.priority().weight) {
            System.out.printf("%s + %s + %s\n", RED, message.message(), RESET);
        }
        if (nextNotifier != null) nextNotifier.handleMessage(message);
    }
}
