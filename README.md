
# Module 2.5

REST API приложение на реактивном стэке, обеспечивающего доступ к файловому хранилищу MinIO S3
```
User:
    Integer id
    String username
    Status status (ACTIVE, BLOCKED)
Event:
    Integer id
    User user
    File file
    Status status (CREATED, UPDATED, DELETED)
    LocalDateTime timestamp
File:
    Integer id
    String name
    String location (MinIO S3 URL)
    Status status (ACTIVE, ARCHIVED)
```

## [API документация](https://tmqone.github.io/proselyte_study/)

## Зависимости
```
Docker Compose
```

## Запуск проекта

```bash
1. Прописать переменные окружения в .env файле
2. docker compose up -d
```
