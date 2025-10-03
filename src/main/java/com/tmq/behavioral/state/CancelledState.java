package com.tmq.behavioral.state;

public class CancelledState implements State {
    @Override
    public String toString() {
        return "CancelledState";
    }

    @Override
    public void send(Message message) {
        System.out.println("Письмо отозвано");
        message.setState(new DraftState());
    }
}
