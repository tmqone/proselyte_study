package com.tmq.structural.decorator;

public class ConsoleNotifier implements Notifier {

    @Override
    public void send(Message message) {
        System.out.println("[MESSAGE]\nОт: " + message.getFrom()
                + "\nКому: " + message.getTo() + "\nСообщение: " + message.getMessage() + "\n");
    }
}
