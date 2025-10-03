package com.tmq.structural.composite;

public interface Recipient {
    String getName();
    void printInfo(int level);
    default void printInfo(){
        printInfo(0);
    };
}
