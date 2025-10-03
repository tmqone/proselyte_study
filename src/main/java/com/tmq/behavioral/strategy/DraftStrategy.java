package com.tmq.behavioral.strategy;


public class DraftStrategy implements Strategy {
    @Override
    public String toString() {
        return "DraftState";
    }

    @Override
    public void send(Message message) {
        System.out.println("Создан черновик письма");
    }
}
