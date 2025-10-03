package com.tmq.behavioral.command;

public class Main {
    static void main() {
        Message message = new Message("Артем", "Иван", "Мониторинг", "Алерт");
        Notifier notifier = new Notifier();
        Command command = new MessageCommand(notifier, message);
        command.execute();
        command.undo();
    }
}
