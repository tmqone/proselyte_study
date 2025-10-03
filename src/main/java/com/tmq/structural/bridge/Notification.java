package com.tmq.structural.bridge;

public interface Notification {
    void deliver(String subject, String message);
}
