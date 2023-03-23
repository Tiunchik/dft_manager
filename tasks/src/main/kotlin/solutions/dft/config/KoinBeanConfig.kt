package solutions.dft.config

import org.jetbrains.exposed.sql.Database
import org.koin.core.annotation.Single
import org.koin.core.annotation.Module
import org.mapstruct.factory.Mappers
import solutions.dft.repository.TaskConverter

@Module
class KoinBeanConfig {

    @Single(createdAtStart = true)
    fun database(): Database = Database.connect(
        url = "jdbc:postgresql://localhost:5432/tasks",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "postgres"
//        password = "mysecretpassword"
    )

    @Single fun taskConverter(): TaskConverter = Mappers.getMapper(TaskConverter::class.java)

}