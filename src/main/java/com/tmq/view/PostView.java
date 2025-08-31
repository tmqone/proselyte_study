package com.tmq.view;

import com.tmq.controller.PostController;
import com.tmq.controller.PostControllerImpl;
import com.tmq.model.Post;
import java.util.Optional;
import java.util.Scanner;

public class PostView implements GenericView {
    private static final PostView INSTANCE = new PostView();
    private final Scanner scanner = new Scanner(System.in);
    private final PostController postController = PostControllerImpl.getInstance();

    public static PostView getInstance() {
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
                1. Найти все посты
                2. Найти посты по автору
                3. Найти посты по тегам
                4. Найти пост по названию
                5. Найти пост по ID
                6. Добавить пост
                7. Изменить пост
                8. Удалить пост
                0. Вернуться в главное меню
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

            case "4":
                findByName();
                break;

            case "5":
                findById();
                break;

            case "6":
                createPost();
                break;

            case "7":
                updatePost();
                break;

            case "8":
                deletePost();
                break;

            case "0":
                exit();
                break;
            default:
                System.out.println("Некорректный ввод");
        }
        waitForInput();
        showMenu();
    }

    private void deletePost() {
        System.out.print("Введите номер поста: ");
        String id = scanner.nextLine();
        System.out.println(postController.delete(id) ? "Пост был удалён\n" : "Произошла ошибка при удалении\n");
    }

    private void updatePost() {
        System.out.print("Введите номер поста: ");
        String id = scanner.nextLine();
        System.out.print("Введите новое название поста: ");
        String name = scanner.nextLine();
        System.out.print("Введите контент: ");
        String content = scanner.nextLine();
        System.out.print("Введите тэги через запятую: ");
        String labels = scanner.nextLine();
        System.out.println(postController.update(name, id, labels, content) ? "Пост изменен\n" : "Произошла ошибка при изменении\n");
    }

    private void createPost() {
        System.out.print("Введите название поста: ");
        String name = scanner.nextLine();
        System.out.print("Введите контент поста: ");
        String content = scanner.nextLine();
        System.out.print("Введите тэги через запятую: ");
        String labels = scanner.nextLine();
        System.out.println(postController.save(name, labels, content) ? "Пост сохранен\n" : "Произошла ошибка при сохранении\n");
    }

    private void findById() {
        System.out.print("Введите ID поста: ");
        String id = scanner.nextLine();
        postController.getById(id)
                .ifPresentOrElse(this::printPost, () -> System.out.println("Пусто..."));

    }

    private void printPost(Post post) {
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf("Название:%s\nСодержание:%s\nid=%d\nТэги:", post.getTitle(), post.getContent(), post.getId());
        post.getLabels().stream().forEach(label -> System.out.printf("%s", label.getName()));
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------------------");
    }

    //TODO Вью должно отображать > 1 значения если они есть
    private void findByName() {
        System.out.print("Введите название поста: ");
        String name = scanner.nextLine();

        postController.getByName(name)
                .ifPresentOrElse(x -> printPost(x), () -> System.out.println("Пусто..."));
    }

    private void findAll() {
        Optional.of(postController.getAll())
                .filter(list -> !list.isEmpty())
                .ifPresentOrElse(
                        list -> list.forEach(x -> printPost(x)), () -> System.out.println("Пусто...")
                );
    }
}
