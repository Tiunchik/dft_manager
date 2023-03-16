package solutions.dft.config.koin

import org.jetbrains.exposed.sql.Database
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module
import solutions.dft.plugins.UserService
import solutions.dft.temp.TaskRepo

val baseKoinModule = module {

    single { Database.connect(
        url = "jdbc:postgresql://localhost:5432/tasks",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "mysecretpassword"
    ) } withOptions { createdAtStart() }

    singleOf(::UserService) withOptions { createdAtStart()}
    single { TaskRepo() } withOptions { createdAtStart()}

}