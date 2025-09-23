package com.tmq;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockFoo {
    private ReentrantLock reentrantLock = new ReentrantLock();
    private Condition isFirstMAde  = reentrantLock.newCondition();
    private Condition isSecondMade = reentrantLock.newCondition();

    public void first() {
        reentrantLock.lock();
        System.out.print("first");
        isFirstMAde.signal();
        reentrantLock.unlock();
    }

    public void second() throws InterruptedException {
        reentrantLock.lock();
        isFirstMAde.await();
        System.out.print("second");
        isSecondMade.signal();
        reentrantLock.unlock();

    }

    public void third() throws InterruptedException {
        reentrantLock.lock();
        isSecondMade.await();
        System.out.print("third");
        reentrantLock.unlock();
    }
}
