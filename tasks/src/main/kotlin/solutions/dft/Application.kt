package solutions.dft

import io.ktor.server.application.*
import io.ktor.server.cio.*
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


fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

// http://127.0.0.1:8080/tasks/
fun Application.module() {
    configureHTTP()
    configureMonitoring()
    configureSerialization()
//    configureDatabases()
//    configureRouting()


    tasksRoutes()
    showRequiredMigration()
    println("http://127.0.0.1:8080/tasks/")
}

// Global
public object Beans {
    val database = Database.connect(
        url = "jdbc:postgresql://localhost:5432/tasks",
//        url = "jdbc:postgresql://localhost:5432/postgres",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "postgres"
    )
    val taskRepo = TaskRepo()
    val taskConverter: TaskConverter by lazy { Mappers.getMapper(TaskConverter::class.java) }
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
