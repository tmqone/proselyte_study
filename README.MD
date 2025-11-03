
# Module 2.3

Консольное CRUD приложение, которое взаимодействует с БД через Hibernate и позволяет выполнять все CRUD операции над сущностями:
```
Writer (id, firstName, lastName, List<Post> posts);
Post (id, content, created, updated, List<Label> labels, status);
Label (id, name);
PostStatus (enum ACTIVE, UNDER_REVIEW, DELETED);
```





## Зависимости
```
Java (25)
Docker Compose
```

## Запуск проекта

```bash
docker compose up -d  # Поднять postgres в контейнере
./mvnw flyway:migrate # Выполнить миграцию БД
./mvnw package # Сборка проекта
java -jar target/proselyte_study-1.0-SNAPSHOT-jar-with-dependencies.jar # Запуск проекта
```



