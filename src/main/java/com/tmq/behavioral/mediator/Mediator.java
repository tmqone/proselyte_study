package com.tmq.behavioral.mediator;

public interface Mediator {
    void sendMessage(Message message);
    void addUser(User user);
}
