package com.tmq.behavioral.strategy;

public class Main {
    static void main() throws InterruptedException {
        Message message = new Message("Иван", "Артём", "Мониторинг");
        message.send();
        message.setStrategy(new DeliveringStrategy());
        message.send();
        message.setStrategy(new DeliveredStrategy());
        message.send();
        message.setStrategy(new CancelledStrategy());
        message.send();
    }
}
