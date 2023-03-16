package solutions.dft

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import org.koin.core.logger.Level
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import solutions.dft.config.koin.baseKoinModule
import solutions.dft.plugins.*
import solutions.dft.routing.configureRouting

import solutions.dft.temp.tasksRoutes

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::main)
        .start(wait = true)
}

fun Application.main() {
    install(Koin) {
        slf4jLogger(Level.DEBUG)
        modules(baseKoinModule)
    }
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureDatabases()
    configureRouting()

    println("module(): 1")
    tasksRoutes()
    println("module() : END")
}
