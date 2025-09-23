package com.tmq;

public class ReentrantLockFooApp {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLockFoo reentrantLockFoo = new ReentrantLockFoo();
        new Thread(() -> {
            try {
                reentrantLockFoo.third();
            } catch (InterruptedException e) {}
        }).start();
        Thread.sleep(1000);
        new Thread(() -> {
            try {
                reentrantLockFoo.second();
            } catch (InterruptedException e) {}
        }).start();
        Thread.sleep(1000);
        new Thread(reentrantLockFoo::first).start();

        new Thread(() -> {
            try {
                reentrantLockFoo.third();
            } catch (InterruptedException e) {}
        }).start();
        Thread.sleep(1000);
        new Thread(() -> {
            try {
                reentrantLockFoo.second();
            } catch (InterruptedException e) {}
        }).start();
        Thread.sleep(1000);
        new Thread(reentrantLockFoo::first).start();
    }
}
