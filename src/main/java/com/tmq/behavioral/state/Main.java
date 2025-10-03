package com.tmq.behavioral.state;

public class Main {
    static void main() throws InterruptedException {
        Message message = new Message("Иван", "Артем", "Мониторинг");
        for (int i = 0; i < 10; i++) {
            message.send();
        }
    }
}
