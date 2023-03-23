package solutions.dft

import solutions.dft.schema.TaskEntity
import solutions.dft.schema.Tasks
import solutions.dft.schema.sql

suspend fun transactionDAO_one() {
    sql { TaskEntity.all().map(Beans.taskConverter::convertToDto) }
    sql { TaskEntity.all() }.map(Beans.taskConverter::convertToDto) // java.lang.IllegalStateException: No transaction in context.
}

fun showColumnsPath() {

    println(Tasks.code)
    println(Tasks.id)
    println(Tasks.title)
}