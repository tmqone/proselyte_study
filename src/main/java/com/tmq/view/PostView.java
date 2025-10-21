package com.tmq.view;

import com.tmq.controller.PostController;
import com.tmq.controller.factory.GenericControllerFactory;
import com.tmq.controller.factory.PostControllerFactory;
import com.tmq.dto.PostDto;
import com.tmq.exception.*;
import com.tmq.model.Post;

import java.util.Optional;
import java.util.Scanner;

public class PostView implements GenericView {
    private static final PostView INSTANCE = new PostView();
    private final Scanner scanner = new Scanner(System.in);
    private final PostControllerFactory postControllerFactory = new PostControllerFactory();
    private final PostController postController = postControllerFactory.getController();

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
                3. Найти пост по ID
                4. Добавить пост
                5. Изменить пост
                6. Удалить пост
                0. Вернуться в главное меню
                """);
        chooseOption();
        clearConsole();
    }

    @Override
    public void chooseOption() {
        try {
            System.out.print("Введите цифру: ");
            switch (scanner.nextLine()) {
                case "1":
                    findAll();
                    break;

                case "2":
                    findPostsByAuthor();
                    break;

                case "3":
                    findById();
                    break;

                case "4":
                    createPost();
                    break;

                case "5":
                    updatePost();
                    break;

                case "6":
                    deletePost();
                    break;

                case "0":
                    exit();
                    break;
                default:
                    System.out.println("Некорректный ввод");
            }
        } catch (PostNotFoundException e) {
            System.out.println("Пост не найден");
        } catch (NotCorrectInputException e) {
            System.out.println("Некорректный ввод");
        } catch (WriterNotFoundException e) {
            System.out.println("Автор не найден");
        } catch (GeneralException e) {
            System.out.println("Произошла ошибка");
        }
        waitForInput();
        showMenu();
    }

    private void findPostsByAuthor() {
        System.out.print("Введите номер автора: ");
        String id = scanner.nextLine();
        Optional.of(postController.getByWriter(id))
                .filter(list -> !list.isEmpty())
                .ifPresentOrElse(
                        list -> list.forEach(this::printPost), () -> System.out.println("Пусто...")
                );
    }

    private void deletePost() {
        System.out.print("Введите номер поста: ");
        String id = scanner.nextLine();
        postController.delete(id);
        System.out.println("Пост был удалён");
    }

    private void updatePost() {
        System.out.print("Введите номер поста: ");
        String id = scanner.nextLine();
        System.out.print("Введите контент: ");
        String content = scanner.nextLine();
        System.out.print("Введите тэги через запятую: ");
        String labels = scanner.nextLine();
        postController.update(id, labels, content);
        System.out.println("Пост изменен");
    }

    private void createPost() {
        System.out.print("Введите id автора: ");
        String writerId = scanner.nextLine();
        System.out.print("Введите контент поста: ");
        String content = scanner.nextLine();
        System.out.print("Введите тэги через запятую: ");
        String labels = scanner.nextLine();
        postController.save(writerId,labels, content);
        System.out.println("Пост сохранен");
    }

    private void findById() {
        System.out.print("Введите ID поста: ");
        String id = scanner.nextLine();
        PostDto post = postController.getById(id);
        printPost(post);

    }

    private void printPost(PostDto post) {
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf("Содержание: %s\nАвтор: %s\nНомер поста: %d\nТэги: ", post.content(), post.writer(), post.id());
        for (int i = 0; i < post.labels().size(); i++) {
            if (i <= post.labels().size() - 2) {
                System.out.printf("%s,", post.labels().get(i).name());
            } else {
                System.out.printf("%s", post.labels().get(i).name());
            }
        }
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------------------");
    }

    private void findAll() {
        Optional.of(postController.getAll())
                .filter(list -> !list.isEmpty())
                .ifPresentOrElse(
                        list -> list.forEach(this::printPost), () -> System.out.println("Пусто...")
                );
    }
}