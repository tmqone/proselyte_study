package com.tmq.structural.adapter;

public class LegacySmsNotifier {
    private String message;
    public LegacySmsNotifier(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    void printMessage() {
        System.out.println(message);
    }
}
