package com.tmq;

public class CountDownLatchApp {
    public static void main(String[] args) throws InterruptedException {
        FooCountDownLatch fooCountDownLatch = new FooCountDownLatch();
        new Thread(() -> {
            try {
                fooCountDownLatch.third();
            } catch (InterruptedException e) {}
        }).start();
        Thread.sleep(1000);
        new Thread(() -> {
            try {
                fooCountDownLatch.second();
            } catch (InterruptedException e) {}
        }).start();
        Thread.sleep(1000);
        new Thread(fooCountDownLatch::first).start();
    }
}
