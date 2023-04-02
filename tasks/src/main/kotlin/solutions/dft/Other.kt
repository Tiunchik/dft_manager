package solutions.dft

import io.ktor.server.application.*
import io.ktor.server.request.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Если тут что-то развивается во что-то большое, лучше вынести отдельно, а пока так.
 */
class Other


suspend fun <T> sql(log: Boolean = false, block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) {
        if (log) transaction { addLogger(StdOutSqlLogger) }
        block()
    }

val ApplicationCall.httpMethodAndPath: String get() = "${this.request.httpMethod.value} ${this.request.path()}"
