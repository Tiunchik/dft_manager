## TODO

- (Макс) Валидация полей DTO на Делегатах!
- (Макс) [low] сделать интеграцию Dockerfile с Gradle task

- (Даня) mapping DTO -> UpdateStatement for UPD

- [high] Писать Exception в HTTP response, а молчать о них.
- - https://blog.devgenius.io/ktor-rest-apis-exception-handling-1440eac4d06d
- прикрутить запуск liquibase с запуском приложухи.
  (https://www.liquibase.com/blog/3-ways-to-run-liquibase)
- - exposed(Table, Dao)
- - liquibase(sql || any type of config)
- Перетрахать всё liqui на FlyWay
- - с разными docker-compose под разные случаи - например какие именно?
- [low] авторизация - на фильтрах или новый слой абстракции
- [low] code-generation для чего-то связанно с БД - уменьшить кол-во представлений.


- Бизнес требования:
https://docs.google.com/document/d/1bvyp0zaBuiniDND4tyG9WmayvYmkB_i1Z73vILyyYwc/edit?usp=sharing


- DONE (Даня) для Таски - Миграция, CRUD, COIN, все поля для Task реализовать реально.
- DONE (Даня) ? model mapper? 🤔 - MapStructure
- DONE (Макс) прикрутить COIN
- DONE (Макс) [low] прикрутить Ktor yaml config 