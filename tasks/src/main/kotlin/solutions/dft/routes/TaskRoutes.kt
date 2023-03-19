package solutions.dft.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.mapstruct.factory.Mappers
import solutions.dft.Beans
import solutions.dft.schema.TaskConverter
import solutions.dft.schema.TaskEntity
import solutions.dft.schema.TaskRepo

class TT {
    companion object {
        fun runFun() {}
    }
}

// http://127.0.0.1:8080/tasks/
fun Application.tasksRoutes() {
    TT.runFun()
    Beans.database.toString()
    val taskRepo = TaskRepo()
    val converter = Mappers.getMapper(TaskConverter::class.java)
    routing {
        get("/tasks/") {
//            taskRepo.dbQuery(log = true) {
//                TaskEntity.new {
//                    title = "route tasks 1"
//                }
//            }
            call.respond(HttpStatusCode.OK,
//                taskRepo.dbQuery(log = true) { taskRepo.getAllTasks() }
//                taskRepo.dbQuery(log = true) { taskRepo.getAllTasks() }
                taskRepo.dbQuery(log = true) { taskRepo.getAllTasks().map(converter::convertToDto)}
            )
        }
    }
}