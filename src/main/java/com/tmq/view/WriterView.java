package com.tmq.view;

import com.tmq.controller.WriterController;
import com.tmq.controller.WriterControllerImpl;
import com.tmq.exception.*;
import com.tmq.model.Post;
import com.tmq.model.Writer;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class WriterView implements GenericView {
    private static final WriterView INSTANCE = new WriterView();
    private final Scanner scanner = new Scanner(System.in);
    private final WriterController writerController = WriterControllerImpl.getInstance();

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
                4. Найти все посты автора
                5. Добавить автора
                6. Изменить автора
                7. Удалить автора
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
                    findAllPostsFromAuthor();
                    break;
                case "5":
                    createAuthor();
                    break;

                case "6":
                    updateTag();
                    break;

                case "7":
                    deleteTag();
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
        } catch (GenericExceptionHandler e) {
            System.out.println("Произошла ошибка");
        }
        waitForInput();
        showMenu();
    }

    private void findAllPostsFromAuthor() {
        System.out.print("Введите номер автора: ");
        String id = scanner.nextLine();
        Optional.of(writerController.getAllPostsFromWriter(id))
                .ifPresentOrElse(
                        list -> list.forEach(this::printPost),
                        () -> System.out.println("Пусто...")
                );
    }

    private void deleteTag() {
        System.out.print("Введите номер автора: ");
        String id = scanner.nextLine();
        writerController.delete(id);
        System.out.println("Автор был удалён");
    }

    private void updateTag() {
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
        Writer writer = writerController.getById(id);
        System.out.printf("%d.%s %s\n", writer.getId(), writer.getLastName(), writer.getFirstName());
    }

    private void findByName() {
        System.out.print("Введите фамилию автора: ");
        String lastName = scanner.nextLine();
        System.out.print("Введите имя автора: ");
        String firstName = scanner.nextLine();
        List<Writer> writers = writerController.getByName(lastName, firstName);
        writers.forEach(writer -> System.out.printf("%d.%s %s\n", writer.getId(), writer.getLastName(), writer.getFirstName()));
    }

    private void findAll() {
        Optional.of(writerController.getAll())
                .filter(list -> !list.isEmpty())
                .ifPresentOrElse(
                        list -> list.forEach(x -> System.out.printf("%d.%s %s\n",
                                x.getId(), x.getLastName(), x.getFirstName())),
                        () -> System.out.println("Пусто...")
                );
    }

    private void printPost(Post post) {
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf("Название:%s\nСодержание:%s\nid=%d\nТэги:", post.getTitle(), post.getContent(), post.getId());
        for (int i = 0; i < post.getLabels().size(); i++) {
            if (i <= post.getLabels().size() - 2) {
                System.out.printf("%s,", post.getLabels().get(i).getName());
            } else {
                System.out.printf("%s", post.getLabels().get(i).getName());
            }
        }
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------------------");
    }
}
