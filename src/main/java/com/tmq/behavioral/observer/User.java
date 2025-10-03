package com.tmq.behavioral.observer;

public class User implements Observer {
    private String name;

    public User(String name) {
        this.name = name;
    }

    @Override
    public void handleEvent(Message message) {
        System.out.printf("%s в ленте появилось новое сообщение: \n%s\n\n", name, message);
    }
}
