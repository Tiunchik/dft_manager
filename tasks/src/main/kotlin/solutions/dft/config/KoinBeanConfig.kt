package solutions.dft.config

import org.jetbrains.exposed.sql.Database
import org.koin.core.annotation.Single
import org.koin.core.annotation.Module

@Module
class KoinBeanConfig {

    @Single(createdAtStart = true)
    fun database() = Database.connect(
        url = "jdbc:postgresql://localhost:5432/tasks",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "mysecretpassword"
    )

}