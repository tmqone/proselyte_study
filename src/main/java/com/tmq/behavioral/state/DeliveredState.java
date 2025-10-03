package com.tmq.behavioral.state;

public class DeliveredState implements State {
    @Override
    public String toString() {
        return "DeliveredState";
    }

    @Override
    public void send(Message message) {
        System.out.println("Письмо доставлено");
        message.setState(new CancelledState());
    }

}
