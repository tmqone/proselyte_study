package com.tmq.behavioral.state;

public class DeliveringState implements State {
    @Override
    public String toString() {
        return "DeliveringState";
    }

    @Override
    public void send(Message message) {
        System.out.println("Письмо в процессе отправки");
        message.setState(new DeliveredState());
    }
}
