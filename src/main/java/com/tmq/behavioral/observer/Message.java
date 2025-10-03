package com.tmq.behavioral.observer;

import java.time.LocalDateTime;

public class Message {
    String content;
    LocalDateTime timestamp;

    public Message(String content) {
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Время: %s%nКонтент:%s".formatted(timestamp, content);
    }
}
