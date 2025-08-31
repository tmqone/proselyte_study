package com.tmq.view;

import com.tmq.controller.LabelController;
import com.tmq.controller.LabelControllerImpl;
import com.tmq.model.Label;
import com.tmq.model.Status;
import com.tmq.validator.InputValidator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Scanner;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LabelView implements GenericView {
    private static final LabelView INSTANCE = new LabelView();
    private final Scanner scanner = new Scanner(System.in);
    private final LabelController labelController = LabelControllerImpl.getInstance();
    private final InputValidator inputValidator = new InputValidator();
    private final MainView mainView = MainView.getInstance();

    public static LabelView getInstance() {
        return INSTANCE;
    }

    @Override
    public void start() {
        showMenu();
    }

    @Override
    public void exit() {
        mainView.start();
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
                System.out.println("Некорректный ввод");
        }
        waitForInput();
        showMenu();
    }

    private void deleteTag() {
        System.out.print("Введите номер тэга: ");
        String id = scanner.nextLine();
        if (inputValidator.validateLongString(id)){
            Label label = Label.builder().id(Long.parseLong(id)).build();

            if (labelController.delete(label)){
                System.out.println("Тэг был удалён");
            } else {
                System.out.println("Произошла ошибка при удалении");
            }
        }
    }

    private void updateTag() {
        System.out.print("Введите номер тэга: ");
        String id = scanner.nextLine();
        System.out.print("Введите новое название тэга: ");
        String name = scanner.nextLine();

        if(inputValidator.validateLongString(id) && inputValidator.validate(name)){
            if (labelController.update(Label.builder().id(Long.parseLong(id)).name(name).build())) {
                System.out.println("Тэг изменен");
            } else {
                System.out.println("Произошла ошибка при изменении");
            }
        }
    }

    private void createTag() {
        System.out.print("Введите название тэга: ");
        String name = scanner.nextLine();

        if(inputValidator.validate(name)){
            if (labelController.save(Label.builder().name(name).status(Status.ACTIVE).build())) {
                System.out.println("Тэг сохранен");
            } else {
                System.out.println("Произошла ошибка при сохранении");
            }
        }
    }

    private void findById() {
        System.out.print("Введите ID тэга: ");
        String id = scanner.nextLine();
        if (inputValidator.validateLongString(id)){
            labelController.getById(id).ifPresentOrElse(
                    x -> System.out.println(x.getId() + ". " + x.getName()),
                    () -> System.out.println("Пусто..."));
        }
    }

    private void findByName() {
        System.out.print("Введите название тэга: ");
        String name = scanner.nextLine();

        if(inputValidator.validate(name)){
            labelController.getByName(name)
                    .ifPresentOrElse(x -> System.out.println(x.getId() + ". " + x.getName()),
                            () -> System.out.println("Пусто..."));
        }
    }

    private void findAll() {
        List<Label> labels = labelController.getAll();
        if (labels.isEmpty()) {
            System.out.println("Пусто...");
        } else {
            labels.forEach(x -> System.out.println(x.getId() + ". " + x.getName()));
        }
    }
}
