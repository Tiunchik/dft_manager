package solutions.dft.api.rest

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update
import org.koin.core.annotation.Single
import org.koin.ktor.ext.get
import solutions.dft.receiveAndValidate
import solutions.dft.repository.*
import solutions.dft.sql
import java.time.LocalDateTime
import java.util.*
import kotlin.math.abs

@Single(createdAtStart = true)
class TaskController(application: Application) {
    private val ROOT = "/tasks/"
    private val taskConverter: TaskConverter = application.get()

    init {
        application.routing {

            post(ROOT) {
                val task = call.receiveAndValidate<TaskCreate>()
                val code = abs(Random().nextInt()).toString().substring(0..5)
                sql { TaskEntity.new(code) { taskConverter.createDtoToTargetEntityNoId(task, this) } }
                call.respond(HttpStatusCode.Created, mapOf("code" to code))
            }

            get("$ROOT{code}") {
                val code = call.parameters["code"]
                    ?: throw IllegalStateException("Required path variable \"code\", but not present")
                val task = sql { TaskEntity.findById(code) }
                if (task != null) call.respond(HttpStatusCode.OK, taskConverter.entityToModel(task))
                else call.respond(HttpStatusCode.NotFound)
            }

            get(ROOT) {
                call.respond(HttpStatusCode.OK, sql { TaskEntity.all().map(taskConverter::entityToModel) })
            }

            put("$ROOT{code}") {
                val code = call.parameters["code"]
                    ?: throw IllegalStateException("Required path variable \"code\", but not present")
                val task = call.receiveAndValidate<TaskUpdate>()
                sql {
                    TaskTable.update({ TaskTable.code eq code }) {
                        if (task.title != null) it[title] = task.title!!
                        if (task.content != null) it[content] = task.content!!
                        if (task.checkerId != null) it[checkerId] = task.checkerId!!
                        if (task.executorId != null) it[executorId] = task.executorId!!
                        if (task.deadlineAt != null) it[deadlineAt] = task.deadlineAt!!
                        if (task.statusId != null) it[statusId] = task.statusId!!
                        if (task.priority != null) it[priority] = task.priority!!
                        it[updatedAt] = LocalDateTime.now()
                    }
                }
                call.respond(HttpStatusCode.OK)
            }

            delete("$ROOT{code}") {
                val code = call.parameters["code"]
                    ?: throw IllegalStateException("Required path variable \"code\", but not present")
                sql { TaskTable.deleteWhere { TaskTable.code eq code } }
            }

        }
    }
}