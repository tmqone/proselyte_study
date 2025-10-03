package com.tmq.behavioral.interpreter;

public class OrExpression implements Expression {
    private Expression firstExpression;
    private Expression secondExpression;

    public OrExpression(Expression firstExpression, Expression secondExpression) {
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;
    }

    @Override
    public boolean interpret(Message message) {
        return firstExpression.interpret(message) || secondExpression.interpret(message);
    }
}
