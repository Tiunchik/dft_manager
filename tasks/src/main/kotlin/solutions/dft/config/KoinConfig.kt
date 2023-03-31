package solutions.dft.config

import io.ktor.server.application.*
import org.koin.core.logger.Level
import org.koin.dsl.module
import org.koin.ksp.generated.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import solutions.dft.KoinEntryPointModule

fun Application.configureKoin(){
    install(Koin) {
        slf4jLogger(Level.DEBUG)
        val dataBase = module {
            single(createdAtStart = true) { DatabaseFactory.configFlyWayAndCreateDataSource(this@configureKoin) }
        }
        val appModule = module {
            single { this@configureKoin }
        }
        modules(
            listOf(
                KoinEntryPointModule().module,
                appModule,
                dataBase
            )
        )
    }
}