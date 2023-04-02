package solutions.dft.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.slf4j.LoggerFactory
import javax.sql.DataSource

class DatabaseFactory(val application: Application) {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun connectAndMigrate(): Database {
        log.info("Initialising database")
        return hikariDataSource()
            .also { runFlyway(it) }
            .let { Database.connect(it) }
    }

    private fun runFlyway(datasource: DataSource) {
        val flyway = Flyway.configure().dataSource(datasource).load()
        try {
            flyway.info()
            flyway.migrate()
        } catch (e: Exception) {
            log.error("Exception running flyway migration", e)
            throw e
        }
        log.info("Flyway migration has finished")
    }

    private fun hikariDataSource(): HikariDataSource {
        return application.environment.config.config("ktor.database").let {
            HikariConfig().apply {
                driverClassName = it.propertyOrNull("driverClassName")?.getString()
                jdbcUrl = it.propertyOrNull("jdbcUrl")?.getString()
                maximumPoolSize = it.propertyOrNull("maximumPoolSize")?.getString()?.toInt() ?: 10
                isAutoCommit = it.propertyOrNull("isAutoCommit")?.getString() == "true"
                transactionIsolation = it.propertyOrNull("transactionIsolation")?.getString()
                username = it.propertyOrNull("username")?.getString()
                password = it.propertyOrNull("password")?.getString()
                validate()
            }
        }.let(::HikariDataSource)
    }

    companion object {
        fun configFlyWayAndCreateDataSource(application: Application) = DatabaseFactory(application).connectAndMigrate()
    }
}