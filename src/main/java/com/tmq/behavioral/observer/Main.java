package com.tmq.behavioral.observer;

public class Main {
    static void main() {
        Feed feed = new Feed();
        User ivan = new User("Иван");
        User artem = new User("Артем");

        feed.addObserver(ivan);
        feed.addObserver(artem);

        feed.addMessage(new Message("Это мой первый пост"));
    }
}
