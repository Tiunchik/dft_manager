package solutions.dft

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import org.jetbrains.exposed.sql.Database
import solutions.dft.plugins.*
import solutions.dft.temp.tasksRoutes

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    println("module() : RUN")
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    //configureDatabases()
    configureRouting()

    println("module(): 1")
    tasksRoutes()
    println("module() : END")
}

// Global
object Beans {
    val database = Database.connect(
        url = "jdbc:postgresql://localhost:5432/tasks",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "postgres"
    )
}
