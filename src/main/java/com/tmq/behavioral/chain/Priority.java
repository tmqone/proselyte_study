package com.tmq.behavioral.chain;

public enum Priority {
    LOW(1),
    MEDIUM(2),
    HIGH(3);

    int weight;

    Priority(int weight) {
        this.weight = weight;
    }
}
