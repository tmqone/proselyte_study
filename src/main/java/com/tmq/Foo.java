package com.tmq;

public class Foo {
    private boolean isFirstPrinted = false;
    private boolean isSecondPrinted = false;

    private void reset() {
        isFirstPrinted = false;
        isSecondPrinted = false;
    }

    public synchronized void first() {
        System.out.printf("first");
        isFirstPrinted = true;
        notifyAll();
    }

    public synchronized void second() throws InterruptedException {
        while (!isFirstPrinted) {
            // System.out.print(Thread.currentThread().getName() + " не печатаю два, т.к. единица еще не распечатана");
            wait();
        }
        isSecondPrinted = true;
        System.out.printf("second");
        notifyAll();
    }

    public synchronized void third() throws InterruptedException {
        while (!isSecondPrinted) {
            //System.out.println(Thread.currentThread().getName() + " не печатаю три, т.к. двойка еще не распечатана");
            wait();
        }
        System.out.printf("third");
        reset();
    }
}