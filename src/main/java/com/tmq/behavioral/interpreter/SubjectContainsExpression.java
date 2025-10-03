package com.tmq.behavioral.interpreter;

public class SubjectContainsExpression implements Expression {
    private String subject;

    public SubjectContainsExpression(String subject) {
        this.subject = subject;
    }

    @Override
    public boolean interpret(Message message) {
        return message.subject().toLowerCase().contains(subject.toLowerCase());
    }
}
