
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

## API документация

<!-- ReDoc -->
<script src="https://cdn.redoc.ly/redoc/latest/bundles/redoc.standalone.js"></script>
<div id="redoc-container"></div>
<script>
  Redoc.init('https://raw.githubusercontent.com/tmqone/proselyte_study/refs/heads/module-2.4/src/main/resources/swagger.yml', {}, document.getElementById('redoc-container'))
</script>
