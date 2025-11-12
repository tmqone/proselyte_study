
# Module 2.3

REST API приложение, которое взаимодействует с файловым хранилищем и предоставляет возможность получать доступ к файлам и истории загрузок.:
```
User -> Integer id, String name, List<Event> events
Event -> Integer id, User user, File file
File -> Integer id, String name, String filePath
```

## Зависимости
```
Docker Compose
```

## Запуск проекта

```bash
docker compose up -d
```



