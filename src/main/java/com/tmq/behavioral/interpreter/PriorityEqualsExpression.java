package com.tmq.behavioral.interpreter;

public class PriorityEqualsExpression implements Expression {
    private Priority priority;

    public PriorityEqualsExpression(Priority priority) {
        this.priority = priority;
    }
    @Override
    public boolean interpret(Message message) {
        return message.priority() == priority;
    }
}
