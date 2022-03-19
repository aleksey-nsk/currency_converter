# Info

1. Тестовое задание **Конвертер валют**. Подробное условие задачи в файле files/**task.pdf**.

2. Бэкенд реализован в виде **Spring Boot REST API**.

3. В dev-профиле используется БД **PostgreSQL** в контейнере **Docker**. Настройки контейнера указываем
в файле **docker-compose.yaml**. Настройки подключения к БД прописываем  
в файле src/main/resources/**application-dev.yaml**. Для миграций используется **Liquibase**.

4. Для тестирования используем **in-memory базу данных H2**. Настройки test-профиля прописываем  
в файле src/main/resources/**application-test.yaml**. Тесты (**интеграционные** и **unit**) создаём
в директории **src/test/java**.

5. Документацию к API генерируем с помощью **Swagger**. Для просмотра документации открыть адрес:
- в dev-профиле: http://localhost:8082/swagger-ui/index.html
- в проде: ???

6. Реализована валидация данных с помощью **spring-boot-starter-validation**.

7. Используется **Spring Security**. Для преобразования паролей по **алгоритму bcrypt** используем
сайт https://www.browserling.com/tools/bcrypt.

8. Фронтенд реализован в виде **SPA**: имеется единственный HTML-файл с вёрсткой, и JS-скрипт
для динамической подгрузки данных с бэкенда. Использован **AngularJS**. Запущенное
приложение в dev-профиле доступно по адресу http://localhost:8082/ (логин/пароль для входа `user2`/`pswd2`):  
![](https://github.com/aleksey-nsk/currency_converter/blob/master/screenshots/01_app_run.png)  

# Развернуть приложение в проде

1. Развернём приложение в проде. Будем использовать **Docker-контейнеры**.
