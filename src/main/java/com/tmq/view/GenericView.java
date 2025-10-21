package com.tmq.view;

import java.util.Scanner;

public interface GenericView {
    Scanner scanner = new Scanner(System.in);

    void start();
    void exit();
    void showMenu();
    void chooseOption();

    default void clearConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    default void waitForInput(){
        System.out.println("Нажмите Enter, чтобы продолжить...");
        scanner.nextLine();
    }
}