# 1 шаг
## Запуск проекта
В командной строке зайти в корневую папку проекта: ***cd BCL_Project***
___
# 2 шаг
## Сборка и запуск backend
В командной строке ввести ***mvn -pl backend spring-boot:run***
___
# 3 шаг
## Сборка desktop
В новом окне cmd/ps ввести ***cd BCL_Project*** а затем ***mvn -pl desktop clean package***
___
# 4 шаг
## Запуск приложения
В новом окне cmd/ps ввести ***cd BCL_Project*** а затем ***java --module-path "C:\Users\Имя-вашего-компьютера\.m2\repository\org\openjfx\javafx-controls\21.0.4\javafx-controls-21.0.4-win.jar;C:\Users\igorj\.m2\repository\org\openjfx\javafx-graphics\21.0.4\javafx-graphics-21.0.4-win.jar;C:\Users\igorj\.m2\repository\org\openjfx\javafx-base\21.0.4\javafx-base-21.0.4-win.jar;C:\Users\igorj\.m2\repository\org\openjfx\javafx-fxml\21.0.4\javafx-fxml-21.0.4-win.jar" --add-modules javafx.controls,javafx.fxml -jar desktop\target\desktop-1.0.0-jar-with-dependencies.jar***
## Откроется приложение BCL Products Manager
___
# Далее перейдите по адресу http://localhost:8080/h2-console
## Там нужно будет проверить, правильный ли введён пароль. Пароль - BCLyon2024 Логин - staff
___
# API находится по адресу http://localhost:8080/api/products