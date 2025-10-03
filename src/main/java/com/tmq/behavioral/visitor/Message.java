package com.tmq.behavioral.visitor;

public abstract class Message {
    protected String to;
    protected String message;
    public Message(String to, String message) {
        this.to = to;
        this.message = message;
    }

    protected abstract void accept(Visitor visitor);
}
