package solutions.dft

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.jetbrains.exposed.sql.Database
import solutions.dft.plugins.configureHTTP
import solutions.dft.plugins.configureMonitoring
import solutions.dft.plugins.configureRouting
import solutions.dft.plugins.configureSerialization
import solutions.dft.routes.tasksRoutes

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

// http://127.0.0.1:8080/tasks/
fun Application.module() {
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    //configureDatabases()
    configureRouting()

    tasksRoutes()
}

// Global
public object Beans {
    val database = Database.connect(
        url = "jdbc:postgresql://localhost:5432/tasks",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "postgres"
    )
}
