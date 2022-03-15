# Info

- Тестовое задание **Конвертер валют**. Подробное условие задачи в файле files/**task.pdf**.

- Бекэнд реализован в виде **Spring Boot REST API**.

- В dev-профиле используется БД **PostgreSQL** в контейнере **Docker**. Настройки контейнера указываем
в файле **docker-compose.yaml**. Настройки подключения к БД прописываем
в файле src/main/resources/**application-dev.yaml**. Для миграций используется **Liquibase**.
- Для тестирования используем **in-memory базу данных H2**. Настройки test-профиля прописываем
в файле src/main/resources/**application-test.yaml**.

# Как развернуть приложение в Docker-контейнерах
