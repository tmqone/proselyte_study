package com.tmq.view;

import com.tmq.controller.WriterController;
import com.tmq.controller.factory.WriterControllerFactory;
import com.tmq.dto.WriterDto;
import com.tmq.exception.*;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class WriterView implements GenericView {
    private static final WriterView INSTANCE = new WriterView();
    private final Scanner scanner = new Scanner(System.in);
    private final WriterControllerFactory factory = new WriterControllerFactory();
    private final WriterController writerController = factory.getController();
    public static WriterView getInstance() {
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
                1. Найти всех авторов
                2. Найти автора по имени
                3. Найти автора по ID
                4. Добавить автора
                5. Изменить автора
                6. Удалить автора
                0. Вернуться в главное меню
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
                    createAuthor();
                    break;

                case "5":
                    updateWriter();
                    break;

                case "6":
                    deleteWriter();
                    break;

                case "0":
                    exit();
                    break;
                default:
                    System.out.println("Некорректный ввод");}
        } catch (WriterNotFoundException e) {
            System.out.println("Автор не найден");
        } catch (NotCorrectInputException e) {
            System.out.println("Некорректный ввод");
        } catch (GeneralException e) {
            System.out.println("Произошла ошибка");
        }
        waitForInput();
        showMenu();
    }

    private void deleteWriter() {
        System.out.print("Введите номер автора: ");
        String id = scanner.nextLine();
        writerController.delete(id);
        System.out.println("Автор был удалён");
    }

    private void updateWriter() {
        System.out.print("Введите номер автора: ");
        String id = scanner.nextLine();
        System.out.print("Введите фамилию автора: ");
        String lastName = scanner.nextLine();
        System.out.print("Введите имя автора: ");
        String firstName = scanner.nextLine();
        writerController.update(id, lastName, firstName);
        System.out.println("Автор был обновлён");
    }

    private void createAuthor() {
        System.out.print("Введите фамилию автора: ");
        String lastName = scanner.nextLine();
        System.out.print("Введите имя автора: ");
        String firstName = scanner.nextLine();
        writerController.save(lastName, firstName);
        System.out.println("Автор сохранен");
    }

    private void findById() {
        System.out.print("Введите ID автора: ");
        String id = scanner.nextLine();
        WriterDto writer = writerController.getById(id);
        System.out.printf("%d.%s %s\n", writer.id(), writer.lastName(), writer.firstName());
    }

    private void findByName() {
        System.out.print("Введите фамилию автора: ");
        String lastName = scanner.nextLine();
        System.out.print("Введите имя автора: ");
        String firstName = scanner.nextLine();
        List<WriterDto> writers = writerController.getByName(lastName, firstName);
        writers.forEach(writer -> System.out.printf("%d.%s %s\n", writer.id(), writer.lastName(), writer.firstName()));
    }

    private void findAll() {
        Optional.of(writerController.getAll())
                .filter(list -> !list.isEmpty())
                .ifPresentOrElse(
                        list -> list.forEach(x -> System.out.printf("%d.%s %s\n",
                                x.id(), x.lastName(), x.firstName())),
                        () -> System.out.println("Пусто...")
                );
    }
}