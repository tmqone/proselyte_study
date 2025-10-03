package com.tmq.behavioral.visitor;

public class EmailMessage extends Message {
    public EmailMessage(String to, String message) {
        super(to, message);
    }

    @Override
    protected void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
