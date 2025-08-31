package com.tmq.view;

import com.tmq.Main;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Scanner;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MainView implements GenericView {
    private final Scanner scanner = new Scanner(System.in);
    private final LabelView labelView = LabelView.getInstance();
    private static final MainView INSTANCE = new MainView();

    public static MainView getInstance() {
        return INSTANCE;
    }

    public void start() {
        showMenu();
    }

    public void exit() {
        System.exit(1);
    }

    public void showMenu() {
        clearConsole();
        System.out.println("""
                Меню:
                1. Авторы
                2. Посты
                3. Теги
                4. Выход
                """);
        System.out.print("Выберите опцию:");

        chooseOption();
    }

    public void chooseOption() {
        switch (scanner.nextInt()) {
            case 1, 2:
                System.out.println("Функционал не реализован");
                showMenu();
                break;
            case 3:
                labelView.start();
                break;
            case 4:
                exit();
                break;
            default:
                System.out.println("Некорректный ввод");
                showMenu();
        }
    }
}
