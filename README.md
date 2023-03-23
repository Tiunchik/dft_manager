## TODO

- - с разными docker-compose под разные случаи - например какие именно?
- (Макс) (low) сделать интеграцию Dockerfile с Grade task
- прикрутить запуск liquibase с запуском приложухи.
  (https://www.liquibase.com/blog/3-ways-to-run-liquibase)
- для Таски - Миграция, CRUD, COIN, все поля для Task реализовать реально.
- ? model mapper? 🤔
- (low) code-generation для чего-то из exposed(Table, Dao), Model, liquibase(sql || any type of config)

- (Макс) Валидация полей DTO на Делегатах!
- авторизация - на фильтрах или новый слой абстракции
- Перетрахать всё liqui на FlyWay
- (high) Писать Exception в HTTP response, а молчать о них.
- - https://blog.devgenius.io/ktor-rest-apis-exception-handling-1440eac4d06d
- (Даня) mapping DTO -> UpdateStatement for UPD

- Бизнес требования:
https://docs.google.com/document/d/1bvyp0zaBuiniDND4tyG9WmayvYmkB_i1Z73vILyyYwc/edit?usp=sharing


- DONE (Макс) прикрутить COIN
- DONE (Макс) (low) прикрутить Ktor yaml config 