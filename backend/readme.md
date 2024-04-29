Для старта приложения:

1. docker-compose up -d в корне
2. Если это первый запуск, то потребуется создать базу руками в контейнере постгреса (потом она сохранится в volume)
     * docker exec -it customer-db bash - зайти в контейнер
     * psql -U user - зайти в оболочку пскл
     * CREATE DATABASE customer;
     * \c customer - connect to the database
3. Дальше можно стратовать саму аппку через класс Main.class

PS. Не забудь проставить в idea-project structure - sdk и прочюю шляпу + инициализировать проект мавен