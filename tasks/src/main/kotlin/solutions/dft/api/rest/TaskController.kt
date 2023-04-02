package solutions.dft.api.rest

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.koin.core.annotation.Single
import org.koin.ktor.ext.get
import solutions.dft.getParameter
import solutions.dft.receiveAndValidate
import solutions.dft.repository.*
import solutions.dft.service.TaskService
import solutions.dft.sql
import java.util.*
import kotlin.math.abs

@Single(createdAtStart = true)
class TaskController(application: Application) {
    private val ROOT = "/tasks"
    private val taskConverter: TaskConverter = application.get()
    private val taskService: TaskService = application.get()

    init {
        application.routing {

            post("ROOT/") {
                val task = call.receiveAndValidate<TaskCreate>()
                val code = abs(Random().nextInt()).toString().substring(0..5)
                sql { TaskEntity.new(code) { taskConverter.createDtoToTargetEntityNoId(task, this) } }
                call.respond(HttpStatusCode.Created, mapOf("code" to code))
            }
            get("$ROOT/{code}") {
                val code = call.getParameter("code")
                val task = sql { TaskEntity.findById(code) }
                if (task != null) call.respond(HttpStatusCode.OK, taskConverter.entityToModel(task))
                else call.respond(HttpStatusCode.NotFound)
            }

            get("ROOT/") {
                call.respond(HttpStatusCode.OK, sql { TaskEntity.all().map(taskConverter::entityToModel) })
            }
            put("$ROOT/{code}") {
                val code = call.getParameter("code")
                val userId = -1L // todo - hardcode
                val taskUpdateDTO = call.receiveAndValidate<TaskUpdate>()
                val taskEntity = sql { TaskEntity[code].apply(taskConverter::entityToModel) }
                taskService.updateTask(code, userId, taskEntity, taskUpdateDTO)
                call.respond(HttpStatusCode.OK)
            }
            delete("$ROOT/{code}") {
                val code = call.getParameter("code")
                sql { TaskTable.deleteWhere { TaskTable.code eq code } }
            }

            // task changes

            get("$ROOT/{code}/commits/") {
                val code = call.getParameter("code")
                call.respond(HttpStatusCode.OK, TaskChangesCommitTable.selectAllForTask(code))
            }
            get("$ROOT/{code}/commits/{uuid}") {
                val code = call.getParameter("code")
                val uuid = call.getParameter("uuid")
                call.respond(HttpStatusCode.OK, TaskChangesCommitTable.selectAllForTask(code, UUID.fromString(uuid)))
            }

        }
    }
}