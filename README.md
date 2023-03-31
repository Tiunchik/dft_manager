## TODO

- (Макс) [low] сделать интеграцию Dockerfile с Gradle task

- (Даня) mapping DTO -> UpdateStatement for UPD

- продумать модель User
- сделать миграцию к модели
- прикрутить к полям валидацию и сделать базовую
- сделать REST на модель User (+ mapstruct)

- [low] авторизация - на фильтрах или новый слой абстракции
- прикрутить авторизацию на без пользока, JWT токены

- прикрутить кафку как shared либу, попробовать автозапуск через KOIN для shared либы
- продумать систему синхронных/асинхронных сообщений через кафку между сервисами, дополнить shared либу
- вынести общие модели в отдельную shared либу (юзер, таска и т.д.)

- посмотреть возможность работы с разными файлами пропертей через application.propertiesOrNull, возможно вынести проверти для кафки или базы в отдельный файлы

- [low] code-generation для чего-то связанно с БД - уменьшить кол-во представлений.
- exposed(Table, Dao)
- [low] завести разными docker-compose под разные случаи - например какие именно?

- Бизнес требования:
  https://docs.google.com/document/d/1bvyp0zaBuiniDND4tyG9WmayvYmkB_i1Z73vILyyYwc/edit?usp=sharing

- DONE [low] Перетрахать всё liqui на FlyWay
- DONE прикрутить запуск liquibase/FlyWay с запуском приложухи.
- DONE [high] Писать Exception в HTTP response, а молчать о них.
- - https://blog.devgenius.io/ktor-rest-apis-exception-handling-1440eac4d06d
- DONE (Макс) Валидация полей DTO ~~на Делегатах!~~ либа Valiktor
- DONE (Даня) для Таски - Миграция, CRUD, COIN, все поля для Task реализовать реально.
- DONE (Даня) ? model mapper? 🤔 - MapStructure
- DONE (Макс) прикрутить COIN
- DONE (Макс) [low] прикрутить Ktor yaml config 
