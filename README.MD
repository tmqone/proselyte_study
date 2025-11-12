
# Module 2.4

REST API приложение, которое взаимодействует с файловым хранилищем и предоставляет возможность получать доступ к файлам и истории загрузок.:
```
User -> Integer id, String name, List<Event> events
Event -> Integer id, User user, File file
File -> Integer id, String name, String filePath
```

## [API документация](https://tmqone.github.io/proselyte_study/)

## Зависимости
```
Docker Compose
```

## Запуск проекта

```bash
docker compose up -d
```
