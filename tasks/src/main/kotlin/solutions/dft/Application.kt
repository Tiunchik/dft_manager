package solutions.dft

import io.ktor.server.application.*
import io.ktor.server.cio.*
import org.koin.core.logger.Level
import org.koin.dsl.module
import org.koin.ksp.generated.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import solutions.dft.config.configureMonitoring
import solutions.dft.config.configureOpenApi
import solutions.dft.config.configureSerialization


fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.main() {
    install(Koin) {
        slf4jLogger(Level.DEBUG)
        val appModule = module {
            single { this@main }
        }
        modules(
             listOf(
                 KoinEntryPointModule().module,
                 appModule
             )
        )
    }
    configureOpenApi()
    configureMonitoring()
    configureSerialization()
}
