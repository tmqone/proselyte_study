package com.tmq.behavioral.mediator;

import java.util.ArrayList;
import java.util.List;

public class MessageMediator implements Mediator {
    private List<User> users = new ArrayList<>();

    @Override
    public void sendMessage(Message message) {
        for (User u : users) {
            if (message.user() != u) u.receiveMessage(message);
        }
    }

    @Override
    public void addUser(User user) {
        users.add(user);
    }
}
