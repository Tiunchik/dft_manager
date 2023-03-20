package solutions.dft.api.rest

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.annotation.Single
import org.koin.ktor.ext.get
import solutions.dft.temp.TaskRepo

@Single(createdAtStart = true)
class TaskController(private val application: Application) {
    private val taskRepo: TaskRepo = application.get()

    init {
        application.routing {
            get("/tasks/") {
                call.respond(HttpStatusCode.OK, taskRepo.getAllTasks())
            }
        }
    }
}