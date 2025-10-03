package com.tmq.behavioral.strategy;

public class CancelledStrategy implements Strategy {
    @Override
    public String toString() {
        return "CancelledState";
    }

    @Override
    public void send(Message message) {
        System.out.println("Письмо отозвано");
    }
}
