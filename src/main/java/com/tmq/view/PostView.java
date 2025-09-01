package com.tmq.view;

import com.tmq.controller.PostController;
import com.tmq.controller.PostControllerImpl;
import com.tmq.exception.GenericExceptionHandler;
import com.tmq.model.Label;
import com.tmq.model.Post;

import java.util.Iterator;
import java.util.List;
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

            case "2":
                break;

            case "3":
                findByTags();
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

    private void findByTags() {
        System.out.print("Введите тэги через запятую: ");
        String labels = scanner.nextLine();
        List<Post> postsList = postController.getByLabels(labels);

        if (postsList.isEmpty()) {
            System.out.println("Пусто...");
        } else {
            postsList.forEach(this::printPost);
        }
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
        try {
            Post post = postController.getById(id);
            printPost(post);
        } catch (GenericExceptionHandler e){
            System.out.println(e.getMessage());
        }

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

    private void findByName() {
        System.out.print("Введите название поста: ");
        String name = scanner.nextLine();

        List<Post> posts = postController.getByName(name);
        if (posts.isEmpty()) {
            System.out.println("Пусто...");
        } else {
            posts.forEach(post -> printPost(post));
        }
    }

    private void findAll() {
        Optional.of(postController.getAll())
                .filter(list -> !list.isEmpty())
                .ifPresentOrElse(
                        list -> list.forEach(x -> printPost(x)), () -> System.out.println("Пусто...")
                );
    }
}
