package com.tmq;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

public class Foo {
    private boolean isFirstPrinted = false;
    private boolean isSecondPrinted = false;

    private void reset() {
        isFirstPrinted = false;
        isSecondPrinted = false;
    }

    public synchronized void first() {
        System.out.print("first");
        isFirstPrinted = true;
        notifyAll();
    }

    public synchronized void second() throws InterruptedException {
        while (!isFirstPrinted) {
            wait();
        }
        isSecondPrinted = true;
        System.out.print("second");
        notifyAll();
    }

    public synchronized void third() throws InterruptedException {
        while (!isSecondPrinted) {
            wait();
        }
        System.out.print("third");
        reset();
    }
}