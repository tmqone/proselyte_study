package com.tmq.structural.proxy;

import java.util.concurrent.ExecutionException;

public interface Notifier {
    void send(Message message) throws ExecutionException, InterruptedException;
}
