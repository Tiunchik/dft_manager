package solutions.dft

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import solutions.dft.plugins.*

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureHTTP()
    configureMonitoring()
    //configureSerialization()
    //configureDatabases()
    configureRouting()
}
