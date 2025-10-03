package com.tmq.behavioral.mediator;

public class User {
    private String name;

    public User(String name) {
        this.name = name;
    }

    void receiveMessage(Message message) {
        System.out.printf("Пользователь %s получил сообщение %s",  name, message);
    }

    @Override
    public String toString() {
        return name;
    }
}
