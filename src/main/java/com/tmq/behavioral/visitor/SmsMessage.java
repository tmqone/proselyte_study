package com.tmq.behavioral.visitor;

public class SmsMessage extends Message {
    public SmsMessage(String to, String message) {
        super(to, message);
    }

    @Override
    protected void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
