package solutions.dft.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update
import solutions.dft.Beans
import solutions.dft.Beans.taskConverter
import solutions.dft.ValidateDtoException
import solutions.dft.schema.*
import java.time.LocalDateTime
import java.util.*
import kotlin.math.abs

fun Application.tasksRoutes() {
    val ROOT = "/tasks/"
    Beans.database.toString()

    routing {

        post(ROOT) {
            val task = call.receive<TaskCreate>().apply {
                title ?: throw ValidateDtoException("field \"title\" is required, but not present")
                creatorId ?: throw ValidateDtoException("field \"creatorId\" is required, but not present")
                statusId ?: throw ValidateDtoException("field \"statusId\" is required, but not present")
            }
            val code = abs(Random().nextInt()).toString().substring(0..5)
            sql { TaskEntity.new(code) { taskConverter.createDtoToTargetEntityNoId(task, this) } }
            call.respond(HttpStatusCode.Created, mapOf("code" to code))
        }

        get("$ROOT{code}") {
            val code = call.parameters["code"] ?: throw ValidateDtoException("Required path variable \"code\", but not present")
            val task = sql { TaskEntity.findById(code) }
            if (task != null) call.respond(HttpStatusCode.OK, taskConverter.convertToDto(task))
            else call.respond(HttpStatusCode.NotFound)
        }

        get(ROOT) {
            call.respond(HttpStatusCode.OK, sql { TaskEntity.all().map(taskConverter::convertToDto) })
        }

        put("$ROOT{code}") {
            val code = call.parameters["code"] ?: throw ValidateDtoException("Required path variable \"code\", but not present")
            val task = call.receive<TaskUpdate>()
            sql {
                Tasks.update({ Tasks.code eq code }) {
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
            val code = call.parameters["code"] ?: throw ValidateDtoException("Required path variable \"code\", but not present")
            sql { Tasks.deleteWhere { Tasks.code eq code } }
        }

    }
}