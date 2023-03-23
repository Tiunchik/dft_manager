package solutions.dft

import io.ktor.server.application.*
import io.ktor.server.cio.*
import solutions.dft.config.configureMonitoring
import solutions.dft.config.configureOpenApi
import solutions.dft.config.configureSerialization
import solutions.dft.config.configureKoin
import io.ktor.server.engine.*
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers
import solutions.dft.plugins.configureHTTP
import solutions.dft.plugins.configureMonitoring
import solutions.dft.plugins.configureRouting
import solutions.dft.plugins.configureSerialization
import solutions.dft.routes.tasksRoutes
import solutions.dft.schema.*



fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.main() {
    configureKoin()
    configureOpenApi()
    configureMonitoring()
    configureSerialization()

//    tasksRoutes()
    showRequiredMigration()
    println("http://127.0.0.1:8080/tasks/")
}

// По идеи это Дешёвый RuntimeException т.к. не собирает StackTrace
class ValidateDtoException(override val message: String?) : RuntimeException(message, null, false, false)


@Mapper // code-generation
interface TaskConverter {

    fun convertToDto(taskEntity: TaskEntity): Task

    @InheritInverseConfiguration
    @Mapping(target = "id", expression = "java(new org.jetbrains.exposed.dao.id.EntityID(" +
            "task.getCode(), solutions.dft.schema.Tasks.INSTANCE))")
    fun convertToModel(task: Task): TaskEntity

    @InheritInverseConfiguration
    @Mapping(target = "code", ignore = true)
    fun createDtoToTargetEntityNoId(task: TaskCreate, @MappingTarget entity: TaskEntity)

}

fun Application.showRequiredMigration() {
    launch {
        sql {
            val ls = System.lineSeparator();
            val sqlStatements = SchemaUtils
                .statementsRequiredToActualizeScheme(Tasks)
                .joinToString(
                    prefix = "$ls$ls### RUN ### print statementsRequiredToActualizeScheme$ls$ls",
                    postfix = "$ls$ls### END ### print statementsRequiredToActualizeScheme$ls$ls",
                    separator = ls) { "$it;" }
            println(sqlStatements)
        }
    }
}
