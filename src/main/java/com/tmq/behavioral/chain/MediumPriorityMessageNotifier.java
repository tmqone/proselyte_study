package com.tmq.behavioral.chain;

public class MediumPriorityMessageNotifier extends Notifier{
    public static final String YELLOW = "\u001B[33m";

    public MediumPriorityMessageNotifier(Priority priority, int levelOfPriorityToHandle) {
        super(priority, levelOfPriorityToHandle);
    }

    @Override
    public void handleMessage(Message message) {
        if (levelOfPriorityToHandle <= message.priority().weight) {
            System.out.printf("%s + %s + %s\n", YELLOW, message.message(), RESET);
        }
        if (nextNotifier != null) nextNotifier.handleMessage(message);
    }
}
