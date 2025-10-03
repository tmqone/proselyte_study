package com.tmq.behavioral.memento;

public class MessageDraftMemento {
    private final Message state;

    public MessageDraftMemento(Message state) {
        this.state = state;
    }

    public Message getState() {
        return state;
    }
}
