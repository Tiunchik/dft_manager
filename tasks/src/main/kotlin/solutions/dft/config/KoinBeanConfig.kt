package solutions.dft.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.koin.core.annotation.Single
import org.koin.core.annotation.Module
import org.mapstruct.factory.Mappers
import solutions.dft.repository.TaskConverter

@Module
class KoinBeanConfig {

    @Single fun taskConverter(): TaskConverter = Mappers.getMapper(TaskConverter::class.java)

}