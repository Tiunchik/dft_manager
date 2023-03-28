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
        val properties = application.environment.config.config("ktor.database")
        val config = HikariConfig().apply {
            driverClassName = properties.propertyOrNull("driverClassName")?.getString()
            jdbcUrl = properties.propertyOrNull("jdbcUrl")?.getString()
            maximumPoolSize = properties.propertyOrNull("maximumPoolSize")?.getString()?.toInt() ?: 10
            isAutoCommit = properties.propertyOrNull("isAutoCommit")?.getString() == "true"
            transactionIsolation = properties.propertyOrNull("transactionIsolation")?.getString()
            username = properties.propertyOrNull("username")?.getString()
            password = properties.propertyOrNull("password")?.getString()
            validate()
        }
        return HikariDataSource(config)
    }

    companion object {
        fun configFlyWayAndDB(application: Application) = DatabaseFactory(application).connectAndMigrate()
    }
}