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
- в проде: http://localhost:8083/swagger-ui/index.html

6. Реализована валидация данных с помощью **spring-boot-starter-validation**.

7. Используется **Spring Security**. Для преобразования паролей по **алгоритму bcrypt** используем  
сайт https://www.browserling.com/tools/bcrypt.

8. Фронтенд реализован в виде **SPA**: имеется единственный HTML-файл с вёрсткой, и JS-скрипт
для динамической подгрузки данных с бэкенда. Использован **AngularJS**. Запущенное приложение  
в dev-профиле доступно по адресу http://localhost:8082/ (логин/пароль для входа `user2`/`pswd2`):  
![](https://github.com/aleksey-nsk/currency_converter/blob/master/screenshots/01_app_run.png)  

# Развернуть приложение в проде

1. Развернём приложение в проде. Будем использовать **Docker-контейнеры**. Поднимем 3 контейнера:

       - Бэкенд (Spring Boot REST API на встроенном Tomcat-сервере)
       - Базу данных PostgreSQL
       - AngularJS-фронтенд на сервере Nginx

2. Создаём файл application-prod.yaml для настройки prod-профиля. Также в файле application.yaml
указываем в качестве активного профиль prod.

3. Далее создать конфигурационный
файл src/main/java/com/example/bank_api/config/CorsConfig.java для настройки CORS.




3. В корне проекта создать папку prod

4. В файле index.js меняем базовый урл (порт 8083)

5. Выполнить команду mvn clean package и далее созданный jar-файл скопировать
из папки target в папку prod/services/backend.

6. Файл prod/docker-compose.yaml имеет вид такой-то.

7. Для каждого сервиса указан свой Dockerfile (файл для сборки образа).

8. Есть машина, на которой установлены Docker и утилита docker-compose. Копируем
на эту машину всю папку prod и открываем её в терминале. Далее выполняем
команду docker-compose up --build и видим, что скачиваются нужные образы и
создаются контейнеры.




