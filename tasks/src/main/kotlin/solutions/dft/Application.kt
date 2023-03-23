package solutions.dft

import io.ktor.server.application.*
import io.ktor.server.cio.*
import solutions.dft.config.configureKoin
import solutions.dft.config.configureMonitoring
import solutions.dft.config.configureOpenApi
import solutions.dft.config.configureSerialization


fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.main() {
    configureKoin()
    configureOpenApi()
    configureMonitoring()
    configureSerialization()

    printRequiredMigration()
    println("http://127.0.0.1:8080/tasks/")
}

