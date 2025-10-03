package com.tmq.behavioral.strategy;


public class DeliveringStrategy implements Strategy {
    @Override
    public String toString() {
        return "DeliveringState";
    }

    @Override
    public void send(Message message) {
        System.out.println("Письмо в процессе отправки");
    }
}
