package com.tmq.view;

import com.tmq.controller.LabelController;
import com.tmq.controller.LabelControllerImpl;
import com.tmq.exception.*;
import com.tmq.model.Label;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.Scanner;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LabelView implements GenericView {
    private static final LabelView INSTANCE = new LabelView();
    private final Scanner scanner = new Scanner(System.in);
    private final LabelController labelController = LabelControllerImpl.getInstance();

    public static LabelView getInstance() {
        return INSTANCE;
    }

    @Override
    public void start() {
        showMenu();
    }

    @Override
    public void exit() {
        MainView.getInstance().start();
    }

    @Override
    public void showMenu() {
        clearConsole();
        System.out.println("""
                1. Найти все тэги
                2. Найти тэг по названию
                3. Найти тэг по ID
                4. Добавить тэг
                5. Изменить тэг
                6. Удалить тэг
                7. Вернуться в главное меню
                """);
        chooseOption();
        clearConsole();
    }

    @Override
    public void chooseOption() {
        System.out.print("Введите цифру: ");
        try {
            switch (scanner.nextLine()) {
                case "1":
                    findAll();
                    break;

                case "2":
                    findByName();
                    break;

                case "3":
                    findById();
                    break;

                case "4":
                    createTag();
                    break;

                case "5":
                    updateTag();
                    break;

                case "6":
                    deleteTag();
                    break;

                case "7":
                    exit();
                    break;
                default:
                    System.out.println("Некорректный ввод");}
        } catch (LabelNotFoundException e) {
            System.out.println("Тэг не найден");
        } catch (LabelExistsException e) {
            System.out.println("Тэг уже существует");
        } catch (NotCorrectInputException e) {
            System.out.println("Некорректный ввод");
        } catch (GenericExceptionHandler e) {
            System.out.println("Произошла ошибка");
        }
        waitForInput();
        showMenu();
    }

    private void deleteTag() {
        System.out.print("Введите номер тэга: ");
        String id = scanner.nextLine();
        labelController.delete(id);
        System.out.println("Тэг был удалён");
    }

    private void updateTag() {
        System.out.print("Введите номер тэга: ");
        String id = scanner.nextLine();
        System.out.print("Введите новое название тэга: ");
        String name = scanner.nextLine();
        labelController.update(name, id);
        System.out.println("Тэг был обновлён");
    }

    private void createTag() {
        System.out.print("Введите название тэга: ");
        String name = scanner.nextLine();
        System.out.println(labelController.save(name) ? "Тэг сохранен\n" : "Произошла ошибка при сохранении\n");
    }

    private void findById() {
        System.out.print("Введите ID тэга: ");
        String id = scanner.nextLine();
        Label label = labelController.getById(id);
        System.out.printf("%d. %s\n", label.getId(), label.getName());
    }

    private void findByName() {
        System.out.print("Введите название тэга: ");
        String name = scanner.nextLine();
        Label label = labelController.getByName(name);
        System.out.printf("%d. %s\n", label.getId(), label.getName());
    }

    private void findAll() {
        Optional.of(labelController.getAll())
                .filter(list -> !list.isEmpty())
                .ifPresentOrElse(
                        list -> list.forEach(x -> System.out.printf("%d. %s\n", x.getId(), x.getName())),
                        () -> System.out.println("Пусто...")
                );
    }
}
