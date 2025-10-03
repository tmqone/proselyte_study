package com.tmq.behavioral.visitor;

public interface Visitor {
    void visit(EmailMessage message);
    void visit(SmsMessage message);
}
