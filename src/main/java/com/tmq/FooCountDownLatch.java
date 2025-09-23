package com.tmq;

import java.util.concurrent.CountDownLatch;

public class FooCountDownLatch {
    private CountDownLatch isFirstMade = new CountDownLatch(1);
    private CountDownLatch isSecondMade = new CountDownLatch(1);


    public void first() {
        System.out.print("first");
        isFirstMade.countDown();
    }

    public void second() throws InterruptedException {
        isFirstMade.await();
        System.out.print("second");
        isSecondMade.countDown();
    }

    public void third() throws InterruptedException {
        isSecondMade.await();
        System.out.print("third");
        resetLatch();
    }

    private void resetLatch() {
        isFirstMade = new CountDownLatch(1);
        isSecondMade = new CountDownLatch(1);
    }
}
