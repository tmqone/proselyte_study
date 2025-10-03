package com.tmq.behavioral.strategy;


public class DeliveredStrategy implements Strategy {
    @Override
    public String toString() {
        return "DeliveredState";
    }

    @Override
    public void send(Message message) {
        System.out.println("Письмо доставлено");
    }

}
