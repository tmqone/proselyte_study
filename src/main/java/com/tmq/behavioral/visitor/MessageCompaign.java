package com.tmq.behavioral.visitor;

import java.util.ArrayList;
import java.util.List;

public class MessageCompaign {
    private List<Message> messageList = new ArrayList<>();
    public void addMessage(Message message) {
        messageList.add(message);
    }

    public void accept(Visitor visitor) {
        for (Message message : messageList) {
            message.accept(visitor);
        }
    }
}
