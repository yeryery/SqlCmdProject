**SqlCmd Project**

Приложение для работы с базой данных PostgreSQL

Перед началом выполнения интеграционных тестов на компьютере 
пользователя временно создается база данных 'testbase' с двумя 
таблицами: 'test' и 'users'.

Для подключения к этой базе пользователю необходимо ввести 
корректные имя пользователя и пароль в классе 
\test\java\ua\com\juja\yeryery\integration\Preparator.java

По завершению выполнения тестов база данных удаляется.

Для выполнения операций с таблицами необходимо ввести название 
команды и затем выбрать нужную таблицу из списка существующих.

Для изменения конкретной строки в таблице укажите название 
столбца и его значение в этой строке.