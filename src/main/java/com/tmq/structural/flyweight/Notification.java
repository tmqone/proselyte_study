package com.tmq.structural.flyweight;

public class Notification {
    private String ansiColor;
    private static final String RESET = "\u001B[0m";

    public Notification(String ansiColor) {
        this.ansiColor = ansiColor;
    }

    public void render(String message){
        System.out.printf("%s %s %s\n", ansiColor, message, RESET);
    }
}
