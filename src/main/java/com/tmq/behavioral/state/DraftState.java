package com.tmq.behavioral.state;

public class DraftState implements State {
    @Override
    public String toString() {
        return "DraftState";
    }

    @Override
    public void send(Message message) {
        System.out.println("Создан черновик письма");
        message.setState(new DeliveringState());
    }
}
