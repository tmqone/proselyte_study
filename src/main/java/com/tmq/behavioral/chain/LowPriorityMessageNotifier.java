package com.tmq.behavioral.chain;

public class LowPriorityMessageNotifier extends Notifier {
    public static final String GREEN = "\u001B[32m";

    public LowPriorityMessageNotifier(Priority priority, int levelOfPriorityToHandle) {
        super(priority, levelOfPriorityToHandle);
    }

    @Override
    public void handleMessage(Message message) {
        if (levelOfPriorityToHandle <= message.priority().weight) {
            System.out.printf("%s + %s + %s\n", GREEN, message.message(), RESET);
        }
        if (nextNotifier != null) nextNotifier.handleMessage(message);
    }
}
