package com.tmq.behavioral.interpreter;

public interface Expression {
    boolean interpret(Message message);
}
