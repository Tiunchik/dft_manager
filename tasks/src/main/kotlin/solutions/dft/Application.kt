package solutions.dft

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import org.koin.core.context.startKoin
import solutions.dft.plugins.*
import solutions.dft.routing.configureRouting
import org.koin.ksp.generated.*

import solutions.dft.temp.configureTasksRoutes

fun main() {
    startKoin {
        modules(
            KoinEntryPointModule().module
        )
        embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::main)
            .start(wait = true)
    }
}

fun Application.main() {
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureDatabases()
    configureRouting()

    configureTasksRoutes()
}
