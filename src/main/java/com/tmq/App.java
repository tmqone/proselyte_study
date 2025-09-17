package com.tmq;

import java.util.*;

public class App {


    public static void main(String[] args) throws InterruptedException {
        final Foo foo = new Foo();

        System.out.println("3-2-1");
        new Thread(() -> {
            try {
                foo.third();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(300);

        new Thread(() -> {
            try {
                foo.second();
            } catch (InterruptedException e) {
            }
        }).start();
        Thread.sleep(300);

        new Thread(() -> {
            foo.first();
        }).start();
        Thread.sleep(300);

        

        System.out.println();
        System.out.println("---------------------------------------------------------");
        System.out.println("3-1-2");

        new Thread(() -> {
            try {
                foo.third();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(300);

        new Thread(() -> {
            foo.first();
        }).start();
        Thread.sleep(300);

        new Thread(() -> {
            try {
                foo.second();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(300);

        

        System.out.println();
        System.out.println("---------------------------------------------------------");
        System.out.println("2-1-3");

        new Thread(() -> {
            try {
                foo.second();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(300);

        new Thread(() -> {
            foo.first();
        }).start();
        Thread.sleep(300);

        new Thread(() -> {
            try {
                foo.third();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(300);

        System.out.println();
        System.out.println("---------------------------------------------------------");
        System.out.println("2-3-1");

        new Thread(() -> {
            try {
                foo.second();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(300);

        new Thread(() -> {
            try {
                foo.third();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(300);

        new Thread(() -> {
            foo.first();
        }).start();
        Thread.sleep(300);

        System.out.println();
        System.out.println("---------------------------------------------------------");
        System.out.println("1-2-3");

        new Thread(() -> {
            foo.first();
        }).start();
        Thread.sleep(300);

        new Thread(() -> {
            try {
                foo.second();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(300);

        new Thread(() -> {
            try {
                foo.third();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(300);

        System.out.println();
        System.out.println("---------------------------------------------------------");
        System.out.println("1-3-2");

        new Thread(() -> {
            foo.first();
        }).start();
        Thread.sleep(300);

        new Thread(() -> {
            try {
                foo.third();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(300);

        new Thread(() -> {
            try {
                foo.second();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(300);
    }
}
