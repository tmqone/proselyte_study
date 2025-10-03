package com.tmq.structural.decorator;

import java.util.UUID;

public class Main {
    static void main() {
        Message message = new Message("Иван", "Артем", "Привет");
        Notifier notifier = new PriorityConsoleNotifier(new ConsoleNotifier(), Priority.HIGH);
        notifier.send(message);
    }
}
