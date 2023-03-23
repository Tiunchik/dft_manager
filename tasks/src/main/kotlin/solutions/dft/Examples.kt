package solutions.dft

import io.ktor.server.application.*
import org.koin.core.annotation.Single
import org.koin.ktor.ext.get
import solutions.dft.repository.TaskConverter
import solutions.dft.repository.TaskEntity

@Single
class Examples(application: Application) {
    private val taskConverter: TaskConverter = application.get()

    suspend fun transactionDAO_one() {
        // Correct
        sql { TaskEntity.all().map(taskConverter::entityToModel) }
        // java.lang.IllegalStateException: No transaction in context.
        sql { TaskEntity.all() }.map(taskConverter::entityToModel)

        // все действия с Entity должны быть внутри транзакции
    }
}