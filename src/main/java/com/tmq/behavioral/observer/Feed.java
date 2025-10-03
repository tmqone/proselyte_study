package com.tmq.behavioral.observer;

import java.util.ArrayList;
import java.util.List;

public class Feed implements Observed {
    private List<Message> messages = new ArrayList<>();
    private List<Observer> observers = new ArrayList<>();

    public void addMessage(Message message) {
        messages.add(message);
        notifyObservers(message);
    }

    public void removeMessage(Message message) {
        messages.remove(message);
    }


    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers(Message message) {
        for (Observer observer : observers) {
            observer.handleEvent(message);
        }
    }
}
