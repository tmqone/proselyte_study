package com.tmq.behavioral.chain;

public class Main {
    static void main() {
        Message message = new Message("Hello", Priority.HIGH);

        Notifier low = new LowPriorityMessageNotifier(Priority.LOW, 1);
        Notifier medium = new MediumPriorityMessageNotifier(Priority.MEDIUM, 2);
        Notifier high = new HighPriorityMessageNotifier(Priority.HIGH, 3);

        low.setNextNotifier(medium);
        medium.setNextNotifier(high);
        low.handleMessage(message);
        medium.handleMessage(message);
        high.handleMessage(message);
    }
}
