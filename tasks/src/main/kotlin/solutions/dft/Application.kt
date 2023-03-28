package solutions.dft

import io.ktor.server.application.*
import io.ktor.server.cio.*
import solutions.dft.config.*


fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.main() {
    configureKoin()
    configureOpenApi()
    configureMonitoring()
    configureSerialization()
    configureExceptionHandleHTTP()

    printRequiredMigration()
    println("http://127.0.0.1:8080/tasks/")
}

