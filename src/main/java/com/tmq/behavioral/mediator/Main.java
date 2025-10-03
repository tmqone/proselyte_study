package com.tmq.behavioral.mediator;

public class Main {
    static void main() {
        User artem = new User("Артем");
        User ivan = new User("Ivan");
        User petr = new User("Petr");

        Message artemMessage = new Message(artem, "Я Артём");
        Message ivanMessage = new Message(ivan, "Я Иван");
        Message petrMessage = new Message(petr, "Я Пётр");

        MessageMediator mediator = new MessageMediator();
        mediator.addUser(artem);
        mediator.addUser(ivan);
        mediator.addUser(petr);

        mediator.sendMessage(artemMessage);
        mediator.sendMessage(ivanMessage);
        mediator.sendMessage(petrMessage);
    }
}
