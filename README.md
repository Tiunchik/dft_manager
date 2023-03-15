## TODO

- docker - как отдельная папка
- - с разными docker-compose под разные случаи - например какие именно?
- postgres Единый сервер для Баз Данных всех сервисов + entry-point.init.d
- liquibase у каждого сервиса свой, интегрированный с ktor - при запуске проекта он прицепом запускает liquibase 
и чекает все вложенный в resources файлы миграции.
- (Макс) сделать интеграцию Dockerfile с Grade task
- (Макс) прикрутить Ktor yaml config 
- прикрутить запуск liquibase с запуском приложухи.
(https://www.liquibase.com/blog/3-ways-to-run-liquibase) 