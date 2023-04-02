package solutions.dft.config

import io.ktor.server.application.*
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.koin.core.annotation.Single
import org.koin.ktor.ext.get
import solutions.dft.repository.TaskChangeEventTable
import solutions.dft.repository.TaskChangesCommitTable
import solutions.dft.repository.TaskTable
import solutions.dft.sql


@Single(createdAtStart = true)
class MigrationPrinter {

    //     todo - если есть норм кастомный @ComponentScan и свои аннотации, то стоит сделать Перехват.
    private val allExposedTables = listOf<Table>(TaskTable, TaskChangeEventTable, TaskChangesCommitTable)

    /** Exposed не идеальна, это хорошо видно по кол-во issue, в этом поле лежат Миграции которые нужно Исключить для показа в консоли.
     * Считается как часть конфига. */
    private val suppressMigration = setOf(
        "ALTER TABLE tasks ALTER COLUMN created_at TYPE TIMESTAMP, ALTER COLUMN created_at SET DEFAULT (CURRENT_TIMESTAMP);",
        "ALTER TABLE tasks ALTER COLUMN updated_at TYPE TIMESTAMP, ALTER COLUMN updated_at SET DEFAULT (CURRENT_TIMESTAMP);",
        "ALTER TABLE tasks ADD CONSTRAINT tasks_code_unique UNIQUE (code);",
    )

    suspend fun printRequiredMigration() {
        sql {
            val ls = System.lineSeparator();
            SchemaUtils
            val sqlStatements = SchemaUtils
                .statementsRequiredToActualizeScheme(*this@MigrationPrinter.allExposedTables.toTypedArray())
                .asSequence()
                .filter(suppressMigration::contains)
                .joinToString(
                    prefix = "$ls$ls### RUN ### print statementsRequiredToActualizeScheme$ls$ls",
                    postfix = "$ls$ls### END ### print statementsRequiredToActualizeScheme$ls$ls",
                    separator = ls
                ) { "$it;" }
            println(sqlStatements)
        }
    }

}

fun Application.configureMigrationPrinter() {
    launch { get<MigrationPrinter>().printRequiredMigration() }
}